package net.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.example.util.AppContainer;
import net.example.util.HibernateUtil;

import java.io.IOException;

@WebServlet("/authorization")
public class AuthenticationController extends HttpServlet {

    private AppContainer appContainer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/JSP/authorization.jsp")
            .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = HibernateUtil.getProxySession();
        session.beginTransaction();

        appContainer = new AppContainer(session);

        resp.setContentType("text/json");

        var userName = req.getParameter("userName");
        var password = req.getParameter("password");

        if (userName == null || password == null) {
            session.getTransaction().commit();

            resp.setStatus(400);
        } else {
            var user = appContainer.getJsonMapper().writeValueAsString(
                appContainer.getUserService().userAuthentication(userName, password.getBytes()));

            session.getTransaction().commit();

            resp.setStatus(200);
            resp.sendRedirect("/files");
        }
    }
}
