package net.example.dto.mapper;

import net.example.domain.entity.Event;
import net.example.dto.EventReadDto;

public class EventReadMapper implements Mapper<EventReadDto, Event> {
    @Override
    public EventReadDto mapFrom(Event source) {
        return EventReadDto.builder()
            .id(source.getId())
            .eventType(source.getEventType())
            .userId(source.getUser().getId())
            .fileInfo(source.getFileInfo())
            .createdAt(source.getCreatedAt())
            .build();
    }
}
