package entity;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Component
@Scope("prototype")
public class ExchangeRate {

    private Currency firstCurrency;
    private Currency secondCurrency;
    private Integer firstCurrencyValue;
    private List<ExchangeRateHistory> history;

    @Override
    public String toString() {
        return "Exchange rate for " +
                firstCurrency.getCode() + "/" + secondCurrency.getCode() +
                " is " + firstCurrencyValue + " to " + history.get(history.size() - 1).toString();
    }
}
