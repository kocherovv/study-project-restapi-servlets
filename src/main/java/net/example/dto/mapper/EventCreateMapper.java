package net.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.domain.entity.Event;
import net.example.dto.EventCreateDto;
import net.example.exception.NotFoundException;

@RequiredArgsConstructor
public class EventCreateMapper implements Mapper<Event, EventCreateDto> {
    private final UserRepositoryImpl userRepository;

    @Override
    public Event mapFrom(EventCreateDto dto) {
        return Event.builder()
            .eventType(dto.getEventType())
            .user(userRepository.findById(dto.getUserId()).orElseThrow(NotFoundException::new))
            .fileInfo(dto.getFileInfo())
            .build();
    }
}
