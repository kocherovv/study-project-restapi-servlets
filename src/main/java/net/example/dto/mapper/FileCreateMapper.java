package net.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import net.example.repository.impl.EventRepositoryImpl;
import net.example.domain.entity.File;
import net.example.dto.FileCreateDto;
import net.example.exception.NotFoundException;

@RequiredArgsConstructor
public class FileCreateMapper implements Mapper<File, FileCreateDto>{

    private final EventRepositoryImpl eventRepository;

    @Override
    public File mapFrom(FileCreateDto source) {
        return File.builder()
            .name(source.getName())
            .content(source.getContent())
            .events(source.getEvents_id().stream()
                .map(id -> eventRepository.findById(id)
                    .orElseThrow(NotFoundException::new))
                .toList())
            .extension(source.getExtension())
            .createdBy(source.getCreatedBy())
            .createdAt(source.getCreatedAt())
            .updatedAt(source.getUpdatedAt())
            .build();
    }
}
