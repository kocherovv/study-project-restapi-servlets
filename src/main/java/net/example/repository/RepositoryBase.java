package net.example.repository;

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
        entityManager.getTransaction().begin();

        var criteriaQuery = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteriaQuery.from(clazz);

        entityManager.getTransaction().commit();

        return entityManager.createQuery(criteriaQuery)
            .getResultList();
    }

    @Override
    public Optional<E> findById(ID id) {
        entityManager.getTransaction().begin();

        var entity = Optional.ofNullable(entityManager.find(clazz, id));

        entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public Optional<E> findById(ID id, Map<String, Object> properties) {
        entityManager.getTransaction().begin();

        var entity = Optional.ofNullable(entityManager.find(clazz, id, properties));

        entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public E create(E entity) {
        entityManager.getTransaction().begin();

        entityManager.persist(entity);

        entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public E update(E entity) {
        entityManager.getTransaction().begin();

        entityManager.merge(entity);

        entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public void delete(E entity) {
        entityManager.getTransaction().begin();

        entityManager.remove(entity);

        entityManager.getTransaction().commit();
    }
}
