package net.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import net.example.database.repository.impl.EventRepositoryImpl;
import net.example.database.repository.impl.FileRepositoryImpl;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.mapper.EventReadMapper;
import net.example.mapper.FileInfoDtoMapper;
import net.example.mapper.FileReadMapper;
import net.example.mapper.UserReadMapper;
import net.example.service.EventService;
import net.example.service.FileService;
import net.example.service.UserService;

@Getter
public class AppContainer {

    private static final AppContainer instance = new AppContainer();

    private final UserRepositoryImpl userRepository;
    private final EventRepositoryImpl eventRepository;
    private final FileRepositoryImpl fileRepository;

    private final UserReadMapper userReadMapper;
    private final FileReadMapper fileReadMapper;
    private final EventReadMapper eventReadMapper;
    private final FileInfoDtoMapper fileInfoDtoMapper;

    private final ObjectMapper jsonMapper;

    private final UserService userService;
    private final FileService fileService;
    private final EventService eventService;

    private AppContainer() {

        var session = HibernateUtil.getProxySession();

        userRepository = new UserRepositoryImpl(session);
        fileRepository = new FileRepositoryImpl(session);
        eventRepository = new EventRepositoryImpl(session);

        eventReadMapper = new EventReadMapper();
        userReadMapper = new UserReadMapper();
        fileReadMapper = new FileReadMapper();
        fileInfoDtoMapper = new FileInfoDtoMapper();

        jsonMapper = new ObjectMapper();
        jsonMapper.findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        userService = new UserService(session, userRepository,
            userReadMapper);
        fileService = new FileService(session, fileRepository, userRepository,
            fileReadMapper, fileInfoDtoMapper);
        eventService = new EventService(session, eventRepository, userRepository, eventReadMapper);
    }

    public static AppContainer getInstance() {
        return instance;
    }
}
