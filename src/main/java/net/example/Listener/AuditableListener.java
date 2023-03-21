package net.example.Listener;

import net.example.domain.entity.AuditableEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
