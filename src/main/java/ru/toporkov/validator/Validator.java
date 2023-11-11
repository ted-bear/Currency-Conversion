package ru.toporkov.validator;

public interface Validator<T> {

    boolean isValid(T object);
}
