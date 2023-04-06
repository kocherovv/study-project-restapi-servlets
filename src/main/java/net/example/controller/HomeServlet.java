package net.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var cookies = Arrays.stream(req.getCookies())
            .filter(cookie -> cookie.getName().equals("userId")).toList();

        if (cookies.isEmpty() || cookies.get(0) == null) {
            req.getRequestDispatcher("/WEB-INF/JSP/authorization.jsp")
                .forward(req, resp);
        }
    }
}
