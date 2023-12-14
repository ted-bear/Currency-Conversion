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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(UrlPath.EXCHANGE_RATE + "/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var uriParts = req.getRequestURI().split("/");
        var exchangeRateCodes = uriParts[uriParts.length - 1];
        var newRate = BigDecimal.valueOf(Double.parseDouble(readRequestBody(req).getOrDefault("rate", "0.0")));
        String responseJson;

        try {
            var byIsoCode = exchangeRateService.updateExchangeRate(newRate, exchangeRateCodes);
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
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    private Map<String, String> readRequestBody(HttpServletRequest request) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                return bufferedReader
                        .lines()
                        .map((str) -> str.split("="))
                        .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
