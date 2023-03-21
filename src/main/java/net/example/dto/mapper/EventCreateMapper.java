package net.example.dto.mapper;

import net.example.domain.entity.Event;
import net.example.dto.EventCreateDto;

public class EventCreateMapper implements Mapper<Event, EventCreateDto> {
    @Override
    public Event mapFrom(EventCreateDto dto) {
        return Event.builder()
            .eventType(dto.getEventType())
            .user(dto.getUser())
            .file(dto.getFile())
            .createdBy(dto.getCreatedBy())
            .build();
    }
}
