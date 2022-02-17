package ru.springtraining.service;

import lombok.RequiredArgsConstructor;
import ru.springtraining.entity.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.springtraining.repository.CurrencyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    //TODO все методы по работе с данными

    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }
        public Currency getById(int id) {
        return currencyRepository.getById(id);
    }

    public Currency add(Currency currency){
        return currencyRepository.save(currency);
    }

}
