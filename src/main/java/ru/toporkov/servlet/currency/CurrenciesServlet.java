package ru.toporkov.servlet.currency;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.toporkov.entity.Currency;
import ru.toporkov.service.CurrencyService;
import ru.toporkov.util.UrlPath;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(UrlPath.CURRENCIES)
public class CurrenciesServlet extends HttpServlet {

    private static final CurrencyService currencyService = CurrencyService.getInstance();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> allCurrencies;
        try {
            allCurrencies = currencyService.findAll();
        } catch (SQLException e) {
            throw new ServletException("DB is not available");
        }
        var currenciesJson = gson.toJson(allCurrencies);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (var writer = resp.getWriter()) {
            writer.write(currenciesJson);
        }
    }
}
