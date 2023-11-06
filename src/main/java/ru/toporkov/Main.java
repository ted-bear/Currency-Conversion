package ru.toporkov;

import ru.toporkov.dao.CurrencyDAO;
import ru.toporkov.dao.ExchangeRateDAO;
import ru.toporkov.entity.Currency;
import ru.toporkov.entity.ExchangeRate;
import ru.toporkov.util.ConnectionManager;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Currency currency = Currency.builder()
                .fullName("Rubble")
                .code("RUB")
                .sign("₽")
                .build();
        CurrencyDAO currencyDAO = CurrencyDAO.getInstance();
        ExchangeRateDAO exchangeRateDAO = ExchangeRateDAO.getInstance();

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .baseCurrencyId(1)
                .targetCurrencyId(2)
                .rate(BigDecimal.TWO)
                .build();

        try {
            System.out.println(exchangeRateDAO.findById(1));
        } finally {
            ConnectionManager.closePool();
        }
    }
}
