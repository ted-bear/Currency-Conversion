package ru.toporkov.service;

import ru.toporkov.dao.CurrencyDAO;
import ru.toporkov.dao.ExchangeRateDAO;
import ru.toporkov.dto.CreateExchangeRateDTO;
import ru.toporkov.dto.GetExchangeRateDTO;
import ru.toporkov.entity.Currency;
import ru.toporkov.entity.ExchangeRate;
import ru.toporkov.mapper.CreateExchangeRateMapper;
import ru.toporkov.mapper.GetExchangeRateMapper;
import ru.toporkov.validator.CreateExchangeRateValidator;
import ru.toporkov.validator.ErrorMessage;
import ru.toporkov.validator.GetExchangeRateValidator;
import ru.toporkov.validator.exception.ApplicationException;
import ru.toporkov.validator.exception.NotFoundExchangePairException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static ru.toporkov.validator.ErrorMessage.NO_SUCH_EXCHANGE_PAIR;

public class ExchangeRateService {

    private static volatile ExchangeRateService instance;
    private final ExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getInstance();
    private final CurrencyDAO currencyDAO = CurrencyDAO.getInstance();
    private final CreateExchangeRateMapper exchangeRateMapper = CreateExchangeRateMapper.getInstance();
    private final GetExchangeRateMapper getExchangeRateMapper = GetExchangeRateMapper.getInstance();
    private final CreateExchangeRateValidator createExchangeRateValidator = CreateExchangeRateValidator.getInstance();
    private final GetExchangeRateValidator getExchangeRateValidator = GetExchangeRateValidator.getInstance();

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

    public GetExchangeRateDTO saveExchangeRate(CreateExchangeRateDTO createExchangeRateDTO) throws ApplicationException {
        try {
            createExchangeRateValidator.isValid(createExchangeRateDTO);

            ExchangeRate exchangeRate = exchangeRateMapper.mapFrom(createExchangeRateDTO);
            exchangeRateDAO.save(exchangeRate);

            return getExchangeRateMapper.mapFrom(exchangeRate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GetExchangeRateDTO findByIsoCode(String exchangeRateString) throws ApplicationException {
        try {
            getExchangeRateValidator.isValid(exchangeRateString);

            var baseCurrencyCode = exchangeRateString.substring(0, 3);
            var targetCurrencyCode = exchangeRateString.substring(3);

            var baseCurrency = currencyDAO.findByIsoCode(baseCurrencyCode);
            var targetCurrency = currencyDAO.findByIsoCode(targetCurrencyCode);

            Optional<ExchangeRate> exchangeRate = Optional.empty();

            if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
                exchangeRate = exchangeRateDAO.findByIds(
                        baseCurrency.get().getId(),
                        targetCurrency.get().getId()
                );
            }

             return getExchangeRateMapper.mapFrom(exchangeRate.orElseThrow(() -> new NotFoundExchangePairException(NO_SUCH_EXCHANGE_PAIR)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GetExchangeRateDTO updateExchangeRate(BigDecimal newRate, String codes) throws ApplicationException {
        try {
            getExchangeRateValidator.isValid(codes);

            var baseCurrencyCode = codes.substring(0, 3);
            var targetCurrencyCode = codes.substring(3);

            var baseCurrency = currencyDAO.findByIsoCode(baseCurrencyCode);
            var targetCurrency = currencyDAO.findByIsoCode(targetCurrencyCode);

            Optional<ExchangeRate> exchangeRate = Optional.empty();

            if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
                var newExchangeRate = ExchangeRate
                        .builder()
                        .rate(newRate)
                        .baseCurrencyId(baseCurrency.get().getId())
                        .targetCurrencyId(targetCurrency.get().getId())
                        .build();

                exchangeRate = exchangeRateDAO.update(newExchangeRate);
            }

            return getExchangeRateMapper.mapFrom(exchangeRate.orElseThrow(() -> new NotFoundExchangePairException(NO_SUCH_EXCHANGE_PAIR)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
