package net.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Data
public class FileCreateDto {

    private final String name;

    private final String extension;

    private final byte[] content;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final String createdBy;

    @Builder.Default
    private final List<Long> events_id = new ArrayList<>();
}
