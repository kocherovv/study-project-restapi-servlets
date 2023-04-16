package net.example.controller.jsonAndPathSegments;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.example.domain.enums.EventType;
import net.example.domain.enums.ReadOperationType;
import net.example.dto.FileCreateDto;
import net.example.dto.FileReadDto;
import net.example.dto.UserReadDto;
import net.example.service.EventService;
import net.example.service.FileService;
import net.example.service.UserService;
import net.example.util.AppContainer;

import java.io.IOException;

@WebServlet(name = "/files")
public class FileController extends HttpServlet {

    private final FileService fileService = AppContainer.getInstance().getFileService();
    private final UserService userService = AppContainer.getInstance().getUserService();
    private final EventService eventService = AppContainer.getInstance().getEventService();
    private final ObjectMapper jsonMapper = AppContainer.getInstance().getJsonMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = (UserReadDto) req.getSession().getAttribute("user");
        var writer = resp.getWriter();
        resp.setContentType("application/json");

        if (req.getParameter("operation") == null) {
            resp.sendError(400);
        }
        var operation = ReadOperationType.valueOf(req.getParameter("operation"));

        switch (operation) {
            case FIND_ALL -> {
                try {
                    var allByUserId = fileService.findAllByUserId(user.getId());
                    writer.write(jsonMapper.writeValueAsString(allByUserId));
                } catch (Exception e) {
                    resp.sendError(404);
                }
            }

            case FIND_BY_ID -> {
                var fileId = Long.valueOf(req.getParameter("fileId"));
                var file = jsonMapper.writeValueAsString(fileService.findById(fileId));
                writer.write(file);
            }

            case FIND_ALL_BY_USER -> {
                writer.write(jsonMapper.writeValueAsString(fileService.findAllByUserId(user.getId())));
            }

            case DOWNLOAD -> {
                var fileId = Long.valueOf(req.getParameter("fileId"));
                eventService.create(fileId, user.getId(), EventType.DOWNLOAD);
                var file = jsonMapper.writeValueAsString(fileService.findById(fileId));
                writer.write(file);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = (UserReadDto) req.getSession().getAttribute("user");
        var name = req.getParameter("name");
        var extension = req.getParameter("extension");
        var content = req.getPart("content").getInputStream().readAllBytes();
        var writer = resp.getWriter();

        var newFile = fileService.create(FileCreateDto.builder()
            .userId(user.getId())
            .name(name)
            .extension(extension)
            .content(content)
            .build());

        writer.write(jsonMapper.writeValueAsString(newFile));
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = (UserReadDto) req.getSession().getAttribute("user");
        var fileId = Long.valueOf(req.getParameter("fileId"));
        var name = req.getParameter("name");
        var extension = req.getParameter("extension");
        var content = req.getPart("content").getInputStream().readAllBytes();
        var writer = resp.getWriter();

        eventService.create(fileId, user.getId(), EventType.UPDATE);
        var updatedFile = fileService.update(FileReadDto.builder()
            .name(name)
            .extension(extension)
            .content(content)
            .build());

        writer.write(jsonMapper.writeValueAsString(updatedFile));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var fileId = Long.valueOf(req.getParameter("fileId"));
        var user = (UserReadDto) req.getSession().getAttribute("user");

        eventService.create(fileId, user.getId(), EventType.DELETE);
        fileService.deleteById(fileId);
    }
}
