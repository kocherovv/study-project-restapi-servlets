package net.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.example.domain.entity.File;
import net.example.domain.entity.User;
import net.example.domain.enums.EventType;

import java.time.Instant;

@RequiredArgsConstructor
@Builder
@Data
public class EventCreateDto {

    private final User user;

    private final File file;

    private final EventType eventType;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final String createdBy;
}
