package net.example.dto;

import lombok.*;
import net.example.domain.entity.File;
import net.example.domain.entity.User;
import net.example.domain.enums.EventType;

import java.time.Instant;

@RequiredArgsConstructor
@Builder
@Data
public class EventReadDto {

    private final Long id;

    private final User user;

    private final File file;

    private final EventType eventType;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final String createdBy;
}
