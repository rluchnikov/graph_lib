package org.graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class GraphImplTest {

	@Test
	void addVertex() {
	Graph<String> graph =	new GraphImpl<String>();
	graph.addVertex("test");
	graph.addVertex("test1");
	assertAll("Should return vertices",
				() -> assertEquals("test", graph.getVertex("test").getValue()),
				() -> assertEquals("test1", graph.getVertex("test1").getValue()));
	}

	@Test
	void checkVertex() {
		Graph<String> graph =	new GraphImpl<String>();
		graph.addVertex("test");
		graph.addVertex("test");
		assertEquals(graph.getVertexSize(), 1);
	}
	
	@Test
	void addConcurrenceVertex() {
		Graph<String> graph =	new GraphImpl<String>();
		List<Thread> threads = Stream.of(
				new Thread(()-> graph.addVertex("test1")),
				new Thread(()-> graph.addVertex("test2"))
				).peek(Thread::start)
				.collect(Collectors.toList());
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertAll("Should return vertices",
			    () -> assertEquals("test1", graph.getVertex("test1").getValue()),
			    () -> assertEquals("test2", graph.getVertex("test2").getValue()));
	}

	@Test
	void addDirectedEdge() {
		Graph<String> directedGraph =	new GraphImpl<String>(true);
        directedGraph.addEdge("test1","test2");
		assertAll("Should return edges",
				() -> assertNotNull( directedGraph.getEdge("test1")),
				() -> assertTrue(directedGraph.getEdge("test2").size() == 0));
	}

	@Test
    void addUnderectedEdge() {
        Graph<String> undirectedGraph =	new GraphImpl<String>(false);
        undirectedGraph.addEdge("test1","test2");
        assertAll("Should return edges",
                () -> assertNotNull(undirectedGraph.getEdge("test1")),
                () -> assertNotNull(undirectedGraph.getEdge("test2")));
    }

	@Test
	void addConcurrenceEdge() {
		Graph<String> dirGraph = new GraphImpl<String>(true);
        Graph<String> unGraph =	new GraphImpl<String>(false);
		List<Thread> threads = Stream.of(
				new Thread(()-> dirGraph.addEdge("test1","test2")),
				new Thread(()-> unGraph.addEdge("test1","test3")),
				new Thread(()-> dirGraph.addEdge("test2","test3"))
		).peek(Thread::start).collect(Collectors.toList());
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertAll("Should return edges",
				() -> assertTrue(dirGraph.getEdge("test1").size()>0),
				() -> assertTrue(unGraph.getEdge("test1").size()>0),
				() -> assertTrue(unGraph.getEdge("test3").size()>0),
				() -> assertTrue(dirGraph.getEdge("test2").size()>0));
	}

	@Test
	void getPathUngraph() {
		Graph<String> unGraph =	new GraphImpl<String>(false);
		List<Thread> threads = Stream.of(
				new Thread(()-> unGraph.addEdge("test1","test2")),
				new Thread(()-> unGraph.addEdge("test3","test4")),
				new Thread(()-> unGraph.addEdge("test2","test4"))
		).peek(Thread::start).collect(Collectors.toList());
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Set<Edge<String>> edge = unGraph.getPath("test1","test4");
		edge.forEach(i->System.out.printf("path: %s ",i.toString()));
		assertTrue(edge.size()>0);
	}

	@Test
	void getPathDirgraph() {
		Graph<String> directedGraph =	new GraphImpl<String>(true);
		List<Thread> threads = Stream.of(
				new Thread(()-> directedGraph.addEdge("test1","test2")),
				new Thread(()-> directedGraph.addEdge("test3","test4")),
				new Thread(()-> directedGraph.addEdge("test2","test4"))
		).peek(Thread::start).collect(Collectors.toList());
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Set<Edge<String>> edgeNull = directedGraph.getPath("test1","test3");
		assertTrue(edgeNull.size() == 0);
		Set<Edge<String>> edges = directedGraph.getPath("test1","test4");
		edges.forEach(i->System.out.printf("path: %s ",i.toString()));
		assertTrue(edges.size()>0);
	}

}
