package graph-components;

/**
 * This class provides a skeletal implementation of Graph to reduce the effort
 * needed for a concrete implementation.
 */
public abstract class AbstractGraph implements Graph {

	/**
	 * This class provides a skeletal implementation of Edge to reduce the effort
	 * needed for a concrete implementation.
	 */
	protected class AbstractEdge implements Edge {

		@Override
		public Object getObject () {
			// TODO implement!
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * This class provides a skeletal implementation of Vertex to reduce the
	 * effort needed for a concrete implementation.
	 */
	protected class AbstractVertex implements Vertex {

		@Override
		public Object getObject () {
			// TODO implement!
			throw new UnsupportedOperationException();
		}

	}

	
	public Vertex aVertex () {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public int degree ( Vertex v ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public Iterable<Vertex> vertices () {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public Vertex opposite ( Vertex v, Edge e ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public Vertex[] endVertices ( Edge e ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public Iterable<Edge> edges () {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public int numEdges () {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public int numVertices () {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public Edge insertEdge ( Vertex v, Vertex w, Object obj ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public Vertex insertVertex ( Object obj ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public Iterable<Edge> incidentEdges ( Vertex v ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public Iterable<Vertex> adjacentVertices ( Vertex v ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public boolean areAdjacent ( Vertex v, Vertex w ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public void removeEdge ( Edge e ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}

	public void removeVertex ( Vertex v ) {
		// TODO implement!
		throw new UnsupportedOperationException();
	}
	
}
