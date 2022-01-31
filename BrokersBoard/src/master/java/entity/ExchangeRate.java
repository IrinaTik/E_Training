package entity;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

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
    private Calendar date;
    private Map.Entry<Integer, Double> rate; //используется пара, так как для некоторых валют обменный курс может быть не 1 к скольки-то, а 10 или 100 к скольки-то

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return "Exchange rate for " +
                firstCurrency.getCode() + "/" + secondCurrency.getCode() +
                " at " + dateFormat.format(this.date.getTime()) +
                " is " + rate.getKey() + " to " + rate.getValue().toString();
    }
}
