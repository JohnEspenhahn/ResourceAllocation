package org.espenhahn.allocate.util.graph;

public class GraphEdgeDirected<T> extends AGraphEdge<T> {

	public GraphEdgeDirected(double cost, GraphVertex<T> start, GraphVertex<T> end) {
		super(cost, start, end);
	}

	@Override
	public GraphEdgeType getType() {
		return GraphEdgeType.DIRECTED;
	}
	
}