package net.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
@Data
public class FileInfoDto extends AbstractReadDto {

    private final Long id;

    private final Long userId;

    private final String name;

    private final String extension;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;
}
