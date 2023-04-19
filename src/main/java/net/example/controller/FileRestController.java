package net.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import net.example.domain.enums.EventType;
import net.example.dto.FileCreateDto;
import net.example.dto.FileReadDto;
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

                var file = fileService.findById(fileId).orElseThrow(NotActiveException::new);

                resp.getWriter().write(jsonMapper.writeValueAsString(file));
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if (pathSegments.length == 5 && pathSegments[2].equals("file") && pathSegments[4].equals("download")) {
            try {
                var fileId = Long.valueOf(pathSegments[3]);

                var downloadFile = fileService.downloadById(fileId).orElseThrow(NotActiveException::new);

                eventService.create(fileId, Long.valueOf(userId), EventType.DOWNLOAD);

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

        if (pathSegments.length == 3 && pathSegments[2].equals("file")) {
            var userId = Long.valueOf(req.getParameter("userId"));
            var filePart = req.getPart("file");
            var splitName = filePart.getSubmittedFileName().split("\\.");

            var fileContent = filePart.getInputStream().readAllBytes();

            var createdFile = fileService.create(FileCreateDto.builder()
                .userId(userId)
                .content(fileContent)
                .name(splitName[0])
                .extension(splitName[1])
                .build());

            eventService.create(createdFile.getId(), userId, EventType.UPLOAD);

            resp.getWriter().println(jsonMapper.writeValueAsString(createdFile));
        } else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 4 && pathSegments[2].equals("file")) {
            var userId = Long.valueOf(req.getParameter("userId"));
            var fileId = Long.valueOf(pathSegments[3]);
            var name = req.getParameter("fileName");
            var extension = req.getParameter("extension");
            var content = req.getPart("content").getInputStream().readAllBytes();

            var updatedFile = fileService.update(FileReadDto.builder()
                .id(fileId)
                .userId(userId)
                .content(content)
                .name(name)
                .extension(extension)
                .build());

            eventService.create(updatedFile.getId(), userId, EventType.UPDATE);

            resp.getWriter().println(jsonMapper.writeValueAsString(updatedFile));
        } else {
            resp.sendError(404);
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

            eventService.create(fileId, userId, EventType.DELETE);

            fileService.deleteById(fileId);
        } else {
            resp.sendError(400);
        }
    }

    @SneakyThrows
    private static void writeFileOutputStream(HttpServletResponse resp, FileReadDto file) {
        resp.getOutputStream().write(file.getContent());
    }

    @SneakyThrows
    private void updatePage(HttpServletResponse resp) {
        resp.sendRedirect(getServletContext().getContextPath() + "/files");
    }
}