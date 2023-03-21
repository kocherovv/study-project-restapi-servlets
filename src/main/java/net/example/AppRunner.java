package net.example;

import net.example.dto.mapper.*;
import net.example.repository.impl.EventRepositoryImpl;
import net.example.service.EventService;
import net.example.repository.impl.FileRepositoryImpl;
import net.example.repository.impl.UserRepositoryImpl;
import net.example.service.FileService;
import net.example.service.UserService;
import net.example.util.HibernateUtil;

public class AppRunner {
    public static void main(String[] args) {
        var session = HibernateUtil.getProxySession();

        session.beginTransaction();

        var userRepository = new UserRepositoryImpl(session);
        var fileRepository = new FileRepositoryImpl(session);
        var eventRepository = new EventRepositoryImpl(session);

        var eventReadMapper = new EventReadMapper();
        var eventCreateMapper = new EventCreateMapper();
        var userCreateMapper = new UserCreateMapper();
        var userReadMapper = new UserReadMapper(eventReadMapper);
        var fileCreateMapper = new FileCreateMapper(eventRepository);
        var fileReadMapper = new FileReadMapper(eventReadMapper);

        var userService = new UserService(userRepository, eventRepository, userCreateMapper,
            userReadMapper, eventReadMapper);
        var fileService = new FileService(fileRepository, eventRepository, fileCreateMapper,
            fileReadMapper, eventReadMapper);
        var eventService = new EventService(eventRepository, eventCreateMapper, eventReadMapper);


        session.getTransaction().commit();
    }
}