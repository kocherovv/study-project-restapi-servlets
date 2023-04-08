package net.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.domain.enums.EventType;
import net.example.dto.EventReadDto;
import net.example.dto.FileCreateDto;
import net.example.dto.FileReadDto;
import net.example.dto.mapper.EventReadMapper;
import net.example.dto.mapper.FileCreateMapper;
import net.example.dto.mapper.FileReadMapper;
import net.example.exception.NotFoundException;
import net.example.model.AppStatusCode;
import net.example.repository.impl.EventRepositoryImpl;
import net.example.repository.impl.FileRepositoryImpl;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class FileService implements CrudService<FileCreateDto, FileReadDto, Long> {

    private final FileRepositoryImpl fileRepositoryImpl;
    private final EventRepositoryImpl eventRepositoryImpl;

    private final FileCreateMapper fileCreateMapper;
    private final FileReadMapper fileReadMapper;
    private final EventReadMapper eventReadMapper;

    public List<FileReadDto> findAll() {
        return fileRepositoryImpl.findAll().stream()
            .map(fileReadMapper::mapFrom)
            .toList();
    }

    public Optional<FileReadDto> findById(Long id) {
        return fileRepositoryImpl.findById(id)
            .map(fileReadMapper::mapFrom);
    }

    public Optional<FileReadDto> findById(Long id, EventType eventType) {
        return fileRepositoryImpl.findById(id)
            .map(fileReadMapper::mapFrom);
    }

    public FileReadDto create(FileCreateDto dto) {
        return fileReadMapper.mapFrom(fileRepositoryImpl.create(fileCreateMapper.mapFrom(dto)));
    }

    public FileReadDto update(FileReadDto dto) throws NotFoundException {
        var event = fileRepositoryImpl.findById(dto.getId())
            .orElseThrow(NotFoundException::new);

        event.setContent(dto.getContent());
        event.setName(dto.getName());
        event.setExtension(dto.getExtension());
        event.setEvents(dto.getEvents().stream()
            .map(EventReadDto::getId)
            .map(id -> eventRepositoryImpl.findById(id)
                .orElseThrow(NotFoundException::new))
            .toList());

        return fileReadMapper.mapFrom(fileRepositoryImpl.update(event));
    }

    public void deleteById(Long id) throws NotFoundException {
        fileRepositoryImpl.findById(id).ifPresentOrElse(
            fileRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });
    }

    public List<FileReadDto> findAllByUserId(Long id) {
        return fileRepositoryImpl.findAllByUserId(id).stream()
            .map(fileReadMapper::mapFrom)
            .toList();
    }
}
