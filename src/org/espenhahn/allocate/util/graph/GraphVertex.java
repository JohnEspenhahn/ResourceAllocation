package org.espenhahn.allocate.util.graph;

import java.util.HashMap;
import java.util.Map;

public class GraphVertex<T> implements Comparable<GraphVertex<T>> {
	T value;
	Map<GraphVertex<T>, GraphEdge<T>> edges;
	boolean visited;
	
	public GraphVertex(T value) {
		this.value = value;
		this.edges = new HashMap<GraphVertex<T>, GraphEdge<T>>();
	}
	
	public void addEdge(GraphVertex<T> vertex) {
		GraphEdge<T> edge = new GraphEdgeDirected<T>(0, this, vertex);
		edges.put(vertex, edge);
	}
	
	public void dfs(GraphVisitor<T> visitor) {
		if (visited) return;
		
		visited = true;
		for (GraphEdge<T> edge: edges.values()) {
			GraphVertex<T> end = edge.getEnd();
			if (!end.visited) end.dfs(visitor);
		}
		
		visitor.onVisitVertex(this);
	}
	
	/**
	 * Cleanup after being removed
	 */
	protected void onRemoved() {
		for (GraphEdge<T> edge: edges.values()) {
			GraphVertex<T> end = edge.getEnd();
			end.removeEdgeTo(this);
		}
	}
	
	protected void removeEdgeTo(GraphVertex<T> vertex) {
		edges.remove(vertex);
	}

	@Override
	public int compareTo(GraphVertex<T> o) {
		return o.edges.size() - edges.size();
	}

}