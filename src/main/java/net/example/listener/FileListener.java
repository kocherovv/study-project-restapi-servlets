package net.example.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import net.example.domain.entity.File;

import java.time.LocalDateTime;

public class FileListener {

    @PrePersist
    public void fillPrePersistParameters(File entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void updateLastChangeDate(File entity) {
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
