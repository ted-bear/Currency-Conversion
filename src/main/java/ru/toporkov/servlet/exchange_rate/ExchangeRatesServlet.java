package ru.toporkov.servlet.exchange_rate;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.toporkov.entity.ExchangeRate;
import ru.toporkov.service.ExchangeRateService;
import ru.toporkov.validator.Error;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static ru.toporkov.util.UrlPath.EXCHANGE_RATES;
import static ru.toporkov.validator.ErrorMessage.DATABASE_UNAVAILABLE;

@WebServlet(EXCHANGE_RATES)
public class ExchangeRatesServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRate> result;
        String jsonResponse;

        try {
            result = exchangeRateService.findAll();
            jsonResponse = gson.toJson(result);
        } catch (SQLException e) {
            jsonResponse = gson.toJson(Error.of(String.valueOf(DATABASE_UNAVAILABLE.getStatus()), DATABASE_UNAVAILABLE.getMessage()));
            resp.setStatus(DATABASE_UNAVAILABLE.getStatus());
        }

        try (var writer = resp.getWriter()) {
            writer.write(jsonResponse);
        }
    }
}
