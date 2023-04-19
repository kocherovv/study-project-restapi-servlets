package net.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.example.domain.enums.EventType;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
@Data
public class EventReadDto {

    private final Long id;

    private final Long userId;

    private final String fileInfo;

    private final EventType eventType;

    private final LocalDateTime createdAt;
}
