package ru.toporkov.validator.exception;

public class DAOException extends RuntimeException {

    public DAOException(Throwable throwable) {
        super(throwable);
    }
}
