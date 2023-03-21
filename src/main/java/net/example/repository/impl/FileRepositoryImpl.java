package net.example.repository.impl;

import jakarta.persistence.EntityManager;
import net.example.domain.entity.File;
import net.example.repository.RepositoryBase;
import net.example.repository.FileRepository;

public class FileRepositoryImpl extends RepositoryBase<File, Long> implements FileRepository {

    private final EntityManager entityManager;

    public FileRepositoryImpl(EntityManager entityManager) {
        super(File.class, entityManager);
        this.entityManager = entityManager;
    }
}
