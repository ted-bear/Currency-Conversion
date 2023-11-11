package ru.toporkov.validator;

import lombok.Value;

@Value(staticConstructor = "of")
public class Error {
    String status;
    String message;
}
