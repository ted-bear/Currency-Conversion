package ru.toporkov.validator;

import java.sql.SQLException;

public interface Validator<T> {

    boolean isValid(T object) throws SQLException;
}
