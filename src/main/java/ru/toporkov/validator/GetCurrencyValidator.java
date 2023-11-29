package ru.toporkov.validator;

import ru.toporkov.validator.exception.InvalidLengthISOCodeException;

import static ru.toporkov.validator.ErrorMessage.INVALID_CURRENCY_ISO_CODE;

public class GetCurrencyValidator implements Validator<String> {

    private static volatile GetCurrencyValidator instance;

    private GetCurrencyValidator() {}

    public static GetCurrencyValidator getInstance() {
        if (instance == null) {
            synchronized (GetCurrencyValidator.class) {
                if (instance == null) {
                    instance = new GetCurrencyValidator();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isValid(String isoCode) throws InvalidLengthISOCodeException {

        if (isoCode == null || isoCode.length() != 3) {
            throw new InvalidLengthISOCodeException(INVALID_CURRENCY_ISO_CODE);
        }

        return true;
    }
}
