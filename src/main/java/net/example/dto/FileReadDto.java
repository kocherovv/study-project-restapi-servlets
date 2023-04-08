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
public class FileReadDto {

    private final Long id;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final String createdBy;

    private final String name;

    private final String extension;

    private final byte[] content;

    @Builder.Default
    private final List<EventReadDto> events = new ArrayList<>();

}
