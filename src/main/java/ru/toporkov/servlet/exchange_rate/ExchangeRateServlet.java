package ru.toporkov.servlet.exchange_rate;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.toporkov.service.ExchangeRateService;
import ru.toporkov.util.UrlPath;
import ru.toporkov.validator.Error;
import ru.toporkov.validator.exception.ApplicationException;

import java.io.IOException;

@WebServlet(UrlPath.EXCHANGE_RATE + "/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var uriParts = req.getRequestURI().split("/");
        var exchangeRate = uriParts[uriParts.length - 1];
        String responseJson;

        try {
            var byIsoCode = exchangeRateService.findByIsoCode(exchangeRate);
            responseJson = gson.toJson(byIsoCode);
        } catch (ApplicationException e) {
            responseJson = gson.toJson(Error.of(String.valueOf(e.getError().getStatus()), e.getError().getMessage()));
            resp.setStatus(e.getError().getStatus());
        }

        try (var writer = resp.getWriter()) {
            writer.write(responseJson);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
