package ru.springtraining.service;


import ru.springtraining.dao.ExchangeRateDAO;
import ru.springtraining.entity.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateDAO exchangeRateDAO;

    //TODO

    public List<ExchangeRate> getAll() throws SQLException {
        return exchangeRateDAO.getAll();
    }

    public ExchangeRate getById(int id) throws SQLException {
        return exchangeRateDAO.getById(id);
    }

    public boolean add(ExchangeRate rate) throws SQLException {
        return exchangeRateDAO.add(rate);
    }

    public void update(String query) throws SQLException {

    }

    public void delete(String query) throws SQLException {

    }

}
