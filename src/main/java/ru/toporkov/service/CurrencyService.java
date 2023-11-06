package ru.toporkov.service;

import ru.toporkov.dao.CurrencyDAO;
import ru.toporkov.entity.Currency;

import java.sql.SQLException;
import java.util.List;

public class CurrencyService {

    private static volatile CurrencyService currencyService;
    private static final CurrencyDAO currencyDAO = CurrencyDAO.getInstance();

    private CurrencyService() {}

    public static CurrencyService getInstance() {
        if (currencyService == null) {
            synchronized (CurrencyService.class) {
                if (currencyService == null) {
                    currencyService = new CurrencyService();
                }
            }
        }
        return currencyService;
    }

//    По-хорошему тут должны возвращаться DTO'шки, но в нашем случе поля сущности и DTO полностью совпадают
    public List<Currency> findAll() throws SQLException {
        return currencyDAO.findAll();
    }
}
