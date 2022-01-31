package service;

import dao.CurrencyDAO;
import entity.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyDAO currencyDAO;

    //TODO

    public List<Currency> getAll() throws SQLException {
        return currencyDAO.getAll();
    }

    public Currency getById(int id) throws SQLException {
        return currencyDAO.getById(id);
    }

    public boolean add(Currency currency) throws SQLException {
        return currencyDAO.add(currency);
    }

    public void update(String query) throws SQLException {

    }

    public void delete(String query) throws SQLException {

    }

}
