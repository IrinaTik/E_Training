package ru.springtraining.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// JPA
@Entity
@Table(name = "exchange_rate_history")
public class ExchangeRateHistory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "rate_id")
    private ExchangeRate rate;

    @Column(name = "date_time")
    private LocalDateTime date;

    @Column(name = "currency_value")
    private Double currencyValue;

    private String formatDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExchangeRateHistory that = (ExchangeRateHistory) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return currencyValue + " at " + formatDate();
    }
}
