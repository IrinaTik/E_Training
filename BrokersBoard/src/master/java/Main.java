import entity.Currency;
import entity.ExchangeRate;
import entity.ExchangeRateHistory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CurrencyService;
import service.ExchangeRateService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(scanBasePackages = "service, dao, entity")
public class Main {

    public static void main(String[] args) throws SQLException {

        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        Currency currencyRubl = context.getBean(Currency.class, "Рубль", "RUB", "Russia");
        Currency currencyDollar = context.getBean(Currency.class, "Доллар", "USD", "USA");
        Currency currencyEuro = context.getBean(Currency.class, "Евро", "EUR", "Europe");
        CurrencyService currencyService = context.getBean(CurrencyService.class);
        addCurrencies(currencyRubl, currencyDollar, currencyEuro, currencyService);

        configureAndAddExchangeRates(context, currencyRubl, currencyDollar, currencyEuro);
    }

    private static void configureAndAddExchangeRates(ApplicationContext context, Currency currencyRubl, Currency currencyDollar, Currency currencyEuro) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        List<ExchangeRateHistory> historyRublDollar = getExchangeRateHistory(context, now, 72.74, 76.52);
        ExchangeRate rateRublDollar = context.getBean(ExchangeRate.class, currencyDollar, currencyRubl, 1, historyRublDollar);
        List<ExchangeRateHistory> historyRublEuro = getExchangeRateHistory(context, now, 82.4, 81.9);
        ExchangeRate rateRublEuro = context.getBean(ExchangeRate.class, currencyDollar, currencyRubl, 1, historyRublEuro);
        List<ExchangeRateHistory> historyEuroDollar = getExchangeRateHistory(context, now, 12.6, 12.5);
        ExchangeRate rateEuroDollar = context.getBean(ExchangeRate.class, currencyDollar, currencyRubl, 1, historyEuroDollar);

        ExchangeRateService exchangeRateService = context.getBean(ExchangeRateService.class);
        addExchangeRates(rateRublDollar, rateRublEuro, rateEuroDollar, exchangeRateService);
    }

    private static List<ExchangeRateHistory> getExchangeRateHistory(ApplicationContext context, LocalDateTime now, Double yesterdayValue, Double nowValue) {
        ExchangeRateHistory historyYesterday = context.getBean(ExchangeRateHistory.class, now.minusDays(1L), yesterdayValue);
        ExchangeRateHistory historyNow = context.getBean(ExchangeRateHistory.class, now, nowValue);
        List<ExchangeRateHistory> history = new ArrayList<>();
        history.add(historyYesterday);
        history.add(historyNow);
        return history;
    }


    private static void addExchangeRates(ExchangeRate rateRublDollar, ExchangeRate rateRublEuro, ExchangeRate rateEuroDollar, ExchangeRateService exchangeRateService) throws SQLException {
        printHistories(rateRublDollar, rateRublEuro, rateEuroDollar);
        exchangeRateService.add(rateRublDollar);
        exchangeRateService.add(rateRublEuro);
        exchangeRateService.add(rateEuroDollar);
        exchangeRateService.getAll().stream().forEach(System.out::println);
    }

    private static void printHistories(ExchangeRate rateRublDollar, ExchangeRate rateRublEuro, ExchangeRate rateEuroDollar) {
        System.out.println("RUB/USD history: " + rateRublDollar.getHistory());
        System.out.println("RUB/EUR history: " + rateRublEuro.getHistory());
        System.out.println("EUR/USD history: " + rateEuroDollar.getHistory());
    }

    private static void addCurrencies(Currency currencyRubl, Currency currencyDollar, Currency currencyEuro, CurrencyService currencyService) throws SQLException {
        currencyService.add(currencyRubl);
        currencyService.add(currencyDollar);
        currencyService.add(currencyEuro);
        currencyService.getAll().stream().forEach(System.out::println);
    }

}
