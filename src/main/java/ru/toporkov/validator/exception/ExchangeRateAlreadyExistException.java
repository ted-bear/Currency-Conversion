package ru.toporkov.validator.exception;

import ru.toporkov.validator.ErrorMessage;

public class ExchangeRateAlreadyExistException extends ApplicationException {

    public ExchangeRateAlreadyExistException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
