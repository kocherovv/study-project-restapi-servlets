package net.example.controller.jstlAndParam;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.example.dto.UserCreateDto;
import net.example.util.AppContainer;
import net.example.util.PasswordHasher;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var username = req.getParameter("username");
        var email = req.getParameter("email");
        var password = PasswordHasher.hashPassword(req.getParameter("password").getBytes());

        var userDto = AppContainer.getInstance().getUserService().create(UserCreateDto.builder()
            .name(username)
            .email(email)
            .password(password)
            .build());

        req.getSession().setAttribute("user", userDto);
        resp.sendRedirect(getServletContext().getContextPath() + "/files");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/JSP/registration.jsp")
            .forward(req, resp);
    }
}
