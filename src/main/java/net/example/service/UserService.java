package net.example.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.example.database.repository.impl.UserRepositoryImpl;
import net.example.domain.entity.User;
import net.example.dto.UserReadDto;
import net.example.exception.NotFoundException;
import net.example.mapper.UserReadMapper;
import net.example.model.AppStatusCode;
import net.example.util.PasswordHasher;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class UserService {

    private final EntityManager entityManager;

    private final UserRepositoryImpl userRepositoryImpl;

    private final UserReadMapper userReadMapper;

    public List<UserReadDto> findAll() {
        entityManager.getTransaction().begin();

        var usersDto = userRepositoryImpl.findAll().stream()
            .map(userReadMapper::mapFrom)
            .toList();

        entityManager.getTransaction().commit();

        return usersDto;
    }

    public Optional<UserReadDto> findById(User user) {
        entityManager.getTransaction().begin();

        var userDto = userRepositoryImpl.findById(user.getId())
            .map(userReadMapper::mapFrom);

        entityManager.getTransaction().commit();

        return userDto;
    }

    public UserReadDto create(User user) {
        entityManager.getTransaction().begin();

        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));

        var userDto = userReadMapper.mapFrom(userRepositoryImpl.create(user));

        entityManager.getTransaction().commit();

        return userDto;
    }

    public UserReadDto update(User user) throws NotFoundException {
        entityManager.getTransaction().begin();

        var entity = userRepositoryImpl.findById(user.getId())
            .orElseThrow(NotFoundException::new);

        entity.setName(user.getName());
        entity.setEmail(user.getEmail());

        var userDto = userReadMapper.mapFrom(userRepositoryImpl.update(entity));

        entityManager.getTransaction().commit();

        return userDto;
    }

    public void delete(User user) throws NotFoundException {
        entityManager.getTransaction().begin();

        userRepositoryImpl.findById(user.getId()).ifPresentOrElse(
            userRepositoryImpl::delete,
            () -> {
                throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
            });

        entityManager.getTransaction().commit();
    }
}
