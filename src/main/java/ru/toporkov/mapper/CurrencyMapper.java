package ru.toporkov.mapper;

import ru.toporkov.dto.CreateCurrencyDTO;

import java.nio.charset.StandardCharsets;

public class CurrencyMapper {

    public static CreateCurrencyDTO mapStringToCreateCurrencyDTO(String str) {
        var strings = str.split("&");
        String fullName = null;
        String sign = null;
        String code = null;

        for (String string : strings) {
            String[] pair = string.split("=");
            String key = pair[0];
            String value = pair[1];

            switch (key) {
                case "name" -> fullName = value;
                case "sign" -> sign = value;
                case "code" -> code = new String(value.getBytes(), StandardCharsets.UTF_8);
            }
        }

        return new CreateCurrencyDTO(code, fullName, sign);
    }
}
