package net.example.mapper;

import lombok.RequiredArgsConstructor;
import net.example.domain.entity.File;
import net.example.dto.FileReadDto;

@RequiredArgsConstructor
public class FileReadMapper implements Mapper<FileReadDto, File> {

    @Override
    public FileReadDto mapFrom(File source) {
        return FileReadDto.builder()
            .id(source.getId())
            .name(source.getName())
            .content(source.getContent())
            .userId(source.getUser().getId())
            .extension(source.getExtension())
            .createdAt(source.getCreatedAt())
            .updatedAt(source.getUpdatedAt())
            .build();
    }
}
