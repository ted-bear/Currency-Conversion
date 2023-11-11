package ru.toporkov.validator.exception;

import lombok.Getter;
import ru.toporkov.validator.ErrorMessage;

@Getter
public class ApplicationException extends RuntimeException {

    private final ErrorMessage error;

    public ApplicationException(ErrorMessage errorMessage) {
        this.error = errorMessage;
    }
}
