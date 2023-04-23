package net.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.example.domain.entity.Event;
import net.example.service.EventService;
import net.example.service.FileService;
import net.example.util.AppContainer;

import java.io.IOException;
import java.io.NotActiveException;

@WebServlet(urlPatterns = {"/events", "/events/*"})
public class EventRestController extends HttpServlet {

    private final FileService fileService = AppContainer.getInstance().getFileService();
    private final EventService eventService = AppContainer.getInstance().getEventService();
    private final ObjectMapper jsonMapper = AppContainer.getInstance().getJsonMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");
        var userId = req.getParameter("userId");

        if (pathSegments.length == 3 && pathSegments[2].equals("events") && userId == null) {
            var allEvents = eventService.findAll();
            resp.getWriter().write(jsonMapper.writeValueAsString(allEvents));

        } else if (pathSegments.length == 3 && pathSegments[2].equals("events")) {
            var allEvents = eventService.findAllByUserId(
                Event.builder()
                    .id(Long.valueOf(userId))
                    .build());

            resp.getWriter().write(jsonMapper.writeValueAsString(allEvents));

        } else if (pathSegments.length == 4 && pathSegments[2].equals("events")) {
            try {
                var eventId = Long.valueOf(pathSegments[3]);
                var event = eventService.findById(
                    Event.builder()
                        .id(eventId)
                        .build())
                    .orElseThrow(NotActiveException::new);

                resp.getWriter().write(jsonMapper.writeValueAsString(event));
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 3 && pathSegments[2].equals("events")) {
            var reqBody = req.getInputStream().readAllBytes();

            var newEvent = eventService.create(jsonMapper.readValue(reqBody, Event.class));

            resp.getWriter().println(jsonMapper.writeValueAsString(newEvent));
        } else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 4 && pathSegments[2].equals("events")) {
            var reqBody = req.getInputStream().readAllBytes();

            var updatedEvent = eventService.update(jsonMapper.readValue(reqBody, Event.class));

            resp.getWriter().println(jsonMapper.writeValueAsString(updatedEvent));
        } else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 4 && pathSegments[2].equals("events")) {
            var eventId = Long.valueOf(pathSegments[3]);

            try {
                eventService.deleteById(
                    Event.builder()
                        .id(eventId)
                        .build());
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
}
