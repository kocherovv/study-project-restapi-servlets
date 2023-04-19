package net.example.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String extension;

    private byte[] content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void fillPrePersistParameters() {
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void updateLastChangeDate() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}
