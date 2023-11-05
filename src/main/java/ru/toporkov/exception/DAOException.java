package ru.toporkov.exception;

public class DAOException extends RuntimeException {

    public DAOException(Throwable throwable) {
        super(throwable);
    }
}
