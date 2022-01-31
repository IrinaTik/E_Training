import entity.Currency;
import entity.ExchangeRate;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CurrencyService;
import service.ExchangeRateService;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Calendar;

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
        Calendar calendar = Calendar.getInstance();
        ExchangeRate rateRublDollar = context.getBean(ExchangeRate.class, currencyDollar, currencyRubl, calendar, new AbstractMap.SimpleEntry<>(1, 72.74));
        ExchangeRate rateRublEuro = context.getBean(ExchangeRate.class, currencyEuro, currencyRubl, calendar, new AbstractMap.SimpleEntry<>(1, 82.4));
        ExchangeRate rateEuroDollar = context.getBean(ExchangeRate.class, currencyEuro, currencyDollar, calendar, new AbstractMap.SimpleEntry<>(1, 12.6));
        ExchangeRateService exchangeRateService = context.getBean(ExchangeRateService.class);
        addExchangeRates(rateRublDollar, rateRublEuro, rateEuroDollar, exchangeRateService);
    }

    private static void addExchangeRates(ExchangeRate rateRublDollar, ExchangeRate rateRublEuro, ExchangeRate rateEuroDollar, ExchangeRateService exchangeRateService) throws SQLException {
        exchangeRateService.add(rateRublDollar);
        exchangeRateService.add(rateRublEuro);
        exchangeRateService.add(rateEuroDollar);
        System.out.println(exchangeRateService.getAll());
    }

    private static void addCurrencies(Currency currencyRubl, Currency currencyDollar, Currency currencyEuro, CurrencyService currencyService) throws SQLException {
        currencyService.add(currencyRubl);
        currencyService.add(currencyDollar);
        currencyService.add(currencyEuro);
        System.out.println(currencyService.getAll());
    }

}
