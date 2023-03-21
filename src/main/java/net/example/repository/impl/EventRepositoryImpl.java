package net.example.repository.impl;

import jakarta.persistence.EntityManager;
import net.example.repository.RepositoryBase;
import net.example.domain.entity.Event;
import net.example.repository.EventRepository;

public class EventRepositoryImpl extends RepositoryBase<Event, Long> implements EventRepository {

    private final EntityManager entityManager;

    public EventRepositoryImpl(EntityManager entityManager) {
        super(Event.class, entityManager);
        this.entityManager = entityManager;
    }
}
