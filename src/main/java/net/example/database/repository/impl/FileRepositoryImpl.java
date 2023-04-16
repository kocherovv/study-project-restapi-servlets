package net.example.database.repository.impl;

import jakarta.persistence.EntityManager;
import net.example.database.repository.FileRepository;
import net.example.database.repository.RepositoryBase;
import net.example.domain.entity.File;

import java.util.List;

public class FileRepositoryImpl extends RepositoryBase<File, Long> implements FileRepository {

    private final EntityManager entityManager;

    public FileRepositoryImpl(EntityManager entityManager) {
        super(File.class, entityManager);
        this.entityManager = entityManager;
    }

    public List<File> findAllByUserId(Long userId) {
        entityManager.getTransaction().begin();

        var files = entityManager.createQuery(
                "select f from File f where f.user.id = :userId")
            .setParameter("userId", userId).getResultList();

        entityManager.getTransaction().commit();

        return files;
    }
}
