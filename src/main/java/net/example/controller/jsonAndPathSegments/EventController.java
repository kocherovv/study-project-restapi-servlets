package net.example.controller.jsonAndPathSegments;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.example.domain.enums.ReadOperationType;
import net.example.dto.UserReadDto;
import net.example.service.EventService;
import net.example.util.AppContainer;

import java.io.IOException;

@WebServlet("/events-j/")
public class EventController extends HttpServlet {

    private final EventService eventService = AppContainer.getInstance().getEventService();
    private final ObjectMapper jsonMapper = AppContainer.getInstance().getJsonMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = (UserReadDto) req.getSession().getAttribute("user");
        var operation = ReadOperationType.valueOf(req.getParameter("operation"));
        var writer = resp.getWriter();
        resp.setContentType("application/json");

        switch (operation) {
            case FIND_ALL -> {
                var events = eventService.findAll();
                writer.write(jsonMapper.writeValueAsString(events));
            }

            case FIND_ALL_BY_USER -> {
                var events = eventService.findAllByUserId(user.getId());
                writer.write(jsonMapper.writeValueAsString(events));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
