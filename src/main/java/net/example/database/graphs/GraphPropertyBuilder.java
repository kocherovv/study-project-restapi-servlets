package net.example.database.graphs;

import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;

import java.util.Map;

public class GraphPropertyBuilder {

    private final FileGraphs fileGraphs;

    public GraphPropertyBuilder(Session session) {
        fileGraphs = new FileGraphs(session);
    }

    public Map<String, Object> getProperty(GraphPropertyName graphPropertyName) {
        Object rootGraph = null;

        switch (graphPropertyName) {
            case FILE_WITH_USER -> rootGraph = fileGraphs.withUser();
        }

        assert rootGraph != null;
        return Map.of(GraphSemantic.LOAD.getJakartaHintName(), rootGraph);
    }
}
