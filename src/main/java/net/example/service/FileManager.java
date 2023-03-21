package net.example.service;

import lombok.RequiredArgsConstructor;
import net.example.dto.EventReadDto;
import net.example.dto.mapper.*;
import net.example.repository.impl.EventRepositoryImpl;
import net.example.repository.impl.FileRepositoryImpl;
import net.example.repository.impl.UserRepositoryImpl;

@RequiredArgsConstructor
public class FileManager {

    private final FileRepositoryImpl fileRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;
    private final EventRepositoryImpl eventRepositoryImpl;

    private final FileCreateMapper fileCreateMapper;
    private final FileReadMapper fileReadMapper;
    private final EventCreateMapper eventCreateMapper;
    private final EventReadMapper eventReadMapper;
    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;

    public EventReadDto uploadFile() {
        return null;
    }
}
