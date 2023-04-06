package net.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.example.dto.mapper.*;
import net.example.repository.impl.EventRepositoryImpl;
import net.example.repository.impl.FileRepositoryImpl;
import net.example.repository.impl.UserRepositoryImpl;
import net.example.service.CrudService;
import net.example.service.EventService;
import net.example.service.FileService;
import net.example.service.UserService;
import org.hibernate.Session;

@Getter
public class AppContainer {

    private final UserRepositoryImpl userRepository;
    private final EventRepositoryImpl eventRepository;
    private final FileRepositoryImpl fileRepository;

    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;
    private final FileCreateMapper fileCreateMapper;
    private final FileReadMapper fileReadMapper;
    private final EventCreateMapper eventCreateMapper;
    private final EventReadMapper eventReadMapper;

    private final ObjectMapper jsonMapper;

    private final UserService userService;
    private final FileService fileService;
    private final EventService eventService;

    public AppContainer(Session session) {

        userRepository = new UserRepositoryImpl(session);
        fileRepository = new FileRepositoryImpl(session);
        eventRepository = new EventRepositoryImpl(session);

        eventReadMapper = new EventReadMapper();
        eventCreateMapper = new EventCreateMapper();
        userCreateMapper = new UserCreateMapper();
        userReadMapper = new UserReadMapper(eventReadMapper);
        fileCreateMapper = new FileCreateMapper(eventRepository);
        fileReadMapper = new FileReadMapper(eventReadMapper);

        jsonMapper = new ObjectMapper();

        userService = new UserService(userRepository, eventRepository, userCreateMapper,
            userReadMapper, eventReadMapper);
        fileService = new FileService(fileRepository, eventRepository, fileCreateMapper,
            fileReadMapper, eventReadMapper);
        eventService = new EventService(eventRepository, eventCreateMapper, eventReadMapper);
    }
}
