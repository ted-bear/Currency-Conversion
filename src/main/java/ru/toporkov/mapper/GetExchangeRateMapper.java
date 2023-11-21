package ru.toporkov.mapper;

import ru.toporkov.dao.CurrencyDAO;
import ru.toporkov.dto.GetExchangeRateDTO;
import ru.toporkov.entity.ExchangeRate;

public class GetExchangeRateMapper {

    private static volatile GetExchangeRateMapper instance;
    private final CurrencyDAO currencyDAO = CurrencyDAO.getInstance();

    private GetExchangeRateMapper() {}

    public GetExchangeRateDTO mapFrom(ExchangeRate object) {
        var baseCurrency = currencyDAO.findById(object.getBaseCurrencyId());
        var targetCurrency = currencyDAO.findById(object.getTargetCurrencyId());
        GetExchangeRateDTO dto = null;

        if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
            dto = new GetExchangeRateDTO(object.getId(), baseCurrency.get(),
                    targetCurrency.get(),
                    object.getRate());
        }

        return dto;
    }

    public static GetExchangeRateMapper getInstance() {
        if (instance == null) {
            synchronized (GetExchangeRateMapper.class) {
                if (instance == null) {
                    instance = new GetExchangeRateMapper();
                }
            }
        }
        return instance;
    }
}
