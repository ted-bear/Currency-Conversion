package ru.toporkov;

import ru.toporkov.dao.CurrencyDAO;
import ru.toporkov.entity.Currency;
import ru.toporkov.util.ConnectionManager;

public class Main {
    public static void main(String[] args) {
        Currency currency = Currency.builder()
                .fullName("Rubble")
                .code("RUB")
                .sign("₽")
                .build();
        CurrencyDAO currencyDAO = CurrencyDAO.getInstance();

        try {
            currencyDAO.delete(4);
            currencyDAO.save(currency);
        } finally {
            ConnectionManager.closePool();
        }
    }
}
