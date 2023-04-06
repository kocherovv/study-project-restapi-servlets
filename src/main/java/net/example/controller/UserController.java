package net.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.example.dto.UserCreateDto;
import net.example.dto.UserReadDto;
import net.example.util.AppContainer;
import net.example.util.HibernateUtil;

import java.io.IOException;

@WebServlet("/users")
public class UserController extends HttpServlet {

    private AppContainer appContainer;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var session = HibernateUtil.getProxySession();
        session.beginTransaction();

        appContainer = new AppContainer(session);

        response.setContentType("text/json");

        var userId = request.getParameter("user_id");

        if (userId == null) {
            session.getTransaction().commit();

            response.setStatus(400);
        } else {
            var user = appContainer.getJsonMapper().writeValueAsString(
                appContainer.getUserService().findById(Long.valueOf(userId))
                    .orElse(null));

            session.getTransaction().commit();

            response.setStatus(200);
            response.getWriter().println(user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = HibernateUtil.getProxySession();

        session.beginTransaction();

        appContainer = new AppContainer(session);

        resp.setContentType("text/json");

        var userName = req.getParameter("userName");
        var email = req.getParameter("userEmail");

        if (userName == null || email == null) {
            session.getTransaction().commit();
            resp.setStatus(400);
        } else {
            var userCreateDto = UserCreateDto.builder()
                .name(userName)
                .email(email)
                .build();

            var createdUser = appContainer.getJsonMapper().writeValueAsString(appContainer.getUserService().create(userCreateDto));

            session.getTransaction().commit();

            resp.setStatus(201);
            resp.getWriter().println(createdUser);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = HibernateUtil.getProxySession();
        session.beginTransaction();

        appContainer = new AppContainer(session);

        resp.setContentType("text/json");

        var userId = req.getParameter("user_id");
        var userName = req.getParameter("userName");
        var email = req.getParameter("userEmail");

        if (userId == null) {
            session.getTransaction().commit();

            resp.setStatus(400);
        } else {
            var user = appContainer.getJsonMapper().writeValueAsString(
                appContainer.getUserService()
                    .update(UserReadDto.builder()
                        .id(Long.valueOf(userId))
                        .name(userName)
                        .email(email)
                        .build()));

            session.getTransaction().commit();

            resp.setStatus(200);
            resp.getWriter().println(user);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = HibernateUtil.getProxySession();
        session.beginTransaction();

        appContainer = new AppContainer(session);

        resp.setContentType("text/json");

        var userId = req.getParameter("user_id");

        if (userId == null) {
            session.getTransaction().commit();

            resp.setStatus(400);
        } else {
            appContainer.getUserService().deleteById(Long.valueOf(userId));

            session.getTransaction().commit();

            resp.setStatus(200);
        }
    }
}
