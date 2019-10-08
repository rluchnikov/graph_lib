package org.graph;

import java.util.List;
import java.util.Set;

public interface Graph<E> {
	
	/**
	 * Method to add a edge when two elements are given
	 * 
	 * @param source the name of the first Vertex
	 * @param destination the name of the second Vertex
	 * @param isDirected direction
	 */
	 void addEdge(E source, E destination,Boolean isDirected);

	List<Edge<E>> getEdge(E vertex);

 	 /**
      * Method to add a new vertex
      * 
	  * @param element
	  */
	 void addVertex(E element);
	 
	Vertex<E> getVertex(E element);

	int getVertexSize();

	/**
	 * Method to get path between two vertices
	 *
	 * @param from
	 * @param to
	 */
	Set<Edge<E>> getPath(E from, E to);

}
