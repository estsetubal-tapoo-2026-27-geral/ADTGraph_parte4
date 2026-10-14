/* 
 * The MIT License
 *
 * Copyright 2019 brunomnsilva@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tapoo.graph;

import java.util.*;

/**
 * Undirected graph using AdjacencyList. Teaching starter for A3.4.
 * CLASS TODOs: insertVertex, insertEdge and incidentEdges.
 * CONSOLIDATION TODOs: opposite, removal and replacement.
 * @param <V> vertex element type
 * @param <E> edge element type
 */
public class GraphAdjacencyList<V, E> implements Graph<V, E> {
    private Map<V, Vertex<V>> vertices;

    public GraphAdjacencyList() {
        vertices = new HashMap<>();
    }

    @Override
    public int numVertices() { return vertices.size(); }
    @Override
    public int numEdges() { return edges().size(); }
    @Override
    public Collection<Vertex<V>> vertices() { return new ArrayList<>(vertices.values()); }
    @Override
    public Collection<Edge<E, V>> edges() {
        Set<Edge<E, V>> result = new HashSet<>();
        for (Vertex<V> v : vertices.values()) result.addAll(((MyVertex) v).incidentEdges);
        return new ArrayList<>(result);
    }

    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) {
        MyVertex first = checkVertex(u);
        MyVertex second = checkVertex(v);
        Set<Edge<E, V>> common = new HashSet<>(first.incidentEdges);
        common.retainAll(second.incidentEdges);
        return !common.isEmpty();
    }

    @Override
    public Vertex<V> insertVertex(V vElement) {
        // TODO CLASS: reject duplicate elements, create and store a MyVertex.
        // Use the element as the map key.
        throw new UnsupportedOperationException("TODO CLASS: insertVertex");
    }

    @Override
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement) {
        // TODO CLASS: validate both vertices and reject duplicate edge elements.
        // Add the SAME edge to both incident lists, once only for a self-loop.
        throw new UnsupportedOperationException("TODO CLASS: insertEdge");
    }

    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) {
        // TODO CLASS: validate v and return an independent collection.
        throw new UnsupportedOperationException("TODO CLASS: incidentEdges");
    }

    /** Supplied convenience overload; delegates to the class exercise. */
    @Override
    public Edge<E, V> insertEdge(V first, V second, E element) {
        return insertEdge(findVertex(first), findVertex(second), element);
    }

    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) {
        // TODO CONSOLIDATION: validate both references; return null if not incident.
        throw new UnsupportedOperationException("TODO CONSOLIDATION: opposite");
    }
    @Override
    public V removeVertex(Vertex<V> v) {
        // TODO CONSOLIDATION: remove all incident edges and invalidate v.
        // Keep all collections consistent.
        throw new UnsupportedOperationException("TODO CONSOLIDATION: removeVertex");
    }
    @Override
    public E removeEdge(Edge<E, V> e) {
        // TODO CONSOLIDATION: remove every stored reference to e.
        throw new UnsupportedOperationException("TODO CONSOLIDATION: removeEdge");
    }
    @Override
    public V replace(Vertex<V> v, V newElement) {
        // TODO CONSOLIDATION: validate, reject duplicates, return previous element.
        // Update the map key as well as the stored element.
        throw new UnsupportedOperationException("TODO CONSOLIDATION: replace vertex");
    }
    @Override
    public E replace(Edge<E, V> e, E newElement) {
        // TODO CONSOLIDATION: validate, reject duplicates, return previous element.
        // Preserve the edge reference.
        throw new UnsupportedOperationException("TODO CONSOLIDATION: replace edge");
    }

    private MyVertex checkVertex(Vertex<V> vertex) {
        for (Vertex<V> current : vertices.values())
            if (vertex != null && current == vertex) return (MyVertex) current;
        throw new InvalidVertexException();
    }

    private Edge<E, V> checkEdge(Edge<E, V> edge) {
        for (Edge<E, V> current : edges())
            if (edge != null && current == edge) return edge;
        throw new InvalidEdgeException();
    }

    private Vertex<V> findVertex(V element) {
        for (Vertex<V> vertex : vertices.values())
            if (Objects.equals(vertex.element(), element)) return vertex;
        return null;
    }

    private boolean existsEdgeWith(E element) {
        for (Edge<E, V> edge : edges())
            if (Objects.equals(edge.element(), element)) return true;
        return false;
    }

    
    private class MyVertex implements Vertex<V> {
        private V element;
        private List<Edge<E, V>> incidentEdges = new ArrayList<>();
        private MyVertex(V element) { this.element = element; }
        @Override public V element() { return element; }
        @Override public String toString() { return String.valueOf(element); }
    }

    private class MyEdge implements Edge<E, V> {
        private E element;
        
        private MyEdge(E element) {
            this.element = element;
            
        }
        @Override public E element() { return element; }
        @Override @SuppressWarnings("unchecked")
        public Vertex<V>[] vertices() {
            Vertex<V> first = null;
            for (Vertex<V> vertex : vertices.values()) {
                if (((MyVertex) vertex).incidentEdges.contains(this)) {
                    if (first != null) return (Vertex<V>[]) new Vertex<?>[]{first, vertex};
                    first = vertex;
                }
            }
            if (first != null) return (Vertex<V>[]) new Vertex<?>[]{first, first};
            throw new InvalidEdgeException();
        }
        @Override public String toString() { return String.valueOf(element); }
    }

    @Override
    public String toString() {
        return "GraphAdjacencyList: vertices=" + vertices() + ", edges=" + edges();
    }
}
