package net.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.dto.EventReadDto;
import net.example.dto.UserCreateDto;
import net.example.dto.UserReadDto;
import net.example.dto.mapper.EventReadMapper;
import net.example.dto.mapper.UserCreateMapper;
import net.example.dto.mapper.UserReadMapper;
import net.example.exception.NotFoundException;
import net.example.model.AppStatusCode;
import net.example.repository.impl.EventRepositoryImpl;
import net.example.repository.impl.UserRepositoryImpl;
import net.example.util.PasswordHasher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class UserService implements CrudService<UserCreateDto, UserReadDto, Long> {

    private final UserRepositoryImpl userRepositoryImpl;
    private final EventRepositoryImpl eventRepositoryImpl;

    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;
    private final EventReadMapper eventReadMapper;

    public List<UserReadDto> findAll() {
        return userRepositoryImpl.findAll().stream()
            .map(userReadMapper::mapFrom)
            .toList();
    }

    @Transactional
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

        event.setEvents(dto.getEvents().stream()
            .map(EventReadDto::getId)
            .map(id -> eventRepositoryImpl.findById(id)
                .orElseThrow(NotFoundException::new))
            .toList());
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

    public boolean userAuthentication(String username, byte[] password) {
        return userRepositoryImpl.findByUserName(username)
            .map(value ->
                Arrays.equals(value.getPassword(), PasswordHasher.hashPassword(password)))
            .orElse(false);
    }
}
