package org.example.graphTravelers.adapter;

import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.Collection;
import java.util.stream.Collectors;

public class JGraphAdapter implements GraphAdapter {
    private final DefaultUndirectedGraph<Integer, String> graph;

    public JGraphAdapter() {
        this.graph = new DefaultUndirectedGraph<>(String.class);
    }

    @Override
    public void addVertex(Integer vertex) {
        graph.addVertex(vertex);
    }

    @Override
    public void addEdge(String edge, Integer vertex1, Integer vertex2) {
        graph.addEdge(vertex1, vertex2, edge);
    }

    @Override
    public Collection<Integer> getNeighbors(Integer vertex) {
        return graph.edgesOf(vertex).stream()
                .map(graph::getEdgeTarget)
                .collect(Collectors.toSet());
    }
}