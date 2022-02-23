package ru.springtraining.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtraining.entity.ExchangeRate;
import ru.springtraining.repository.ExchangeRateRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExchangeRateService {

    private final ExchangeRateRepository rateRepository;

    public List<ExchangeRate> getAll() {
        return rateRepository.findAll();
    }

    public ExchangeRate getById(Integer id) {
        return rateRepository.getById(id);
    }


}
