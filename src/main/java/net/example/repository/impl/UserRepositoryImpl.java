package net.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import net.example.domain.entity.User;
import net.example.repository.RepositoryBase;
import net.example.repository.UserRepository;

import java.util.Optional;

@Transactional
public class UserRepositoryImpl extends RepositoryBase<User, Long> implements UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
        this.entityManager = entityManager;
    }

    public Optional<User> findByUserName(String userName) {
        return Optional.ofNullable(
            entityManager.createQuery("select u from User u where u.name = :name", User.class)
                .setParameter("name", userName)
                .getSingleResult());
    }
}
