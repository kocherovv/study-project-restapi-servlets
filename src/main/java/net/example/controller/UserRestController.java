package net.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.example.dto.UserCreateDto;
import net.example.dto.UserReadDto;
import net.example.service.UserService;
import net.example.util.AppContainer;
import net.example.util.PasswordHasher;

import java.io.IOException;
import java.io.NotActiveException;

@WebServlet(urlPatterns = {"/users", "/users/*"})
public class UserRestController extends HttpServlet {
    private final UserService userService = AppContainer.getInstance().getUserService();
    private final ObjectMapper jsonMapper = AppContainer.getInstance().getJsonMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 3 && pathSegments[2].equals("users")) {
            var allUsers = userService.findAll();
            resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));

        } else if (pathSegments.length == 4 && pathSegments[2].equals("users")) {
            try {
                var userId = Long.valueOf(pathSegments[3]);
                var user = userService.findById(userId).orElseThrow(NotActiveException::new);

                resp.getWriter().write(jsonMapper.writeValueAsString(user));
            } catch (Exception e) {
                resp.sendError(404);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 3 && pathSegments[2].equals("users")) {
            var username = req.getParameter("username");
            var email = req.getParameter("email");
            byte[] password = PasswordHasher.hashPassword(req.getParameter("password").getBytes());

            var userDto = userService.create(UserCreateDto.builder()
                .name(username)
                .email(email)
                .password(password)
                .build());

            resp.getWriter().write(jsonMapper.writeValueAsString(userDto));

        } else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 4 && pathSegments[2].equals("users")) {
            var userId = Long.valueOf(pathSegments[3]);
            var name = req.getParameter("userName");
            var email = req.getParameter("email");

            var user = userService.update(
                UserReadDto.builder()
                    .id(userId)
                    .name(name)
                    .email(email)
                    .build());

            resp.getWriter().write(jsonMapper.writeValueAsString(user));

        } else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 4 && pathSegments[2].equals("users")) {
            var userId = Long.valueOf(pathSegments[3]);

            userService.deleteById(userId);

        } else {
            resp.sendError(404);
        }
    }
}
