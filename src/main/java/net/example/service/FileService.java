package net.example.service;

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

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class FileService implements CrudService<FileCreateDto, FileInfoDto, Long> {

    private final FileRepositoryImpl fileRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    private final FileCreateMapper fileCreateMapper;
    private final FileReadMapper fileReadMapper;
    private final FileInfoDtoMapper fileInfoDtoMapper;

    public List<FileInfoDto> findAll() {
        return fileRepositoryImpl.findAll().stream()
            .map(fileInfoDtoMapper::mapFrom)
            .toList();
    }

    public Optional<FileInfoDto> findById(Long id) {
        return fileRepositoryImpl.findById(id)
            .map(fileInfoDtoMapper::mapFrom);
    }

    public Optional<FileInfoDto> findById(Long id, EventType eventType) {
        return fileRepositoryImpl.findById(id)
            .map(fileInfoDtoMapper::mapFrom);
    }

    public FileInfoDto create(FileCreateDto dto) {
        return fileInfoDtoMapper.mapFrom(fileRepositoryImpl.create(fileCreateMapper.mapFrom(dto)));
    }

    public FileInfoDto update(FileInfoDto fileInfoDto) {
        var event = fileRepositoryImpl.findById(fileInfoDto.getId())
            .orElseThrow(NotFoundException::new);

        event.setName(fileInfoDto.getName());
        event.setExtension(fileInfoDto.getExtension());
        event.setUser(userRepositoryImpl.findById(fileInfoDto.getUserId())
            .orElseThrow(NotFoundException::new));

        return fileInfoDtoMapper.mapFrom(fileRepositoryImpl.update(event));
    }

    public FileInfoDto update(FileReadDto dto) throws NotFoundException {
        var event = fileRepositoryImpl.findById(dto.getId())
            .orElseThrow(NotFoundException::new);

        event.setContent(dto.getContent());
        event.setName(dto.getName());
        event.setExtension(dto.getExtension());
        event.setUser(userRepositoryImpl.findById(dto.getUserId())
            .orElseThrow(NotFoundException::new));

        return fileInfoDtoMapper.mapFrom(fileRepositoryImpl.update(event));
    }

    public void deleteById(Long id) throws NotFoundException {
        fileRepositoryImpl.findById(id).ifPresentOrElse(
            fileRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });
    }

    public List<FileInfoDto> findAllByUserId(Long id) {
        return fileRepositoryImpl.findAllByUserId(id).stream()
            .map(fileInfoDtoMapper::mapFrom)
            .toList();
    }

    public Optional<FileReadDto> downloadById(Long id) {
        return fileRepositoryImpl.findById(id)
            .map(fileReadMapper::mapFrom);
    }
}
