package ru.toporkov.validator.exception;

import ru.toporkov.validator.ErrorMessage;

public class InvalidFormFieldException extends ApplicationException {

    public InvalidFormFieldException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
