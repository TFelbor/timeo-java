// Author: Tytus Felbor

package graph-components;
import java.util.LinkedList;


public class AdjacencyListGraph extends AbstractGraph {

	// List of all vertices
	private LinkedList<AdjacencyListVertex> vertices;
	// List of all edges
	private LinkedList<AdjacencyListEdge> edges;

	// Constructs an empty adjacency list graph
	public AdjacencyListGraph () {

		vertices = new LinkedList<AdjacencyListVertex>();
		edges = new LinkedList<AdjacencyListEdge>();

	}

	protected class AdjacencyListVertex extends AbstractVertex {

		private Object vertexObject;
		private LinkedList<AdjacencyListEdge> incidentEdges;

		// Constructs a vertex with an associated object
		public AdjacencyListVertex ( Object o ) {

			if ( o == null )
			  throw new IllegalArgumentException("Vertex object is null");

			this.vertexObject = o;
			this.incidentEdges = new LinkedList<>();
		}

		public Object getObject () {

			return vertexObject;

		}
	}

	protected class AdjacencyListEdge extends AbstractEdge {

		private Object edgeObject;
		private AdjacencyListVertex v, w;

		// Constructs an edge between two vertices with an associated object
		public AdjacencyListEdge ( AdjacencyListVertex v, AdjacencyListVertex w,
		                           Object o ) {

			if ( v == null || w == null )
			  throw new IllegalArgumentException("Vertices is null");

			this.v = v;
			this.w = w;
			this.edgeObject = o;

		}

		@Override
		public Object getObject () {

			return edgeObject;

		}
	}

	// Inserts a vertex with an associated object
	@Override
	public Vertex insertVertex ( Object o ) {

		AdjacencyListVertex v = new AdjacencyListVertex(o);
		vertices.add(v);
		return v;

	}

	// Removes a specific edge from the graph
	@Override
	public void removeEdge ( Edge e ) {

		if ( e == null ) throw new IllegalArgumentException("Edge is null");
		if ( !(e instanceof AdjacencyListEdge) )
		  throw new IllegalArgumentException("Edge must be a part of this graph");

		AdjacencyListEdge ale = (AdjacencyListEdge) e;
		ale.v.incidentEdges.remove(ale);
		ale.w.incidentEdges.remove(ale);
		edges.remove(ale);

	}

	// Removes a specifc vertex and all its incident edges
	@Override
	public void removeVertex ( Vertex v ) {

		if ( v == null ) throw new IllegalArgumentException("Vertex is null");
		if ( !(v instanceof AdjacencyListVertex) )
		  throw new IllegalArgumentException("Vertex must be a part of this graph");

		AdjacencyListVertex alv = (AdjacencyListVertex) v;

		while ( !alv.incidentEdges.isEmpty() ) {

			removeEdge(alv.incidentEdges.getFirst());

		}

		vertices.remove(alv);

	}

	// Returns the number of vertices
	@Override
	public int numVertices () {

		return vertices.size();

	}

	// Returns the number of edges
	@Override
	public int numEdges () {

		return edges.size();

	}

	// Returns all vertices in an iterable form
	@Override
	public Iterable<Vertex> vertices () {

		LinkedList<Vertex> vs = new LinkedList<>();
		vs.addAll(vertices);
		return vs;

	}

	// Returns all edges in an iterable form
	@Override
	public Iterable<Edge> edges () {

		LinkedList<Edge> es = new LinkedList<>();
		es.addAll(edges);
		return es;

	}

	// Returns a vertex
	@Override
	public Vertex aVertex () {

		if ( vertices.isEmpty() ) return null;
		else return vertices.getFirst();

	}

	// Returns the degree of the specified vertex
	@Override
	public int degree ( Vertex v ) {

		if ( v == null ) throw new IllegalArgumentException("Vertex is null");
		if ( !(v instanceof AdjacencyListVertex) )
		  throw new IllegalArgumentException("Vertex must be a part of this graph");

		AdjacencyListVertex alv = (AdjacencyListVertex) v;
		return alv.incidentEdges.size();

	}

