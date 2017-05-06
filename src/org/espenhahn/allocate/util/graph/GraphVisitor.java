package org.espenhahn.allocate.util.graph;

public interface GraphVisitor<T> {

	void onVisitVertex(GraphVertex<T> vertex);
	
}
