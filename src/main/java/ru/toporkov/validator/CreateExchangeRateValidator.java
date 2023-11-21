package ru.toporkov.validator;

import ru.toporkov.dao.ExchangeRateDAO;
import ru.toporkov.dto.CreateExchangeRateDTO;
import ru.toporkov.validator.exception.ApplicationException;
import ru.toporkov.validator.exception.ExchangeRateAlreadyExistException;
import ru.toporkov.validator.exception.InvalidFormFieldException;

import java.sql.SQLException;

import static ru.toporkov.validator.ErrorMessage.EXCHANGE_RATE_ALREADY_EXISTS;
import static ru.toporkov.validator.ErrorMessage.INVALID_FORM_FIELD;

public class CreateExchangeRateValidator implements Validator<CreateExchangeRateDTO> {

    private static volatile CreateExchangeRateValidator instance;
    private final ExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getInstance();

    private CreateExchangeRateValidator() {}

    public static CreateExchangeRateValidator getInstance() {
        if (instance == null) {
            synchronized (CreateExchangeRateValidator.class) {
                if (instance == null) {
                    instance = new CreateExchangeRateValidator();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isValid(CreateExchangeRateDTO object) throws SQLException, ApplicationException {

        if (object.baseCurrency() == null || object.targetCurrency() == null) {
            throw new InvalidFormFieldException(INVALID_FORM_FIELD);
        }

        if (exchangeRateDAO.findByIds(object.baseCurrency(), object.targetCurrency()).isPresent()) {
            throw new ExchangeRateAlreadyExistException(EXCHANGE_RATE_ALREADY_EXISTS);
        }

        return true;
    }
}
