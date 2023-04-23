package net.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
@Data
public class FileReadDto extends AbstractReadDto {

    private final Long id;

    private final Long userId;

    private final String name;

    private final String extension;

    private final byte[] content;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;
}
