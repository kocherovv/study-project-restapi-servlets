package net.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.dto.mapper.EventReadMapper;
import net.example.model.AppStatusCode;
import net.example.repository.impl.EventRepositoryImpl;
import net.example.dto.EventCreateDto;
import net.example.dto.EventReadDto;
import net.example.dto.mapper.EventCreateMapper;
import net.example.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class EventService implements CrudService <EventCreateDto, EventReadDto, Long>{

    private final EventRepositoryImpl eventRepositoryImpl;

    private final EventCreateMapper eventCreateMapper;
    private final EventReadMapper eventReadMapper;

    public List<EventReadDto> findAll() {
        return eventRepositoryImpl.findAll().stream()
            .map(eventReadMapper::mapFrom)
            .toList();
    }

    public Optional<EventReadDto> findById(Long id) {
        return eventRepositoryImpl.findById(id)
            .map(eventReadMapper::mapFrom);
    }

    public EventReadDto create(EventCreateDto dto) {
        return eventReadMapper.mapFrom(eventRepositoryImpl.create(eventCreateMapper.mapFrom(dto)));
    }

    public EventReadDto update(EventReadDto eventReadDto) {
        var event = eventRepositoryImpl.findById(eventReadDto.getId())
            .orElseThrow(NotFoundException::new);

        event.setEventType(eventReadDto.getEventType());
        event.setUser(eventReadDto.getUser());
        event.setFile(eventReadDto.getFile());

        return eventReadMapper.mapFrom(eventRepositoryImpl.update(event));
    }

    public void deleteById(Long id) throws NotFoundException {
        eventRepositoryImpl.findById(id).ifPresentOrElse(
            eventRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });
    }
}
