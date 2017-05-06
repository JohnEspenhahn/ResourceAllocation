package org.espenhahn.allocate.util.graph;

import java.util.Collection;
import java.util.LinkedList;

public class Graph<T> {

	private final Collection<GraphVertex<T>> vs;
	
	public Graph() {
		this(new LinkedList<GraphVertex<T>>());
	}
	
	/**
	 * Create a heap backed by the given collection
	 * @param vs The backing collection
	 */
	public Graph(Collection<GraphVertex<T>> vs) {
		this.vs = vs;
	}
	
	public GraphVertex<T> addVertex(T value) {
		GraphVertex<T> v = new GraphVertex<T>(value);
		vs.add(v);
		return v;
	}
	
	public boolean isEmpty() {
		return vs.isEmpty();
	}
	
}
