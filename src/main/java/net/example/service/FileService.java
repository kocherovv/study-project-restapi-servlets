package net.example.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.FileRepositoryImpl;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.domain.entity.File;
import net.example.dto.FileInfoDto;
import net.example.dto.FileReadDto;
import net.example.exception.NotFoundException;
import net.example.mapper.FileInfoDtoMapper;
import net.example.mapper.FileReadMapper;
import net.example.model.AppStatusCode;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class FileService {

    private final EntityManager entityManager;

    private final FileRepositoryImpl fileRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    private final FileReadMapper fileReadMapper;
    private final FileInfoDtoMapper fileInfoDtoMapper;

    public List<FileInfoDto> findAll() {
        entityManager.getTransaction().begin();

        var filesDto = fileRepositoryImpl.findAll().stream()
            .map(fileInfoDtoMapper::mapFrom)
            .toList();

        entityManager.getTransaction().commit();

        return filesDto;
    }

    public Optional<FileInfoDto> findById(File file) {
        entityManager.getTransaction().begin();

        var fileDto = fileRepositoryImpl.findById(file.getId())
            .map(fileInfoDtoMapper::mapFrom);

        entityManager.getTransaction().commit();

        return fileDto;
    }

    public FileInfoDto create(File entity) {
        entityManager.getTransaction().begin();

        var fileDto = fileInfoDtoMapper.mapFrom(fileRepositoryImpl.create(entity));

        entityManager.getTransaction().commit();

        return fileDto;
    }

    public FileInfoDto update(File changedFile) throws NotFoundException {
        entityManager.getTransaction().begin();

        var entity = fileRepositoryImpl.findById(changedFile.getId())
            .orElseThrow(NotFoundException::new);

        updateEntityParameters(changedFile, entity);

        var fileDto = fileInfoDtoMapper.mapFrom(fileRepositoryImpl.update(entity));

        entityManager.getTransaction().commit();

        return fileDto;
    }

    public void delete(File entity) throws NotFoundException {
        entityManager.getTransaction().begin();

        fileRepositoryImpl.findById(entity.getId()).ifPresentOrElse(
            fileRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });

        entityManager.getTransaction().commit();
    }

    public List<FileInfoDto> findAllByUserId(Long userId) {
        entityManager.getTransaction().begin();

        var filesDto = fileRepositoryImpl.findAllByUserId(userId).stream()
            .map(fileInfoDtoMapper::mapFrom)
            .toList();

        entityManager.getTransaction().commit();

        return filesDto;
    }

    public Optional<FileReadDto> downloadById(Long id) {
        entityManager.getTransaction().begin();

        var fileDto = fileRepositoryImpl.findById(id)
            .map(fileReadMapper::mapFrom);

        entityManager.getTransaction().commit();

        return fileDto;
    }

    private void updateEntityParameters(File entity, File file) {
        file.setContent(entity.getContent());
        file.setName(entity.getName());
        file.setExtension(entity.getExtension());
        file.setUser(userRepositoryImpl.findById(entity.getUser().getId())
            .orElseThrow(NotFoundException::new));
    }
}
