package org.graph;

/**
 * class Vertex
 * @author Ruslan Luchnikov
 *
 * @param <E>
 */
public class Vertex<E> {
	
	private E value;

	private boolean marked;
	
	private int weight = 0;
	
	Vertex() {
		
	}
	
	Vertex(E value) {
		this.value = value;
    }
	
	public Vertex(E value, int weight) {
        this(value);
        this.weight = weight;
    }
	
	public E getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

	public void mark() {
		marked = true;
	}

	public boolean isMarked() {
		return marked;
	}

}
