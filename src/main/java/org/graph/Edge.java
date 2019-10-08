package org.graph;

/**
 * class Edge
 * @author Ruslan Luchnikov
 *
 * @param <E>
 */
public class Edge<E> {
	
	private final Vertex<E> source;
    private final Vertex<E> next;
    
    
	public Edge(Vertex<E> source, Vertex<E> next) {
		this.source = source;
		this.next = next;
	}

	public Vertex<E> getNext () {
		return next;
	}

	public Vertex<E> getSource () {
		return source;
	}

	@Override
	public String toString() {
		return "Edge{" +
				"source=" + source.getValue() +
				", next=" + next.getValue() +
				'}';
	}
}
