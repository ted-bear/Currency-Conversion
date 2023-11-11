package ru.toporkov.validator.exception;

import ru.toporkov.validator.ErrorMessage;

public class CurrencyAlreadyExistException extends ApplicationException {

    public CurrencyAlreadyExistException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
