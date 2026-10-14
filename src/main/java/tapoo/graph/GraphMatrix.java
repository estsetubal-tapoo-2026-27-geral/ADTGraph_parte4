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
 * Undirected graph using Matrix. Teaching starter for A3.4.
 * CLASS TODOs: insertVertex, insertEdge and incidentEdges.
 * All other operations are supplied.
 * @param <V> vertex element type
 * @param <E> edge element type
 */
public class GraphMatrix<V, E> implements Graph<V, E> {
    private static final int DIM = 20;
    private List<Vertex<V>> vertices;
    private Edge<E, V>[][] matrix;

    public GraphMatrix() {
        vertices = new ArrayList<>();
        matrix = createMatrix(DIM);
    }

    @Override
    public int numVertices() { return vertices.size(); }
    @Override
    public int numEdges() { return edges().size(); }
    @Override
    public Collection<Vertex<V>> vertices() { return new ArrayList<>(vertices); }
    @Override
    public Collection<Edge<E, V>> edges() {
        List<Edge<E, V>> result = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++)
            for (int j = i; j < vertices.size(); j++)
                if (matrix[i][j] != null) result.add(matrix[i][j]);
        return result;
    }

    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) {
        return matrix[checkVertex(u)][checkVertex(v)] != null;
    }

    @Override
    public Vertex<V> insertVertex(V vElement) {
        // TODO CLASS: reject duplicate elements, create and store a MyVertex.
        // Call ensureCapacity() before adding the vertex.
        throw new UnsupportedOperationException("TODO CLASS: insertVertex");
    }

    @Override
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement) {
        // TODO CLASS: validate both vertices and reject duplicate edge elements.
        // Reject occupied cells; store the SAME edge in both symmetric cells.
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
        checkVertex(v);
        Vertex<V>[] ends = checkEdge(e).vertices();
        if (ends[0] == v) return ends[1];
        if (ends[1] == v) return ends[0];
        return null;
    }
    @Override
    public V removeVertex(Vertex<V> v) {
        int removed = checkVertex(v);
        int size = vertices.size();
        // Rebuild without the removed row/column; unused cells stay null.
        Edge<E, V>[][] compact = createMatrix(matrix.length);
        for (int i = 0, ni = 0; i < size; i++) {
            if (i == removed) continue;
            for (int j = 0, nj = 0; j < size; j++) {
                if (j == removed) continue;
                compact[ni][nj++] = matrix[i][j];
            }
            ni++;
        }
        vertices.remove(removed);
        matrix = compact;
        return v.element();
    }
    @Override
    public E removeEdge(Edge<E, V> e) {
        Vertex<V>[] ends = checkEdge(e).vertices();
        int i = checkVertex(ends[0]);
        int j = checkVertex(ends[1]);
        matrix[i][j] = null;
        matrix[j][i] = null;
        return e.element();
    }
    @Override
    public V replace(Vertex<V> v, V newElement) {
        MyVertex vertex = (MyVertex) vertices.get(checkVertex(v));
        Vertex<V> existing = findVertex(newElement);
        if (existing != null && existing != v)
            throw new InvalidVertexException("A vertex with this element already exists.");
        V previous = vertex.element;
        vertex.element = newElement;
        return previous;
    }
    @Override
    public E replace(Edge<E, V> e, E newElement) {
        MyEdge edge = (MyEdge) checkEdge(e);
        for (Edge<E, V> current : edges()) {
            if (current != e && Objects.equals(current.element(), newElement))
                throw new InvalidEdgeException("An edge with this element already exists.");
        }
        E previous = edge.element;
        edge.element = newElement;
        return previous;
    }

    private int checkVertex(Vertex<V> vertex) {
        for (int i = 0; i < vertices.size(); i++)
            if (vertex != null && vertices.get(i) == vertex) return i;
        throw new InvalidVertexException();
    }

    private Edge<E, V> checkEdge(Edge<E, V> edge) {
        for (Edge<E, V> current : edges())
            if (edge != null && current == edge) return edge;
        throw new InvalidEdgeException();
    }

    private Vertex<V> findVertex(V element) {
        for (Vertex<V> vertex : vertices)
            if (Objects.equals(vertex.element(), element)) return vertex;
        return null;
    }

    private boolean existsEdgeWith(E element) {
        for (Edge<E, V> edge : edges())
            if (Objects.equals(edge.element(), element)) return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    private Edge<E, V>[][] createMatrix(int size) {
        return (Edge<E, V>[][]) new Edge<?, ?>[size][size];
    }

    /** Grow before inserting a vertex when all existing rows are in use. */
    private void ensureCapacity() {
        if (vertices.size() < matrix.length) return;
        Edge<E, V>[][] expanded = createMatrix(matrix.length * 2);
        for (int i = 0; i < matrix.length; i++)
            System.arraycopy(matrix[i], 0, expanded[i], 0, matrix.length);
        matrix = expanded;
    }

    private class MyVertex implements Vertex<V> {
        private V element;
        
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
            for (int i = 0; i < vertices.size(); i++)
                for (int j = i; j < vertices.size(); j++)
                    if (matrix[i][j] == this)
                        return (Vertex<V>[]) new Vertex<?>[]{vertices.get(i), vertices.get(j)};
            throw new InvalidEdgeException();
        }
        @Override public String toString() { return String.valueOf(element); }
    }

    @Override
    public String toString() {
        return "GraphMatrix: vertices=" + vertices() + ", edges=" + edges();
    }
}
