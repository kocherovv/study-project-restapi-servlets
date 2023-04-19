package net.example.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.example.domain.enums.EventType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JdbcTypeCode(SqlTypes.JSON)
    private String fileInfo;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private LocalDateTime createdAt;

    @PrePersist
    public void fillPrePersistParameters() {
        this.setCreatedAt(LocalDateTime.now());
    }
}
