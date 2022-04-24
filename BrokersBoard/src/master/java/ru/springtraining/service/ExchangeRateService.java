package ru.springtraining.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtraining.mappers.CBCurrencyToCurrencyMapper;
import ru.springtraining.response.ExchangeRateResponse;
import ru.springtraining.response.CBCurrency;
import ru.springtraining.entity.Currency;
import ru.springtraining.entity.ExchangeRate;
import ru.springtraining.entity.ExchangeRateHistory;
import ru.springtraining.repository.ExchangeRateRepository;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExchangeRateService {

    private final ExchangeRateRepository rateRepository;
    private final CurrencyService currencyService;
    private final CBCurrencyToCurrencyMapper cbCurrencyToCurrencyMapper;

    // сайт вида https://www.cbr-xml-daily.ru//archive//yyyy//MM//dd//daily_json.js
    private static String CBSiteArchive = "https://www.cbr-xml-daily.ru//archive//%s//%s//%s//daily_json.js";

    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static LocalDate FIRST_CB_DATE = LocalDate.of(1992, Month.JULY, 1);
    private final static String RUB_CODE = "RUB";
    private final static Integer DEFAULT_NOMINAL = 1;
    private final static Integer DECIMAL_ROUNDUP = 10_000;

    public List<ExchangeRate> getAll() {
        return rateRepository.findAll();
    }

    public ExchangeRate getRateById(Integer id) {
        return rateRepository.findById(id).orElse(null);
    }

    public ExchangeRate add(ExchangeRate rate) {
        return rateRepository.saveAndFlush(rate);
    }

    public ExchangeRate update(ExchangeRate rate) {
        return rateRepository.saveAndFlush(rate);
    }

    public ExchangeRateResponse getByCurrenciesAndDate
            (String firstCurrencyCode, String secondCurrencyCode, LocalDate date) throws Exception {
        Currency firstCurrency = currencyService.findByCode(firstCurrencyCode);
        Currency secondCurrency = currencyService.findByCode(secondCurrencyCode);
        if ((firstCurrency == null) || (secondCurrency == null)) {
            Map<String, CBCurrency> cbCurrencyMap = getCBInfo(date);
            // если информации о валюте нет на сайте ЦБ и это не рубль, то ввод кодов был неверным
            if (isAbsentInCBSiteAndNotRuble(cbCurrencyMap, firstCurrencyCode) ||
                    isAbsentInCBSiteAndNotRuble(cbCurrencyMap, secondCurrencyCode)) {
                return null;
            }
            // если ввод кодов был верен и информации о валюте нет в БД, идем на сайт ЦБ и сохраняем валюту в БД
            if (firstCurrency == null) {
                firstCurrency = createCurrencyFromCBInfoAndSaveToBD(cbCurrencyMap, firstCurrencyCode);
            }
            if (secondCurrency == null) {
                secondCurrency = createCurrencyFromCBInfoAndSaveToBD(cbCurrencyMap, secondCurrencyCode);
            }
            ExchangeRate rate = createExchangeRateFromCBInfo(cbCurrencyMap, date, firstCurrency, secondCurrency);
            add(rate);
            return createExchangeRateResponse(rate, rate.getHistory().get(0));
        }

        ExchangeRate rate = rateRepository.getByCurrencies(firstCurrency, secondCurrency);
        // если валюты были в БД, а их курс нет
        if (rate == null) {
            Map<String, CBCurrency> cbCurrencyMap = getCBInfo(date);
            rate = createExchangeRateFromCBInfo(cbCurrencyMap, date, firstCurrency, secondCurrency);
            add(rate);
            return createExchangeRateResponse(rate, rate.getHistory().get(0));
        } else {
            // если и валюты, и их курс были в БД, то ищем в истории запись за указанную дату
            List<ExchangeRateHistory> history = rate.getHistory();
            for (ExchangeRateHistory entry : history) {
                if (entry.getDate().isEqual(date)) {
                    return createExchangeRateResponse(rate, entry);
                }
            }
            // записи за указанную дату не оказалось, создаем ее и записываем в БД
            Map<String, CBCurrency> cbCurrencyMap = getCBInfo(date);
            ExchangeRateHistory newHistoryEntry =
                    createExchangeRateHistoryFromCBInfo(cbCurrencyMap, date, firstCurrency.getCode(), secondCurrency.getCode());
            newHistoryEntry.setRate(rate);
            rate.getHistory().add(newHistoryEntry);
            update(rate);
            return createExchangeRateResponse(rate, newHistoryEntry);
        }
    }

    private Map<String, CBCurrency> getCBInfo(LocalDate date) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String month;
        if (date.getMonthValue() < 10) {
            month = "0" + date.getMonthValue();
        } else {
            month = "" + date.getMonthValue();
        }
        URL url = new URL(String.format(CBSiteArchive, date.getYear(), month, date.getDayOfMonth()));
        JsonNode node = objectMapper.readTree(url);
        JsonNode valuteNode = node.get("Valute");
        String valuteNodeString = valuteNode.toString().toLowerCase();
        return objectMapper.convertValue(objectMapper.readTree(valuteNodeString), new TypeReference<>() {
        });
    }

    private ExchangeRate createExchangeRateFromCBInfo
            (Map<String, CBCurrency> cbCurrencyMap, LocalDate date, Currency firstCurrency, Currency secondCurrency) {
        ExchangeRate rate = new ExchangeRate();
        rate.setFirstCurrency(firstCurrency);
        rate.setSecondCurrency(secondCurrency);
        rate.setFirstCurrencyValue(DEFAULT_NOMINAL);
        rate.setHistory(new ArrayList<>());
        ExchangeRateHistory historyEntry =
                createExchangeRateHistoryFromCBInfo(cbCurrencyMap, date, firstCurrency.getCode(), secondCurrency.getCode());
        historyEntry.setRate(rate);
        rate.getHistory().add(historyEntry);
        return rate;
    }

    private ExchangeRateHistory createExchangeRateHistoryFromCBInfo
            (Map<String, CBCurrency> cbCurrencyMap, LocalDate date, String firstCurrencyCode, String secondCurrencyCode) {
        ExchangeRateHistory historyEntry = new ExchangeRateHistory();
        historyEntry.setDate(date);
        // вычисление значений курса - для пар валют с рублем и без рубля (кросс-курс)
        if (firstCurrencyCode.equals(RUB_CODE)) {
            CBCurrency bankCurrency = cbCurrencyMap.get(secondCurrencyCode.toLowerCase());
            Double rateValue = 1 / (bankCurrency.getValue() * bankCurrency.getNominal());
            historyEntry.setCurrencyValue(roundUpToFourDecimalPlaces(rateValue));
        } else {
            if (secondCurrencyCode.equals(RUB_CODE)) {
                historyEntry.setCurrencyValue(cbCurrencyMap.get(firstCurrencyCode.toLowerCase()).getValue());
            } else {
                // кросс-курс вычисляется по формуле (value1 * nominal2)/(value2 * nominal1)
                CBCurrency firstBankCurrency = cbCurrencyMap.get(firstCurrencyCode.toLowerCase());
                CBCurrency secondBankCurrency = cbCurrencyMap.get(secondCurrencyCode.toLowerCase());
                Double crossRateValue = (firstBankCurrency.getValue() * secondBankCurrency.getNominal()) /
                        (secondBankCurrency.getValue() * firstBankCurrency.getNominal());
                historyEntry.setCurrencyValue(roundUpToFourDecimalPlaces(crossRateValue));
            }
        }
        return historyEntry;
    }

    private Currency createCurrencyFromCBInfoAndSaveToBD(Map<String, CBCurrency> cbCurrencyMap, String currencyCode) {
        Currency currency = createCurrencyFromCBInfo(cbCurrencyMap, currencyCode);
        return currencyService.add(currency);
    }

    private Currency createCurrencyFromCBInfo(Map<String, CBCurrency> cbCurrencyMap, String currencyCode) {
        CBCurrency bankCurrencyInfo = getBankCurrencyFromCBSite(cbCurrencyMap, currencyCode);
        Currency currency = cbCurrencyToCurrencyMapper.cbCurrencyToCurrency(bankCurrencyInfo);
        currency.setOrg("Other");
        return currency;
    }

    public LocalDate createDateFromRequest(String date) {
        try {
            return LocalDate.parse(date.trim(), DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ExchangeRateResponse createExchangeRateResponse(ExchangeRate rate, ExchangeRateHistory historyEntry) {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setFirstCurrency(rate.getFirstCurrency());
        response.setSecondCurrency(rate.getSecondCurrency());
        response.setFirstCurrencyValue(rate.getFirstCurrencyValue());
        response.setSecondCurrencyValue(historyEntry.getCurrencyValue());
        response.setDate(historyEntry.getDate());
        return response;
    }

    private CBCurrency getBankCurrencyFromCBSite(Map<String, CBCurrency> cbCurrencyMap, String currencyCode) {
        return cbCurrencyMap.get(currencyCode.toLowerCase());
    }

    public boolean isDateValid(LocalDate date) {
        return date.isAfter(FIRST_CB_DATE) || date.isEqual(FIRST_CB_DATE);
    }

    private boolean isAbsentInCBSiteAndNotRuble(Map<String, CBCurrency> cbCurrencyMap, String currencyCode) {
        return !currencyCode.equals(RUB_CODE) && !cbCurrencyMap.containsKey(currencyCode.toLowerCase());
    }

    private Double roundUpToFourDecimalPlaces(Double value) {
        return (double) Math.round(value * DECIMAL_ROUNDUP) / DECIMAL_ROUNDUP;
    }
}
