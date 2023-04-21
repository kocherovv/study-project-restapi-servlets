package net.example.service;

import jakarta.persistence.EntityManager;
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

    private final EntityManager entityManager;

    private final UserRepositoryImpl userRepositoryImpl;

    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;

    public List<UserReadDto> findAll() {
        entityManager.getTransaction().begin();

        var users = userRepositoryImpl.findAll().stream()
            .map(userReadMapper::mapFrom)
            .toList();

        entityManager.getTransaction().commit();

        return users;
    }

    public Optional<UserReadDto> findById(Long id) {
        entityManager.getTransaction().begin();

        var users = userRepositoryImpl.findById(id)
            .map(userReadMapper::mapFrom);

        entityManager.getTransaction().commit();

        return users;
    }

    public UserReadDto create(UserCreateDto dto) {
        entityManager.getTransaction().begin();

        var newUser = userReadMapper.mapFrom(userRepositoryImpl.create(userCreateMapper.mapFrom(dto)));

        entityManager.getTransaction().commit();

        return newUser;
    }

    public UserReadDto update(UserReadDto dto) throws NotFoundException {
        entityManager.getTransaction().begin();

        var event = userRepositoryImpl.findById(dto.getId())
            .orElseThrow(NotFoundException::new);

        event.setName(dto.getName());
        event.setEmail(dto.getEmail());

        var updatedUser = userReadMapper.mapFrom(userRepositoryImpl.update(event));

        entityManager.getTransaction().commit();

        return updatedUser;
    }

    public void deleteById(Long id) throws NotFoundException {
        entityManager.getTransaction().begin();

        userRepositoryImpl.findById(id).ifPresentOrElse(
            userRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });

        entityManager.getTransaction().commit();
    }

    public Optional<UserReadDto> findByUserNameAndPassword(String username, byte[] password) {
        entityManager.getTransaction().begin();

        var user = userRepositoryImpl.findByUserNameAndPassword(username, password)
            .map(userReadMapper::mapFrom);

        entityManager.getTransaction().commit();

        return user;
    }
}
