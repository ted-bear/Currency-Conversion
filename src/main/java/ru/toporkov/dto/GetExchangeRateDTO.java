package ru.toporkov.dto;

import ru.toporkov.entity.Currency;

import java.math.BigDecimal;

public record GetExchangeRateDTO (Integer id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
}
