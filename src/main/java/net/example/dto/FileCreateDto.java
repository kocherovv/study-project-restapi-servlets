package net.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class FileCreateDto {

    private final Long userId;

    private final String name;

    private final String extension;

    private final byte[] content;
}
