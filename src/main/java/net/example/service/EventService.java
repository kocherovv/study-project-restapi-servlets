package net.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.EventRepositoryImpl;
import net.example.database.repository.impl.FileRepositoryImpl;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.domain.entity.Event;
import net.example.domain.enums.EventType;
import net.example.dto.EventCreateDto;
import net.example.dto.EventReadDto;
import net.example.dto.mapper.EventCreateMapper;
import net.example.dto.mapper.EventReadMapper;
import net.example.dto.mapper.FileInfoDtoMapper;
import net.example.exception.NotFoundException;
import net.example.model.AppStatusCode;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class EventService implements CrudService<Event, EventReadDto> {

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

    public Optional<EventReadDto> findById(Event event) {
        entityManager.getTransaction().begin();

        var entity = eventRepositoryImpl.findById(event.getId())
            .map(eventReadMapper::mapFrom);

        entityManager.getTransaction().commit();

        return entity;
    }

    public EventReadDto create(Event event) {
        entityManager.getTransaction().begin();

        var newEvent = eventReadMapper.mapFrom(eventRepositoryImpl.create(event));

        entityManager.getTransaction().commit();

        return newEvent;
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

        var newEvent = eventReadMapper.mapFrom(eventRepositoryImpl.create(eventCreateMapper.mapFrom(dto)));

        entityManager.getTransaction().commit();

        return newEvent;
    }

    public EventReadDto update(Event event) {
        entityManager.getTransaction().begin();

        var Entity = eventRepositoryImpl.findById(event.getId())
            .orElseThrow(NotFoundException::new);

        Entity.setEventType(event.getEventType());
        Entity.setUser(userRepositoryImpl.findById(event.getUser().getId())
            .orElseThrow(NotFoundException::new));

        var updatedEvent = eventReadMapper.mapFrom(eventRepositoryImpl.update(event));

        entityManager.getTransaction().commit();

        return updatedEvent;
    }

    public void deleteById(Event event) {
        entityManager.getTransaction().begin();

        eventRepositoryImpl.findById(event.getId()).ifPresentOrElse(
            eventRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });

        entityManager.getTransaction().commit();
    }

    public List<EventReadDto> findAllByUserId(Event event) {
        entityManager.getTransaction().begin();

        var events = eventRepositoryImpl.findAllByUserId(event.getUser().getId()).stream()
            .map(eventReadMapper::mapFrom).toList();

        entityManager.getTransaction().commit();

        return events;
    }
}
