package net.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.example.domain.enums.EventType;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
@Data
public class EventCreateDto {

    private final Long userId;

    private final String fileInfo;

    private final EventType eventType;

    private final LocalDateTime createdAt;

    private final String createdBy;
}
