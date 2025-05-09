package graph-components;

/**
 * An undirected graph.
 */
public interface Graph {

	/**
	 * Get the number of vertices in this graph.
	 * 
	 * @return the number of vertices in this graph
	 */
	public int numVertices ();

	/**
	 * Get the number of edges in this graph.
	 * 
	 * @return the number of edges in this graph
	 */
	public int numEdges ();

	/**
	 * Get the vertices in this graph.
	 * 
	 * @return the vertices in this graph
	 */
	public Iterable<Vertex> vertices ();

	/**
	 * Get the edges in this graph.
	 * 
	 * @return the edges in this graph
	 */
	public Iterable<Edge> edges ();

	/**
	 * Get a vertex in this graph.
	 * 
	 * @return a vertex in this graph
	 */
	public Vertex aVertex ();

	/**
	 * Get the degree of the specified vertex.
	 * 
	 * @param v
	 *          the vertex
	 * @return the degree of v
	 */
	public int degree ( Vertex v );

	/**
	 * Get the vertices adjacent to the specified vertex.
	 * 
	 * @param v
	 *          the vertex
	 * @return the vertices adjacent to v
	 */
	public Iterable<Vertex> adjacentVertices ( Vertex v );

	/**
	 * Get the edges incident on the specified vertex.
	 * 
	 * @param v
	 *          the vertex
	 * @return the edges incident on v
	 */
	public Iterable<Edge> incidentEdges ( Vertex v );

	/**
	 * Get the endpoint vertices for the specified edge.
	 * 
	 * @param e
	 *          the edge
	 * @return the endpoint vertices for e
	 */
	public Vertex[] endVertices ( Edge e );

	/**
	 * Get the other endpoint vertex for the specified edge.
	 * 
	 * @param v
	 *          one endpoint of e (v must be an endpoint of e)
	 * @param e
	 *          the edge
	 * @return the other endpoint of e
	 */
	public Vertex opposite ( Vertex v, Edge e );

	/**
	 * Are two vertices adjacent?
	 * 
	 * @param v
	 *          a vertex
	 * @param w
	 *          another vertex
	 * @return true if v and w are adjacent, false otherwise
	 */
	public boolean areAdjacent ( Vertex v, Vertex w );

	/**
	 * Insert an edge (v,w) with an associated object.
	 * 
	 * @param v
	 *          a vertex
	 * @param w
	 *          another vertex
	 * @param obj
	 *          the object to be associated with the edge
	 * @return the new edge inserted
	 */
	public Edge insertEdge ( Vertex v, Vertex w, Object obj );

	/**
	 * Insert a vertex with an associated object.
	 * 
	 * @param obj
	 *          the object to be associated with the vertex
	 * @return the new vertex inserted
	 */
	public Vertex insertVertex ( Object obj );

	/**
	 * Remove the specified vertex and all of its incident edges.
	 * 
	 * @param v
	 *          the vertex to remove
	 */
	public void removeVertex ( Vertex v );

	/**
	 * Remove the specified edge.
	 * 
	 * @param e
	 *          the edge to remove
	 */
	public void removeEdge ( Edge e );

}