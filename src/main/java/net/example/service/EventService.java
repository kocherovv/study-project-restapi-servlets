package net.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.EventRepositoryImpl;
import net.example.database.repository.impl.FileRepositoryImpl;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.domain.enums.EventType;
import net.example.dto.EventCreateDto;
import net.example.dto.EventReadDto;
import net.example.dto.mapper.EventCreateMapper;
import net.example.dto.mapper.EventReadMapper;
import net.example.dto.mapper.FileInfoDtoMapper;
import net.example.dto.mapper.FileReadMapper;
import net.example.exception.NotFoundException;
import net.example.model.AppStatusCode;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class EventService implements CrudService<EventCreateDto, EventReadDto, Long> {

    private final EntityManager entityManager;

    private final EventRepositoryImpl eventRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;
    private final FileRepositoryImpl fileRepositoryImpl;

    private final EventCreateMapper eventCreateMapper;
    private final EventReadMapper eventReadMapper;
    private final FileInfoDtoMapper fileInfoDtoMapper;

    private final ObjectMapper jsonMapper;

    public List<EventReadDto> findAll() {
        entityManager.getTransaction().begin();

        return eventRepositoryImpl.findAll().stream()
            .map(eventReadMapper::mapFrom)
            .toList();
    }

    public Optional<EventReadDto> findById(Long id) {
        entityManager.getTransaction().begin();

        return eventRepositoryImpl.findById(id)
            .map(eventReadMapper::mapFrom);
    }

    public EventReadDto create(EventCreateDto eventCreateDto) {
        entityManager.getTransaction().begin();

        return eventReadMapper.mapFrom(eventRepositoryImpl.create(eventCreateMapper.mapFrom(eventCreateDto)));
    }

    public EventReadDto create(Long fileId, Long userId, EventType eventType) throws JsonProcessingException {
        entityManager.getTransaction().begin();

        var dto = EventCreateDto.builder()
            .eventType(eventType)
            .userId(userId)
            .fileInfo(jsonMapper.writeValueAsString(
                fileInfoDtoMapper.mapFrom(fileRepositoryImpl.findById(fileId)
                    .orElseThrow(NotFoundException::new))))
            .build();

        return eventReadMapper.mapFrom(eventRepositoryImpl.create(eventCreateMapper.mapFrom(dto)));
    }

    public EventReadDto update(EventReadDto eventReadDto) {
        entityManager.getTransaction().begin();

        var event = eventRepositoryImpl.findById(eventReadDto.getId())
            .orElseThrow(NotFoundException::new);

        event.setEventType(eventReadDto.getEventType());
        event.setUser(userRepositoryImpl.findById(eventReadDto.getUserId()).orElseThrow(NotFoundException::new));

        return eventReadMapper.mapFrom(eventRepositoryImpl.update(event));
    }

    public void deleteById(Long id) {
        entityManager.getTransaction().begin();

        eventRepositoryImpl.findById(id).ifPresentOrElse(
            eventRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });
    }

    public List<EventReadDto> findAllByUserId(Long userId) {
        entityManager.getTransaction().begin();

        return eventRepositoryImpl.findAllByUserId(userId).stream()
            .map(eventReadMapper::mapFrom).toList();
    }
}
