package net.example.dto;

import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Data
public class FileCreateDto {

    private final String name;

    private final String extension;

    private final Byte[] content;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final String createdBy;

    @Builder.Default
    private final List<Long> events_id = new ArrayList<>();
}
