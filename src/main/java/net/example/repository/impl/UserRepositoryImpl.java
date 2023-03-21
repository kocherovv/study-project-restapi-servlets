package net.example.repository.impl;

import jakarta.persistence.EntityManager;
import net.example.domain.entity.User;
import net.example.repository.RepositoryBase;
import net.example.repository.UserRepository;

public class UserRepositoryImpl extends RepositoryBase<User, Long> implements UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
        this.entityManager = entityManager;
    }
}
