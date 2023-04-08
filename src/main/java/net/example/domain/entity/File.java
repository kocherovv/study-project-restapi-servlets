package net.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.example.Listener.AuditableListener;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@ToString(exclude = "events")
@EqualsAndHashCode(of = "name", callSuper = false)
@Table(name = "file")
@EntityListeners(AuditableListener.class)
public class File extends AuditableEntity {

    private String name;

    private String extension;

    private byte[] content;

    @OneToMany(mappedBy = "file", fetch = FetchType.EAGER)
    @Builder.Default
    private List<Event> events = new ArrayList<>();
}
