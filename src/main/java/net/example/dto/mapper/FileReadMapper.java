package net.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import net.example.domain.entity.File;
import net.example.dto.FileReadDto;

@RequiredArgsConstructor
public class FileReadMapper implements Mapper<FileReadDto, File> {

    private final EventReadMapper eventReadMapper;

    @Override
    public FileReadDto mapFrom(File source) {
        return FileReadDto.builder()
            .id(source.getId())
            .name(source.getName())
            .content(source.getContent())
            .events(source.getEvents().stream()
                .map(eventReadMapper::mapFrom)
                .toList())
            .extension(source.getExtension())
            .createdAt(source.getCreatedAt())
            .updatedAt(source.getUpdatedAt())
            .build();
    }
}
