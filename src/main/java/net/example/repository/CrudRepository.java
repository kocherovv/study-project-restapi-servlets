package net.example.repository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrudRepository<E, ID extends Serializable> {

    List<E> findAll();

    default Optional<E> findById(ID id) {
        return findById(id, Collections.emptyMap());
    }

    Optional<E> findById(ID id, Map<String, Object> properties);

    E create(E entity);

    E update(E entity);

    void delete(E entity);
}
