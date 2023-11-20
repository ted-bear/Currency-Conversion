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
import ru.toporkov.validator.Error;
import ru.toporkov.validator.exception.ApplicationException;

import java.io.IOException;

@WebServlet(UrlPath.CURRENCY + "/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var uriParts = req.getRequestURI().split("/");
        var isoCode = uriParts[uriParts.length - 1];
        Currency currencyByISOCode;
        String responseJson;

        try {
            currencyByISOCode = currencyService.getCurrencyByISOCode(isoCode);
            responseJson = gson.toJson(currencyByISOCode);
        } catch (ApplicationException e) {
            responseJson = gson.toJson(Error.of(String.valueOf(e.getError().getStatus()), e.getError().getMessage()));
            resp.setStatus(e.getError().getStatus());
        }

        try (var writer = resp.getWriter()) {
            writer.write(responseJson);
        }
    }
}
