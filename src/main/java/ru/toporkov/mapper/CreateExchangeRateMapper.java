package ru.toporkov.mapper;

import ru.toporkov.dto.CreateExchangeRateDTO;
import ru.toporkov.entity.ExchangeRate;

public class CreateExchangeRateMapper {

    private static volatile CreateExchangeRateMapper instance;

    private CreateExchangeRateMapper() {}

    public ExchangeRate mapFrom(CreateExchangeRateDTO object) {
        return ExchangeRate.builder()
                .baseCurrencyId(object.baseCurrency())
                .targetCurrencyId(object.targetCurrency())
                .rate(object.rate())
                .build();
    }

    public static CreateExchangeRateMapper getInstance() {
        if (instance == null) {
            synchronized (CreateExchangeRateMapper.class) {
                if (instance == null) {
                    instance = new CreateExchangeRateMapper();
                }
            }
        }
        return instance;
    }
}
