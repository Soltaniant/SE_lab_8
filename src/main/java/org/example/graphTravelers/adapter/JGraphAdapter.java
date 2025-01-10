package org.example.graphTravelers.adapter;

import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Collection;
import java.util.stream.Collectors;

public class JGraphAdapter implements GraphAdapter {
    private final DefaultDirectedGraph<Integer, String> graph;

    public JGraphAdapter() {
        this.graph = new DefaultDirectedGraph<>(String.class);
    }

    @Override
    public void addVertex(Integer vertex) {
        graph.addVertex(vertex);
    }

    @Override
    public void addEdge(String edge, Integer vertex1, Integer vertex2) {
        graph.addEdge(vertex1, vertex2);
    }

    @Override
    public Collection<Integer> getNeighbors(Integer vertex) {
        return graph.outgoingEdgesOf(vertex).stream()
                .map(graph::getEdgeTarget)
                .collect(Collectors.toSet());
    }
}