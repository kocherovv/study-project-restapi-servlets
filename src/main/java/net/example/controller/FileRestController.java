package net.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import net.example.domain.entity.Event;
import net.example.domain.entity.File;
import net.example.domain.entity.User;
import net.example.domain.enums.EventType;
import net.example.dto.FileReadDto;
import net.example.exception.NotFoundException;
import net.example.service.EventService;
import net.example.service.FileService;
import net.example.util.AppContainer;

import java.io.IOException;
import java.io.NotActiveException;

@MultipartConfig
@WebServlet(urlPatterns = {"/files", "/files/*"})
public class FileRestController extends HttpServlet {

    private final FileService fileService = AppContainer.getInstance().getFileService();
    private final EventService eventService = AppContainer.getInstance().getEventService();
    private final ObjectMapper jsonMapper = AppContainer.getInstance().getJsonMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");
        var userId = req.getParameter("userId");

        if (pathSegments.length == 3 && pathSegments[2].equals("files") && userId == null) {
            var allFiles = fileService.findAll();
            resp.getWriter().write(jsonMapper.writeValueAsString(allFiles));

        } else if (pathSegments.length == 3 && pathSegments[2].equals("files")) {
            var allFiles = fileService.findAllByUserId(Long.valueOf(userId));
            resp.getWriter().write(jsonMapper.writeValueAsString(allFiles));

        } else if (pathSegments.length == 4 && pathSegments[2].equals("files")) {
            try {
                var fileId = Long.valueOf(pathSegments[3]);

                var file = fileService.findById(File.builder().id(fileId).build())
                    .orElseThrow(NotActiveException::new);

                resp.getWriter().write(jsonMapper.writeValueAsString(file));
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if (pathSegments.length == 5 && pathSegments[2].equals("files") &&
            pathSegments[4].equals("download")) {
            try {
                var fileId = Long.valueOf(pathSegments[3]);

                var downloadFile = fileService.downloadById(fileId).orElseThrow(NotFoundException::new);

                resp.setContentType("application/octet-stream");
                resp.setContentLength(downloadFile.getContent().length);
                resp.setHeader("Content-Disposition",
                    "attachment; filename=" + downloadFile.getName() + "." + downloadFile.getExtension());

                eventService.create(
                    buildEvent(Long.valueOf(userId), jsonMapper.writeValueAsString(downloadFile), EventType.DOWNLOAD));

                writeFileOutputStream(resp, downloadFile);
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

        if (pathSegments.length == 3 && pathSegments[2].equals("files")) {
            var userId = Long.valueOf(req.getParameter("userId"));
            var filePart = req.getPart("file");
            var splitName = filePart.getSubmittedFileName().split("\\.");

            var fileContent = filePart.getInputStream().readAllBytes();

            var createdFile = fileService.create(File.builder()
                .user(User.builder().id(userId).build())
                .content(fileContent)
                .name(splitName[0])
                .extension(splitName[1])
                .build());

            eventService.create(
                buildEvent(userId, jsonMapper.writeValueAsString(createdFile), EventType.UPLOAD));

            resp.getWriter().println(jsonMapper.writeValueAsString(createdFile));
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 4 && pathSegments[2].equals("files")) {
            var userId = Long.valueOf(req.getParameter("userId"));
            var reqBody = req.getInputStream().readAllBytes();

            var updatedFile = fileService.update(jsonMapper.readValue(reqBody, File.class));

            eventService.create(
                buildEvent(userId, jsonMapper.writeValueAsString(updatedFile), EventType.UPDATE));

            resp.getWriter().println(jsonMapper.writeValueAsString(updatedFile));
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 4 && pathSegments[2].equals("files")) {
            var userId = Long.valueOf(req.getParameter("userId"));
            var fileId = Long.valueOf(pathSegments[3]);

            eventService.create(
                buildEvent(
                    userId,
                    jsonMapper.writeValueAsString(
                        fileService.findById(
                            File.builder()
                                .id(fileId)
                                .build())),
                    EventType.DELETE));

            fileService.delete(File.builder().id(fileId).build());

            resp.sendError(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private Event buildEvent(Long userId, String fileInfo, EventType download) throws JsonProcessingException {
        return Event.builder()
            .user(User.builder().id(userId).build())
            .fileInfo(fileInfo)
            .eventType(download)
            .build();
    }

    @SneakyThrows
    private static void writeFileOutputStream(HttpServletResponse resp, FileReadDto file) {
        resp.getOutputStream().write(file.getContent());
    }
}