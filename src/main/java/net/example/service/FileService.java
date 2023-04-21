package net.example.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.FileRepositoryImpl;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.domain.enums.EventType;
import net.example.dto.FileCreateDto;
import net.example.dto.FileInfoDto;
import net.example.dto.FileReadDto;
import net.example.dto.mapper.FileCreateMapper;
import net.example.dto.mapper.FileInfoDtoMapper;
import net.example.dto.mapper.FileReadMapper;
import net.example.exception.NotFoundException;
import net.example.model.AppStatusCode;
import net.example.util.AppContainer;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class FileService implements CrudService<FileCreateDto, FileInfoDto, Long> {

    private final EntityManager entityManager;

    private final FileRepositoryImpl fileRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    private final FileCreateMapper fileCreateMapper;
    private final FileReadMapper fileReadMapper;
    private final FileInfoDtoMapper fileInfoDtoMapper;

    public List<FileInfoDto> findAll() {
        entityManager.getTransaction().begin();

        var files = fileRepositoryImpl.findAll().stream()
            .map(fileInfoDtoMapper::mapFrom)
            .toList();

        entityManager.getTransaction().commit();

        return files;
    }

    public Optional<FileInfoDto> findById(Long id) {
        entityManager.getTransaction().begin();

        var file = fileRepositoryImpl.findById(id)
            .map(fileInfoDtoMapper::mapFrom);

        entityManager.getTransaction().commit();

        return file;
    }

    public Optional<FileInfoDto> findById(Long id, EventType eventType) {
        entityManager.getTransaction().begin();

        var file = fileRepositoryImpl.findById(id)
            .map(fileInfoDtoMapper::mapFrom);

        entityManager.getTransaction().commit();

        return file;
    }

    public FileInfoDto create(FileCreateDto dto) {
        entityManager.getTransaction().begin();

        var file = fileInfoDtoMapper.mapFrom(fileRepositoryImpl.create(fileCreateMapper.mapFrom(dto)));

        entityManager.getTransaction().commit();

        return file;
    }

    public FileInfoDto update(FileInfoDto fileInfoDto) {
        entityManager.getTransaction().begin();

        var event = fileRepositoryImpl.findById(fileInfoDto.getId())
            .orElseThrow(NotFoundException::new);

        event.setName(fileInfoDto.getName());
        event.setExtension(fileInfoDto.getExtension());
        event.setUser(userRepositoryImpl.findById(fileInfoDto.getUserId())
            .orElseThrow(NotFoundException::new));

        var file = fileInfoDtoMapper.mapFrom(fileRepositoryImpl.update(event));

        entityManager.getTransaction().commit();

        return file;
    }

    public FileInfoDto update(FileReadDto dto) throws NotFoundException {
        entityManager.getTransaction().begin();

        var event = fileRepositoryImpl.findById(dto.getId())
            .orElseThrow(NotFoundException::new);

        event.setContent(dto.getContent());
        event.setName(dto.getName());
        event.setExtension(dto.getExtension());
        event.setUser(userRepositoryImpl.findById(dto.getUserId())
            .orElseThrow(NotFoundException::new));

        var file = fileInfoDtoMapper.mapFrom(fileRepositoryImpl.update(event));

        entityManager.getTransaction().commit();

        return file;
    }

    public void deleteById(Long id) throws NotFoundException {
        entityManager.getTransaction().begin();

        fileRepositoryImpl.findById(id).ifPresentOrElse(
            fileRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });

        entityManager.getTransaction().commit();
    }

    public List<FileInfoDto> findAllByUserId(Long id) {
        entityManager.getTransaction().begin();

        var files = fileRepositoryImpl.findAllByUserId(id).stream()
            .map(fileInfoDtoMapper::mapFrom)
            .toList();

        entityManager.getTransaction().commit();

        return files;
    }

    public Optional<FileReadDto> downloadById(Long id) {
        entityManager.getTransaction().begin();

        var fileReadDto = fileRepositoryImpl.findById(id)
            .map(fileReadMapper::mapFrom);

        entityManager.getTransaction().commit();

        return fileReadDto;
    }
}