	// Returns adjacent vertices of a specified vertex in an iterable form
	@Override
	public Iterable<Vertex> adjacentVertices ( Vertex v ) {

		if ( v == null ) throw new IllegalArgumentException("Vertex is null");
		if ( !(v instanceof AdjacencyListVertex) )
		  throw new IllegalArgumentException("Vertex must be from this graph");

		AdjacencyListVertex alv = (AdjacencyListVertex) v;
		LinkedList<Vertex> adjVs = new LinkedList<>();

		for ( AdjacencyListEdge e : alv.incidentEdges ) {

			if ( e.v == alv ) adjVs.add(e.w);
			else adjVs.add(e.v);

		}

		return adjVs;

	}

	// Returns incident edges of a specified vertex in an iterable form
	@Override
	public Iterable<Edge> incidentEdges ( Vertex v ) {

		if ( v == null ) throw new IllegalArgumentException("Vertex is null");
		if ( !(v instanceof AdjacencyListVertex) )
		  throw new IllegalArgumentException("Vertex must be a part of this graph");

		AdjacencyListVertex alv = (AdjacencyListVertex) v;
		LinkedList<Edge> es = new LinkedList<>();
		es.addAll(alv.incidentEdges);
		return es;

	}

	// Returns edge's end vertices
	@Override
	public Vertex[] endVertices ( Edge e ) {

		if ( e == null ) throw new IllegalArgumentException("Edge is null");
		if ( !(e instanceof AdjacencyListEdge) )
		  throw new IllegalArgumentException("Edge must be a part of this graph");

		AdjacencyListEdge ale = (AdjacencyListEdge) e;
		Vertex[] endVs = new Vertex[] { ale.v, ale.w };
		return endVs;

	}

	// Returns edge's opposite vertex to a specified one
	@Override
	public Vertex opposite ( Vertex v, Edge e ) {

		if ( v == null || e == null )
		  throw new IllegalArgumentException("Vertex and edge are null");
		if ( !(e instanceof AdjacencyListEdge)
		    || !(v instanceof AdjacencyListVertex) )
		  throw new IllegalArgumentException("Vertex and edge must be a part of this graph");

		AdjacencyListEdge ale = (AdjacencyListEdge) e;
		AdjacencyListVertex alv = (AdjacencyListVertex) v;

		if ( ale.v == alv ) return ale.w;
		else if ( ale.w == alv ) return ale.v;
		else
		  throw new IllegalArgumentException("Vertex is not an endpoint of this edge");

	}

	// Checks whether two vertices are adjacent
	@Override
	public boolean areAdjacent ( Vertex v1, Vertex v2 ) {

		if ( v1 == null || v2 == null )
		  throw new IllegalArgumentException("Vertices is null");
		if ( !(v1 instanceof AdjacencyListVertex)
		    || !(v2 instanceof AdjacencyListVertex) )
		  throw new IllegalArgumentException("Vertices must be a part of this graph");

		AdjacencyListVertex alv1 = (AdjacencyListVertex) v1;
		AdjacencyListVertex alv2 = (AdjacencyListVertex) v2;

		for ( AdjacencyListEdge e : alv1.incidentEdges ) {

			if ( (e.v == alv1 && e.w == alv2) || (e.w == alv1 && e.v == alv2) ) {

				return true;

			}
		}

		return false;
	}

	// Inserts an edge between two vertices with an associated object
	@Override
	public Edge insertEdge ( Vertex v1, Vertex v2, Object obj ) {

		if ( v1 == null || v2 == null )
		  throw new IllegalArgumentException("Vertices is null");
		if ( !(v1 instanceof AdjacencyListVertex)
		    || !(v2 instanceof AdjacencyListVertex) )
		  throw new IllegalArgumentException("Vertices must be a part of this graph");

		AdjacencyListVertex alv1 = (AdjacencyListVertex) v1;
		AdjacencyListVertex alv2 = (AdjacencyListVertex) v2;

		if ( alv1 == alv2 ) throw new IllegalArgumentException("Equal vertices");

		AdjacencyListEdge e = new AdjacencyListEdge(alv1,alv2,obj);
		alv1.incidentEdges.add(e);
		alv2.incidentEdges.add(e);
		edges.add(e);
		return e;

	}
}