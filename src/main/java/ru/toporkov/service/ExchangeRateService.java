package ru.toporkov.service;

import ru.toporkov.dao.ExchangeRateDAO;
import ru.toporkov.dto.CreateExchangeRateDTO;
import ru.toporkov.entity.ExchangeRate;
import ru.toporkov.mapper.CreateExchangeRateMapper;
import ru.toporkov.validator.CreateExchangeRateValidator;
import ru.toporkov.validator.exception.ApplicationException;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRateService {

    private static volatile ExchangeRateService instance;
    private final ExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getInstance();
    private final CreateExchangeRateMapper exchangeRateMapper = CreateExchangeRateMapper.getInstance();
    private final CreateExchangeRateValidator exchangeRateValidator = CreateExchangeRateValidator.getInstance();

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

    public ExchangeRate saveExchangeRate(CreateExchangeRateDTO createExchangeRateDTO) throws ApplicationException {
        try {
            exchangeRateValidator.isValid(createExchangeRateDTO);

            ExchangeRate exchangeRate = exchangeRateMapper.mapFrom(createExchangeRateDTO);
            exchangeRateDAO.save(exchangeRate);

            return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
