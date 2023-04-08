package net.example.repository.impl;

import jakarta.persistence.EntityManager;
import net.example.domain.entity.User;
import net.example.repository.RepositoryBase;
import net.example.repository.UserRepository;

import java.util.Optional;

public class UserRepositoryImpl extends RepositoryBase<User, Long> implements UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
        this.entityManager = entityManager;
    }

    public Optional<User> findByUserNameAndPassword(String userName, byte[] password) {
        entityManager.getTransaction().begin();

        var entity = Optional.ofNullable(
            entityManager.createQuery("select u from User u where (u.name = :name and u.password = :password)", User.class)
                .setParameter("name", userName)
                .setParameter("password", password)
                .getSingleResult());

        entityManager.getTransaction().commit();

        return entity;
    }
}
