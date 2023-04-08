package net.example.Listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import net.example.domain.entity.AuditableEntity;

import java.time.Instant;

public class AuditableListener <T extends AuditableEntity> {

    @PrePersist
    public void fillPrePersistParameters(T entity) {
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
    }

    @PreUpdate
    public void updateLastChangeDate(T entity) {
        entity.setUpdatedAt(Instant.now());
    }
}
