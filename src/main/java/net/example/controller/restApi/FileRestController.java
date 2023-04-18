package net.example.controller.restApi;

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
import net.example.dto.FileCreateDto;
import net.example.dto.FileReadDto;
import net.example.dto.UserReadDto;
import net.example.service.EventService;
import net.example.service.FileService;
import net.example.util.AppContainer;

import java.io.IOException;

@MultipartConfig
@WebServlet("/files*")
public class FileRestController extends HttpServlet {

    private final FileService fileService = AppContainer.getInstance().getFileService();
    private final EventService eventService = AppContainer.getInstance().getEventService();
    private final ObjectMapper jsonMapper = AppContainer.getInstance().getJsonMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = (UserReadDto) req.getSession().getAttribute("user");
        var pathInfo = req.getRequestURI();
        var pathSegments = pathInfo.split("/");

        if (pathSegments.length == 3 && pathSegments[2].equals("files")) {
            var allUsers = fileService.findAll();
            resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));
        } else if (pathSegments.length == 4 && pathSegments[2].equals("files")) {
            var fileId = Long.valueOf(pathSegments[3]);
        }
        var fileId = req.getParameter("fileId");


        resp.getWriter().println(jsonMapper.writeValueAsString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var event = EventType.valueOf(req.getParameter("event"));
        var user = (UserReadDto) req.getSession().getAttribute("user");

        switch (event) {
            case UPLOAD -> {
                var filePart = req.getPart("uploadFile");
                var splitName = filePart.getSubmittedFileName().split("\\.");

                var fileContent = filePart.getInputStream().readAllBytes();

                var uploadFile = fileService.create(FileCreateDto.builder()
                    .userId(user.getId())
                    .content(fileContent)
                    .name(splitName[0])
                    .extension(splitName[1])
                    .build());

                eventService.create(uploadFile.getId(), user.getId(), event);

                updatePage(resp);
            }

            case DOWNLOAD -> {
                var fileId = Long.valueOf(req.getParameter("file"));

                fileService.downloadById(fileId)
                    .ifPresent(
                        file -> {
                            try {
                                eventService.create(fileId, user.getId(), event);
                            } catch (JsonProcessingException e) {
                                updatePage(resp);
                            }

                            resp.setContentType("application/octet-stream");
                            resp.setContentLength(file.getContent().length);
                            resp.setHeader("Content-Disposition",
                                "attachment; filename=" + file.getName() + "." + file.getExtension());

                            writeFileOutputStream(resp, file);
                        });
            }

            case DELETE -> {
                var fileId = Long.valueOf(req.getParameter("file"));

                eventService.create(fileId, user.getId(), event);
                fileService.deleteById(fileId);

                updatePage(resp);
            }

            default -> {
                updatePage(resp);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
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