package net.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.EventRepositoryImpl;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.dto.UserCreateDto;
import net.example.dto.UserReadDto;
import net.example.dto.mapper.UserCreateMapper;
import net.example.dto.mapper.UserReadMapper;
import net.example.exception.NotFoundException;
import net.example.model.AppStatusCode;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class UserService implements CrudService<UserCreateDto, UserReadDto, Long> {

    private final UserRepositoryImpl userRepositoryImpl;
    private final EventRepositoryImpl eventRepositoryImpl;

    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;

    public List<UserReadDto> findAll() {
        return userRepositoryImpl.findAll().stream()
            .map(userReadMapper::mapFrom)
            .toList();
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepositoryImpl.findById(id)
            .map(userReadMapper::mapFrom);
    }

    public UserReadDto create(UserCreateDto dto) {
        return userReadMapper.mapFrom(userRepositoryImpl.create(userCreateMapper.mapFrom(dto)));
    }

    public UserReadDto update(UserReadDto dto) throws NotFoundException {
        var event = userRepositoryImpl.findById(dto.getId())
            .orElseThrow(NotFoundException::new);

        event.setName(dto.getName());
        event.setEmail(dto.getEmail());

        return userReadMapper.mapFrom(userRepositoryImpl.update(event));
    }

    public void deleteById(Long id) throws NotFoundException {
        userRepositoryImpl.findById(id).ifPresentOrElse(
            userRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });
    }

    public Optional<UserReadDto> findByUserNameAndPassword(String username, byte[] password) {
        return userRepositoryImpl.findByUserNameAndPassword(username, password)
            .map(userReadMapper::mapFrom);
    }
}
