package ru.toporkov.validator;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public enum ErrorMessage {

    INVALID_FORM_FIELD(HttpServletResponse.SC_BAD_REQUEST, "A required form field is missing"),
    CURRENCY_ALREADY_EXISTS(HttpServletResponse.SC_CONFLICT, "Currency with this code already exists"),
    INVALID_CURRENCY_ISO_CODE(HttpServletResponse.SC_BAD_REQUEST, "Length of ISO code should be 3"),
    DATABASE_UNAVAILABLE(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable"),
    EXCHANGE_RATE_ALREADY_EXISTS(HttpServletResponse.SC_CONFLICT, "Exchange rate with this currencies already exists"),
    NO_SUCH_EXCHANGE_PAIR(HttpServletResponse.SC_NOT_FOUND, "No such exchange pair");

    private final String message;
    private final Integer status;

    ErrorMessage(Integer status, String message) {
        this.message = message;
        this.status = status;
    }
}
