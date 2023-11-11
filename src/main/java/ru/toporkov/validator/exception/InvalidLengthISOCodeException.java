package ru.toporkov.validator.exception;

import ru.toporkov.validator.ErrorMessage;

public class InvalidLengthISOCodeException extends ApplicationException {

    public InvalidLengthISOCodeException(ErrorMessage throwable) {
        super(throwable);
    }
}
