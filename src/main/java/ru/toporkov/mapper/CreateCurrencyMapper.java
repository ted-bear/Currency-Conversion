package ru.toporkov.mapper;

import ru.toporkov.dto.CreateCurrencyDTO;
import ru.toporkov.entity.Currency;

public class CreateCurrencyMapper {

    private static volatile CreateCurrencyMapper instance;

    private CreateCurrencyMapper() {}

    public Currency mapFrom(CreateCurrencyDTO object) {
        return Currency.builder()
                .fullName(object.fullName())
                .code(object.code())
                .sign(object.sign())
                .build();
    }

    public static CreateCurrencyMapper getInstance() {
        if (instance == null) {
            synchronized (CreateCurrencyMapper.class) {
                if (instance == null) {
                    instance = new CreateCurrencyMapper();
                }
            }
        }
        return instance;
    }
}
