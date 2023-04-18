package net.example.controller.jsonAndPathSegments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import net.example.domain.enums.EventType;
import net.example.dto.*;
import net.example.service.EventService;
import net.example.service.FileService;
import net.example.service.UserService;
import net.example.util.AppContainer;
import net.example.util.PasswordHasher;

import java.io.IOException;
import java.io.NotActiveException;

@MultipartConfig
@WebServlet(urlPatterns = "/user/*")
public class UserRestController extends HttpServlet {

    private final FileService fileService = AppContainer.getInstance().getFileService();
    private final UserService userService = AppContainer.getInstance().getUserService();
    private final EventService eventService = AppContainer.getInstance().getEventService();
    private final ObjectMapper jsonMapper = AppContainer.getInstance().getJsonMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 3 && pathSegments[2].equals("user")) {
            var allUsers = userService.findAll();
            resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));

        } else if (pathSegments.length == 4 && pathSegments[2].equals("user")) {
            try {
                var userId = Long.valueOf(pathSegments[3]);
                var user = userService.findById(userId).orElseThrow(NotActiveException::new);

                resp.getWriter().write(jsonMapper.writeValueAsString(user));
            } catch (Exception e) {
                resp.sendError(404);
            }

        } else if (pathSegments.length == 5 && pathSegments[2].equals("user") && pathSegments[4].equals("files")) {
            var userId = Long.valueOf(pathSegments[3]);

            var allUserFiles = fileService.findAllByUserId(userId);
            resp.getWriter().println(jsonMapper.writeValueAsString(allUserFiles));

        } else if (pathSegments.length == 5 && pathSegments[2].equals("user") && pathSegments[4].equals("events")) {
            var userId = Long.valueOf(pathSegments[3]);

            var allUserFiles = eventService.findAllByUserId(userId);
            resp.getWriter().println(jsonMapper.writeValueAsString(allUserFiles));

        } else if (pathSegments.length == 6 && pathSegments[2].equals("user") && pathSegments[4].equals("files")) {
            var fileId = Long.valueOf(pathSegments[5]);

            var file = fileService.findById(fileId).orElseThrow(NotActiveException::new);
            resp.getWriter().println(jsonMapper.writeValueAsString(file));

        } else if (pathSegments.length == 7 && pathSegments[2].equals("user") && pathSegments[4].equals("files")
            && pathSegments[6].equals("download")) {
            var fileId = Long.valueOf(pathSegments[5]);
            var userId = Long.valueOf(pathSegments[3]);

            fileService.downloadById(fileId)
                .ifPresent(
                    file -> {
                        try {
                            eventService.create(fileId, userId, EventType.DOWNLOAD);
                        } catch (JsonProcessingException e) {
                            updatePage(resp);
                        }

                        resp.setContentType("application/octet-stream");
                        resp.setContentLength(file.getContent().length);
                        resp.setHeader("Content-Disposition",
                            "attachment; filename=" + file.getName() + "." + file.getExtension());

                        writeFileOutputStream(resp, file);
                    });

        } else if (pathSegments.length == 6 && pathSegments[2].equals("user") && pathSegments[4].equals("events")) {
            var eventId = Long.valueOf(pathSegments[5]);

            var event = eventService.findById(eventId).orElseThrow(NotActiveException::new);
            resp.getWriter().println(jsonMapper.writeValueAsString(event));

        } else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 3 && pathSegments[2].equals("user")) {
            var username = req.getParameter("username");
            var email = req.getParameter("email");
            byte[] password = PasswordHasher.hashPassword(req.getParameter("password").getBytes());

            var userDto = userService.create(UserCreateDto.builder()
                .name(username)
                .email(email)
                .password(password)
                .build());

            resp.getWriter().write(jsonMapper.writeValueAsString(userDto));

        } else if (pathSegments.length == 5 && pathSegments[2].equals("user") && pathSegments[4].equals("files")) {
            var userId = Long.valueOf(pathSegments[3]);
            var filePart = req.getPart("uploadFile");
            var splitName = filePart.getSubmittedFileName().split("\\.");

            var fileContent = filePart.getInputStream().readAllBytes();

            var uploadFile = fileService.create(FileCreateDto.builder()
                .userId(userId)
                .content(fileContent)
                .name(splitName[0])
                .extension(splitName[1])
                .build());

            eventService.create(uploadFile.getId(), userId, EventType.UPLOAD);

            resp.getWriter().println(jsonMapper.writeValueAsString(uploadFile));

        } else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 4 && pathSegments[2].equals("user")) {
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

        } else if (pathSegments.length == 6 && pathSegments[2].equals("user") && "files".equals(pathSegments[4])) {
            var fileId = Long.valueOf(pathSegments[5]);
            var userId = Long.valueOf(pathSegments[3]);
            var name = req.getParameter("fileName");
            var extension = req.getParameter("extension");
            var content = req.getPart("content").getInputStream().readAllBytes();

            var file = fileService.update(
                FileReadDto.builder()
                    .id(fileId)
                    .name(name)
                    .extension(extension)
                    .content(content)
                    .build());

            eventService.create(fileId, userId, EventType.UPDATE);

            resp.getWriter().println(jsonMapper.writeValueAsString(file));

        } else if (pathSegments.length == 6 && pathSegments[2].equals("user") && "events".equals(pathSegments[4])) {
            var eventId = Long.valueOf(pathSegments[5]);
            var userId = Long.valueOf(pathSegments[3]);
            var eventType = EventType.valueOf(req.getParameter("eventType"));

            var event = eventService.update(
                EventReadDto.builder()
                    .id(eventId)
                    .userId(userId)
                    .eventType(eventType)
                    .build());

            resp.getWriter().println(jsonMapper.writeValueAsString(event));

        } else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 4 && pathSegments[2].equals("user")) {
            var userId = Long.valueOf(pathSegments[3]);

            userService.deleteById(userId);

        } else if (pathSegments.length == 6 && pathSegments[2].equals("user") && "files".equals(pathSegments[4])) {
            var fileId = Long.valueOf(pathSegments[5]);
            var userId = Long.valueOf(pathSegments[3]);

            eventService.create(fileId, userId, EventType.DELETE);

            fileService.deleteById(fileId);

        } else if (pathSegments.length == 6 && pathSegments[2].equals("user") && "events".equals(pathSegments[4])) {
            var eventId = Long.valueOf(pathSegments[5]);

            eventService.deleteById(eventId);

        } else {
            resp.sendError(404);
        }
    }

    @SneakyThrows
    private void updatePage(HttpServletResponse resp) {
        resp.sendRedirect(getServletContext().getContextPath() + "/files");
    }

    @SneakyThrows
    private static void writeFileOutputStream(HttpServletResponse resp, FileReadDto file) {
        resp.getOutputStream().write(file.getContent());
    }
}
