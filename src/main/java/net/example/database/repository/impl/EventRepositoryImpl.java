package net.example.database.repository.impl;

import jakarta.persistence.EntityManager;
import net.example.database.repository.EventRepository;
import net.example.database.repository.RepositoryBase;
import net.example.domain.entity.Event;

import java.util.List;

public class EventRepositoryImpl extends RepositoryBase<Event, Long> implements EventRepository {

    private final EntityManager entityManager;

    public EventRepositoryImpl(EntityManager entityManager) {
        super(Event.class, entityManager);
        this.entityManager = entityManager;
    }

    public List<Event> findAllByUserId(Long userId) {
        entityManager.getTransaction().begin();

        var events = entityManager.createQuery(
                "select e from Event e where e.user.id = :userId")
            .setParameter("userId", userId).getResultList();

        entityManager.getTransaction().commit();

        return events;
    }
}
