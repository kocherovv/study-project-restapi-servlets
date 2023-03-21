package net.example.util;

import jakarta.persistence.EntityManager;
import net.example.repository.impl.EventRepositoryImpl;
import net.example.repository.impl.FileRepositoryImpl;
import net.example.repository.impl.UserRepositoryImpl;

public abstract class AppContainer {

    private EntityManager entityManager;

    private UserRepositoryImpl userRepository;
    private EventRepositoryImpl eventRepository;
    private FileRepositoryImpl fileRepository;
}
