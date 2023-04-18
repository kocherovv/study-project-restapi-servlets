package net.example.listener;

import jakarta.persistence.PrePersist;
import net.example.domain.entity.Event;

import java.time.LocalDateTime;

public class EventListener {

    @PrePersist
    public void fillPrePersistParameters(Event entity) {
        entity.setCreatedAt(LocalDateTime.now());
    }
}
