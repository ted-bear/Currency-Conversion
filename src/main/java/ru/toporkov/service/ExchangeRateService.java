package ru.toporkov.service;

import ru.toporkov.dao.ExchangeRateDAO;
import ru.toporkov.entity.ExchangeRate;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRateService {

    private static volatile ExchangeRateService instance;
    private final ExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getInstance();

    private ExchangeRateService(){}

    public static ExchangeRateService getInstance() {
        if (instance == null) {
            synchronized (ExchangeRateService.class) {
                if (instance == null) {
                    instance = new ExchangeRateService();
                }
            }
        }
        return instance;
    }

    public List<ExchangeRate> findAll() throws SQLException {
        return exchangeRateDAO.findAll();
    }
}
