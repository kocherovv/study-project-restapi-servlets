package net.example.database.graphs;

import lombok.AllArgsConstructor;
import net.example.domain.entity.File;
import org.hibernate.Session;
import org.hibernate.graph.RootGraph;

@AllArgsConstructor
public class FileGraphs {

    private Session session;

    public RootGraph<File> withUser() {
        var entityGraphs = session.createEntityGraph(File.class);
        entityGraphs.addAttributeNode("user");

        return entityGraphs;
    }
}
