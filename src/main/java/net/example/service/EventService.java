package net.example.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.EventRepositoryImpl;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.domain.entity.Event;
import net.example.dto.EventReadDto;
import net.example.exception.NotFoundException;
import net.example.mapper.EventReadMapper;
import net.example.model.AppStatusCode;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class EventService implements CrudService<Event, EventReadDto> {

    private final EntityManager entityManager;

    private final EventRepositoryImpl eventRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    private final EventReadMapper eventReadMapper;

    public List<EventReadDto> findAll() {
        entityManager.getTransaction().begin();

        var eventsDto = eventRepositoryImpl.findAll().stream()
            .map(eventReadMapper::mapFrom).toList();

        entityManager.getTransaction().commit();

        return eventsDto;
    }

    public Optional<EventReadDto> findById(Event event) {
        entityManager.getTransaction().begin();

        var eventDto = eventRepositoryImpl.findById(event.getId())
            .map(eventReadMapper::mapFrom);

        entityManager.getTransaction().commit();

        return eventDto;
    }

    public EventReadDto create(Event event) {
        entityManager.getTransaction().begin();

        var eventDto = eventReadMapper.mapFrom(eventRepositoryImpl.create(event));

        entityManager.getTransaction().commit();

        return eventDto;
    }

    public EventReadDto update(Event changedEvent) throws NotFoundException {
        entityManager.getTransaction().begin();

        var Entity = eventRepositoryImpl.findById(changedEvent.getId())
            .orElseThrow(NotFoundException::new);

        updateEntityParameters(changedEvent, Entity);

        var eventDto = eventReadMapper.mapFrom(eventRepositoryImpl.update(changedEvent));

        entityManager.getTransaction().commit();

        return eventDto;
    }

    private void updateEntityParameters(Event changedEvent, Event Entity) {
        Entity.setEventType(changedEvent.getEventType());
        Entity.setUser(userRepositoryImpl.findById(
            changedEvent.getUser().getId()).orElseThrow(NotFoundException::new));
    }

    public void delete(Event event) {
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
