package net.example.service;

import net.example.dto.AbstractReadDto;

import java.util.List;
import java.util.Optional;

public interface CrudService<E, T extends AbstractReadDto> {

    List<T> findAll();

    Optional<T> findById(E id);

    T create(E entity);

    T update(E entity);

    void deleteById(E id);
}
