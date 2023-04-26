package net.example.mapper;

import net.example.domain.entity.File;
import net.example.dto.FileInfoDto;

public class FileInfoDtoMapper implements Mapper<FileInfoDto, File> {
    @Override
    public FileInfoDto mapFrom(File source) {
        return FileInfoDto.builder()
            .id(source.getId())
            .userId(source.getUser().getId())
            .name(source.getName())
            .extension(source.getExtension())
            .updatedAt(source.getUpdatedAt())
            .createdAt(source.getCreatedAt())
            .build();
    }
}
