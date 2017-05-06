package org.espenhahn.allocate.util.graph;

public abstract class AGraphEdge<T> implements GraphEdge<T> {
	double cost;
	GraphVertex<T> start, end;
	
	public AGraphEdge(double cost, GraphVertex<T> start, GraphVertex<T> end) {
		this.cost = cost;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public double getCost() {
		return cost;
	}
	
	@Override
	public GraphVertex<T> getStart() {
		return start;
	}
	
	@Override
	public GraphVertex<T> getEnd() {
		return end;
	}
}