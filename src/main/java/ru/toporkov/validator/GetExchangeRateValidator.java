package ru.toporkov.validator;

import ru.toporkov.dao.ExchangeRateDAO;
import ru.toporkov.validator.exception.ApplicationException;
import ru.toporkov.validator.exception.InvalidLengthISOCodeException;

import static ru.toporkov.validator.ErrorMessage.INVALID_CURRENCY_ISO_CODE;

public class GetExchangeRateValidator implements Validator<String> {

    private final ExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getInstance();

    private static volatile GetExchangeRateValidator instance;

    private GetExchangeRateValidator() {}

    public static GetExchangeRateValidator getInstance() {
        if (instance == null) {
            synchronized (GetExchangeRateValidator.class) {
                if (instance == null) {
                    instance = new GetExchangeRateValidator();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isValid(String isoCode) throws ApplicationException {

        if (isoCode == null || isoCode.length() != 6) {
            throw new InvalidLengthISOCodeException(INVALID_CURRENCY_ISO_CODE);
        }

        return true;
    }
}
