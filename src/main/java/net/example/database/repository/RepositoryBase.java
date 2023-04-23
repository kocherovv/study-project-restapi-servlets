package net.example.database.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase<E, ID extends Serializable> implements CrudRepository<E, ID> {
    private final Class<E> clazz;

    private final EntityManager entityManager;

    @Override
    public List<E> findAll() {
        var criteriaQuery = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteriaQuery.from(clazz);

        return entityManager.createQuery(criteriaQuery)
            .getResultList();
    }

    @Override
    public Optional<E> findById(ID id) {

        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public Optional<E> findById(ID id, Map<String, Object> properties) {

        return Optional.ofNullable(entityManager.find(clazz, id, properties));
    }

    @Override
    public E create(E entity) {
        entityManager.persist(entity);

        return entity;
    }

    @Override
    public E update(E entity) {
        entityManager.merge(entity);

        return entity;
    }

    @Override
    public void delete(E entity) {
        entityManager.remove(entity);
        entityManager.flush();
    }
}
