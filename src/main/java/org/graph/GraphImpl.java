package org.graph;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 *  The {@code GraphImpl} class represents an undirected graph of vertices
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the vertices adjacent to a vertex. It also provides
 *  methods for get path between path between two vertices.
 *  This implementation uses an adjacency-lists representation, which 
 *  is a array of objects.
 *
 *  @param <E>
 *  
 *  @author Ruslan Luchnikov 
 */
public class GraphImpl<E> implements Graph<E> {
	
	private CopyOnWriteArrayList<Vertex<E>> vertices = new CopyOnWriteArrayList<>();
	
	private ConcurrentHashMap<Vertex<E>, ArrayList<Edge<E>>> adjList = new ConcurrentHashMap<>();
	
       
    @Override
    public synchronized void addEdge(E source, E destination,Boolean isDirected) {
    	if (null == source || null == destination) {
			throw new NullPointerException("Elements cannot be null !!");
		}
		Vertex<E> from = new Vertex(source);
		Vertex<E> to = new Vertex(destination);
		Edge<E> edge = new Edge<>(from, to);
		addVertices(from,to);
		adjList.computeIfPresent(from, (vertex, edgeList) -> {
			edgeList.add(new Edge<>(from, to));
			return edgeList; });
		if (!isDirected)
			adjList.computeIfPresent(to, (vertex, edgeList) -> {
				edgeList.add(new Edge<>(to, from));
				return edgeList; });

    }

    @Override
    public void addVertex(E element) {
    	if (null == element)
			throw new NullPointerException("Element cannot be null !!");

    	Vertex v = new Vertex(element);
		System.out.printf("%s \n",
				Thread.currentThread().getName());
    	if (vertices.contains(v)) {
			System.out.printf("Vertex exist");
			return;
		}
    	vertices.add(v);
    	adjList.putIfAbsent(v, new ArrayList<>());
		System.out.printf("vertex add:%s \n",v.getValue());

    }
    
    public Vertex<E> getVertex(E element) {
    	return vertices.stream().filter(e -> e.getValue().equals(element)).findAny().orElse(new Vertex());
    }

    @Override
	public int getVertexSize() {
		return vertices.size();
	}

	@Override
    public List<Edge<E>> getEdge(E vertex) {
		return adjList.searchEntries(1,e -> {
			if (e.getKey().getValue().equals(vertex)) {
				return e.getValue();
			}
			return null;
		});
	}

	
	private void addVertices (Vertex<E> source, Vertex<E> destination) {
	List<Vertex<E>> newVertices =  Arrays.asList(source,destination);
	int value =	vertices.addAllAbsent(newVertices);
	if (value>0)
		newVertices.parallelStream().forEach(i->adjList.putIfAbsent(i, new ArrayList<>()));
	}

	public Set<Edge<E>> getPath(E from, E to) {
		final Set<Edge<E>> visitedEdges = new HashSet<>();
		if (from.equals(to)) {
			return visitedEdges;
		}
		return collect(new Vertex<E>(from), new Vertex<E>(to), visitedEdges);
	}

	private Set<Edge<E>> collect(Vertex<E> start, Vertex<E> target, Set<Edge<E>> visited) {
		Stack<Vertex<E>> next = new Stack<>();
		next.add(start);
		while (!next.isEmpty()) {
			Vertex<E> i = next.pop();
			i.mark();
			//Enqueue new neighbors
			for (Edge<E> edge : adjList.get(i)) {
				if (!visited.contains((edge))) {
					visited.add(edge);
					Vertex<E> neighbor = edge.getNext();
					neighbor.mark();
					if (neighbor.equals(target)) {
						return visited;
					}
					next.add(neighbor);
				}
			}
		}
		return visited.stream().allMatch(e -> e.getNext().getValue().equals(target.getValue())) ? visited : new HashSet<>();
	}

	}
