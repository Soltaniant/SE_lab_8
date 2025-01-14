package org.example.graphTravelers.adapter;

import java.util.Collection;

public interface GraphAdapter {

    void addVertex(Integer vertex);
    void addEdge(String edge, Integer starVertex, Integer endVertex);
    Collection<Integer> getNeighbors(Integer vertex);
}
