package org.espenhahn.allocate.util.graph;

public interface GraphEdge<T> {
	double getCost();
	GraphVertex<T> getStart();
	GraphVertex<T> getEnd();
	GraphEdgeType getType();
}