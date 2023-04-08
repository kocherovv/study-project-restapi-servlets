package net.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import net.example.domain.entity.File;
import net.example.repository.RepositoryBase;
import net.example.repository.FileRepository;

import java.util.ArrayList;

public class FileRepositoryImpl extends RepositoryBase<File, Long> implements FileRepository {

    private final EntityManager entityManager;

    public FileRepositoryImpl(EntityManager entityManager) {
        super(File.class, entityManager);
        this.entityManager = entityManager;
    }

    public ArrayList<File> findAllByUserId(Long userId) {
        entityManager.getTransaction().begin();

        var query = entityManager.createQuery(
            "select f from File f left join Event e where e.user.id = :userId");
        query.setParameter("userId", userId);

        return (ArrayList<File>) query.getResultList();
    }
}
