package ru.toporkov.servlet.currency;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import ru.toporkov.util.UrlPath;

@WebServlet(UrlPath.CURRENCY + "/*")
public class CurrencyServlet extends HttpServlet {
}
