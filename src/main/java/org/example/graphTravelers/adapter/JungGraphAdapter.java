package org.example.graphTravelers.adapter;

import edu.uci.ics.jung.graph.SparseMultigraph;
import java.util.Collection;

public class JungGraphAdapter implements GraphAdapter {
    private final SparseMultigraph<Integer, String> graph;

    public JungGraphAdapter() {
        this.graph = new SparseMultigraph<>();
    }

    @Override
    public void addVertex(Integer vertex) {
        graph.addVertex(vertex);
    }

    @Override
    public void addEdge(String edge, Integer vertex1, Integer vertex2) {
        graph.addEdge(edge, vertex1, vertex2);
    }

    @Override
    public Collection<Integer> getNeighbors(Integer vertex) {
        return graph.getNeighbors(vertex);
    }
}