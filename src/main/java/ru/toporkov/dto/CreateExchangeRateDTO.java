package ru.toporkov.dto;

import ru.toporkov.entity.Currency;

import java.math.BigDecimal;

public record CreateExchangeRateDTO (Integer baseCurrency,
                                     Integer targetCurrency,
                                     BigDecimal rate) {
}
