package net.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import net.example.dto.UserReadDto;
import net.example.util.AppContainer;
import net.example.util.PasswordHasher;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/JSP/login.jsp")
            .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AppContainer.getInstance().getUserService()
            .findByUserNameAndPassword(req.getParameter("username"),
                PasswordHasher.hashPassword(req.getParameter("password").getBytes()))
            .ifPresentOrElse(
                user -> onLoginSuccess(req, resp, user),
                () -> onLoginFail(req, resp)
            );
    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    @SneakyThrows
    private void onLoginSuccess(HttpServletRequest req, HttpServletResponse resp, UserReadDto user) {
        req.getSession().setAttribute("user", user);
        resp.sendRedirect(req.getContextPath()+"/files");
    }
}