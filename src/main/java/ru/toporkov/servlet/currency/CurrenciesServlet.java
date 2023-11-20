package ru.toporkov.servlet.currency;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.toporkov.dto.CreateCurrencyDTO;
import ru.toporkov.entity.Currency;
import ru.toporkov.service.CurrencyService;
import ru.toporkov.util.UrlPath;
import ru.toporkov.validator.Error;
import ru.toporkov.validator.exception.ApplicationException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static ru.toporkov.validator.ErrorMessage.DATABASE_UNAVAILABLE;

@WebServlet(UrlPath.CURRENCIES)
public class CurrenciesServlet extends HttpServlet {

    private static final CurrencyService currencyService = CurrencyService.getInstance();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Currency> allCurrencies;
        String responseJson;

        try {
            allCurrencies = currencyService.findAll();
            responseJson = gson.toJson(allCurrencies);
        } catch (SQLException e) {
            responseJson = gson.toJson(Error.of(String.valueOf(DATABASE_UNAVAILABLE.getStatus()), DATABASE_UNAVAILABLE.getMessage()));
            resp.setStatus(DATABASE_UNAVAILABLE.getStatus());
        }

        try (var writer = resp.getWriter()) {
            writer.write(responseJson);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var currencyDTO = new CreateCurrencyDTO(req.getParameter("code").trim(),
                req.getParameter("name").trim(),
                req.getParameter("sign").trim());
        Currency entity;
        String responseJson;

        try {
            entity = currencyService.saveCurrency(currencyDTO);
            responseJson = gson.toJson(entity);
        } catch (ApplicationException e) {
            responseJson = gson.toJson(Error.of(String.valueOf(e.getError().getStatus()), e.getError().getMessage()));
            resp.setStatus(e.getError().getStatus());
        }

        try (var writer = resp.getWriter()) {
            writer.write(responseJson);
        }
    }
}
