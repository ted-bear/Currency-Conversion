package ru.toporkov.service;

import ru.toporkov.dao.CurrencyDAO;
import ru.toporkov.dto.CreateCurrencyDTO;
import ru.toporkov.entity.Currency;
import ru.toporkov.mapper.CreateCurrencyMapper;
import ru.toporkov.validator.CreateCurrencyValidator;
import ru.toporkov.validator.exception.ApplicationException;

import java.sql.SQLException;
import java.util.List;

public class CurrencyService {

    private static volatile CurrencyService currencyService;
    private final CurrencyDAO currencyDAO = CurrencyDAO.getInstance();
    private final CreateCurrencyMapper mapper = CreateCurrencyMapper.getInstance();
    private final CreateCurrencyValidator validator = CreateCurrencyValidator.getInstance();

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

//    По-хорошему тут должны возвращаться DTO'шки, но в нашем случае поля сущности и DTO полностью совпадают
    public List<Currency> findAll() throws SQLException {
        return currencyDAO.findAll();
    }

    public Currency saveCurrency(CreateCurrencyDTO createCurrencyDTO) throws ApplicationException {
        try {
            var valid = validator.isValid(createCurrencyDTO);

            Currency currency = mapper.mapFrom(createCurrencyDTO);
            currencyDAO.save(currency);

            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
