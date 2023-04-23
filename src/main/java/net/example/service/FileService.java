package net.example.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.FileRepositoryImpl;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.domain.entity.File;
import net.example.dto.FileInfoDto;
import net.example.dto.FileReadDto;
import net.example.dto.mapper.FileCreateMapper;
import net.example.dto.mapper.FileInfoDtoMapper;
import net.example.dto.mapper.FileReadMapper;
import net.example.exception.NotFoundException;
import net.example.model.AppStatusCode;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class FileService implements CrudService<File, FileInfoDto> {

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

    public Optional<FileInfoDto> findById(File file) {
        entityManager.getTransaction().begin();

        var entity = fileRepositoryImpl.findById(file.getId())
            .map(fileInfoDtoMapper::mapFrom);

        entityManager.getTransaction().commit();

        return entity;
    }

    public FileInfoDto create(File entity) {
        entityManager.getTransaction().begin();

        var file = fileInfoDtoMapper.mapFrom(fileRepositoryImpl.create(entity));

        entityManager.getTransaction().commit();

        return file;
    }

    public FileInfoDto update(File entity) throws NotFoundException {
        entityManager.getTransaction().begin();

        var file = fileRepositoryImpl.findById(entity.getId())
            .orElseThrow(NotFoundException::new);

        file.setContent(entity.getContent());
        file.setName(entity.getName());
        file.setExtension(entity.getExtension());
        file.setUser(userRepositoryImpl.findById(entity.getUser().getId())
            .orElseThrow(NotFoundException::new));

        var updatedFile = fileInfoDtoMapper.mapFrom(fileRepositoryImpl.update(file));

        entityManager.getTransaction().commit();

        return updatedFile;
    }

    public void deleteById(File entity) throws NotFoundException {
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

        var files = fileRepositoryImpl.findAllByUserId(userId).stream()
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
