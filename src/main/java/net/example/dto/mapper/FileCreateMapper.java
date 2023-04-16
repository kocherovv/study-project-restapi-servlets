package net.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.domain.entity.File;
import net.example.dto.FileCreateDto;
import net.example.exception.NotFoundException;

@RequiredArgsConstructor
public class FileCreateMapper implements Mapper<File, FileCreateDto> {

    private final UserRepositoryImpl userRepository;

    @Override
    public File mapFrom(FileCreateDto source) {
        return File.builder()
            .name(source.getName())
            .content(source.getContent())
            .user(userRepository.findById(source.getUserId())
                .orElseThrow(NotFoundException::new))
            .extension(source.getExtension())
            .build();
    }
}
