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
import ru.toporkov.validator.ErrorMessage;
import ru.toporkov.validator.exception.ApplicationException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static ru.toporkov.mapper.CurrencyMapper.mapStringToCreateCurrencyDTO;
import static ru.toporkov.validator.ErrorMessage.*;

@WebServlet(UrlPath.CURRENCIES)
public class CurrenciesServlet extends HttpServlet {

    private static final CurrencyService currencyService = CurrencyService.getInstance();
//    TODO: Заменить на jackson
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> allCurrencies;
        try (var writer = resp.getWriter()) {
            allCurrencies = currencyService.findAll();
            var currenciesJson = gson.toJson(allCurrencies);

            writer.write(currenciesJson);
        } catch (SQLException e) {
            resp.sendError(DATABASE_UNAVAILABLE.getStatus(), DATABASE_UNAVAILABLE.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var reader = req.getReader()) {
            var line = reader.readLine();
            var currencyDTO = mapStringToCreateCurrencyDTO(line);
            Currency entity;

            try (var writer = resp.getWriter()) {
                entity = currencyService.saveCurrency(currencyDTO);
                var currencyJson = gson.toJson(entity);

                writer.write(currencyJson);
            } catch (SQLException e) {
                resp.sendError(DATABASE_UNAVAILABLE.getStatus(), DATABASE_UNAVAILABLE.getMessage());
            } catch (ApplicationException e) {
                resp.sendError(e.getError().getStatus(), e.getError().getMessage());
            }
        }
    }
}
