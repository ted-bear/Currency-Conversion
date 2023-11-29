package ru.toporkov.validator.exception;

import ru.toporkov.validator.ErrorMessage;

public class NotFoundExchangePairException extends ApplicationException {

    public NotFoundExchangePairException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
