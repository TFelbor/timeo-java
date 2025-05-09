package timeo-src;
import graph-components.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Time-O orienteering problem implementation to maximize score by visiting
 * controls within their time windows, respecting the time limit and penalties.
 */
public class TimeO {

	// Helper class to store info about each control from the course file.
	private static class Control {
		
		String code;
		int points;
		double openTime;
		double closeTime;

		Control ( String code, int points, double openTime, double closeTime ) {
			
			this.code = code;
			this.points = points;
			this.openTime = openTime;
			this.closeTime = closeTime;
			
		}
	}

	// Helper class to store a visit to a control in the solution path.
	private static class Visit {
		
		String code;
		int id;
		double time;
		int points;

		Visit ( String code, int id, double time, int points ) {
			
			this.code = code;
			this.id = id;
			this.time = time;
			this.points = points;
			
		}
	}

	// Helper class to store the best solution found during backtracking.
	private static class Solution {
		
		List<Visit> visits;
		double totalTime;
		int totalScore;
		int rawScore;
		int penalty;

		Solution () {
			
			visits = new ArrayList<>();
			totalTime = 0.0;
			totalScore = 0;
			rawScore = 0;
			penalty = 0;
			
		}
	}

	public static void main ( String[] args ) {
		
		/*
		 * Extract map file, course file, and pace from command line, output error
		 * if input is in unexpected format.
		 */
		if ( args.length != 3 ) {
			System.err.println("Usage: java TimeO <map_file> <course_file> <pace>");
			return;
		}

		String mapFile = args[0];
		String courseFile = args[1];
		double pace;
		
		try {
			
			pace = Double.parseDouble(args[2]);
			
		} catch ( NumberFormatException e ) {
			
			System.err.println("Error: Pace must be a number");
			return;
		}
		
		// Read map and course files
		try {
			
			Graph graph = new AdjacencyListGraph();
			Map<String,Vertex> codeToVertex = new HashMap<>();
			List<Control> controls = new ArrayList<>();
			double timeLimit = 0.0;
			double penaltyPerMinute = 0.0;

			readMapFile(mapFile,graph,codeToVertex,pace);
			readCourseFile(courseFile,controls,codeToVertex,graph);

			// Find time limit and penalty from course file
			try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
				
				String line = br.readLine();
				if ( line != null ) {
					
					String[] parts = line.trim().split("\\s+");
					if ( parts.length == 3 && parts[0].equals("timelimit") ) {
						
						timeLimit = Double.parseDouble(parts[1]);
						penaltyPerMinute = Double.parseDouble(parts[2]);
						
					}
				}
			}

			// Find the best path using backtracking
			Solution bestSolution =
			    findBestPath(graph,controls,codeToVertex,timeLimit,penaltyPerMinute);

			// Output the results
			printResults(bestSolution,timeLimit,penaltyPerMinute);

		} catch ( IOException e ) {
			System.err.println("Error reading files: " + e.getMessage());
		}
	}

	private static void readMapFile ( String mapFile, Graph graph,
	                                  Map<String,Vertex> codeToVertex,
	                                  double pace )
	    throws IOException {
		
		// Read the map file and build the graph with vertices and edges.
		try (BufferedReader br = new BufferedReader(new FileReader(mapFile))) {
			
			String line = br.readLine();
			if ( line == null ) return;

			// Read control codes
			String[] parts = line.trim().split("\\s+");
			if ( !parts[0].equals("controls") ) return;
			int numControls = Integer.parseInt(parts[1]);

			// Add start/finish vertex
			Vertex startVertex = graph.insertVertex("start");
			codeToVertex.put("start",startVertex);

			// Add control vertices
			for ( int i = 2 ; i < parts.length && i < 2 + numControls ; i++ ) {
				
				String code = parts[i];
				Vertex vertex = graph.insertVertex(code);
				codeToVertex.put(code,vertex);
				
			}

			// Read edges and add to graph
			while ( (line = br.readLine()) != null ) {
				
				parts = line.trim().split("\\s+");
				if ( parts.length != 4 ) continue;

				String src = parts[0];
				String dst = parts[1];
				double dist = Double.parseDouble(parts[2]) * pace;
				double revDist = Double.parseDouble(parts[3]) * pace;

				Vertex srcVertex = codeToVertex.get(src);
				Vertex dstVertex = codeToVertex.get(dst);
				
				if ( srcVertex != null && dstVertex != null ) {
					
					graph.insertEdge(srcVertex,dstVertex,dist);
					graph.insertEdge(dstVertex,srcVertex,revDist);
					
				}
			}
		}
	}

	private static void readCourseFile ( String courseFile,
	                                     List<Control> controls,
	                                     Map<String,Vertex> codeToVertex,
	                                     Graph graph )
	    throws IOException {
		
		// Read the course file and store control information.
		try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
			
			String line;
			// Skip timelimit and controls lines
			br.readLine();
			br.readLine();

			// Read control details
			while ( (line = br.readLine()) != null ) {
				
				String[] parts = line.trim().split("\\s+");
				if ( parts.length != 4 ) continue;

				String code = parts[0];
				int points = Integer.parseInt(parts[1]);
				double openTime = Double.parseDouble(parts[2]);
				double closeTime = Double.parseDouble(parts[3]);

				controls.add(new Control(code,points,openTime,closeTime));

				// Ensure vertex exists
				if ( !codeToVertex.containsKey(code) ) {
					
					Vertex vertex = graph.insertVertex(code);
					codeToVertex.put(code,vertex);
					
				}
			}
		}
	}

	private static Solution findBestPath ( Graph graph, List<Control> controls,
	                                       Map<String,Vertex> codeToVertex,
	                                       double timeLimit,
	                                       double penaltyPerMinute ) {
		
		// Use backtracking to find the path with maximum score.
		Solution bestSolution = new Solution();
		List<Visit> currentPath = new ArrayList<>();
		Set<String> visitedControls = new HashSet<>();
		Vertex startVertex = codeToVertex.get("start");

		// Find the latest close time for pruning
		double latestCloseTime = 0.0;
		for ( Control control : controls ) {
			
			latestCloseTime = Math.max(latestCloseTime,control.closeTime);
			
		}

		// Start backtracking from start vertex
		backtrack(graph,controls,codeToVertex,startVertex,0.0,0,0,timeLimit,
		          penaltyPerMinute,latestCloseTime,currentPath,visitedControls,
		          bestSolution);

		return bestSolution;
	}
	

	// Recursively explore all possible paths, updating the best solution
	private static void backtrack ( Graph graph, List<Control> controls,
	                                Map<String,Vertex> codeToVertex,
	                                Vertex currentVertex, double currentTime,
	                                int currentScore, int currentPenalty,
	                                double timeLimit, double penaltyPerMinute,
	                                double latestCloseTime,
	                                List<Visit> currentPath,
	                                Set<String> visitedControls,
	                                Solution bestSolution ) {
		

		// Check if we can improve the best solution by returning to start
		Vertex startVertex = codeToVertex.get("start");
		for ( Edge edge : graph.incidentEdges(currentVertex) ) {
			
			Vertex nextVertex = graph.opposite(currentVertex,edge);
			if ( nextVertex.equals(startVertex) ) {
				
				double travelTime = (Double) edge.getObject();
				double returnTime = currentTime + travelTime;
				int rawScore = currentScore;
				int penalty = (int) Math.ceil(Math.max(0,returnTime - timeLimit))
				    * (int) penaltyPerMinute;
				int totalScore = rawScore - penalty;

				if ( totalScore > bestSolution.totalScore ) {
					
					// Update best solution
					bestSolution.visits = new ArrayList<>(currentPath);
					bestSolution.visits.add(new Visit("start",0,returnTime,0));
					bestSolution.totalTime = returnTime;
					bestSolution.totalScore = totalScore;
					bestSolution.rawScore = rawScore;
					bestSolution.penalty = penalty;
					
				}
			}
		}

		// Prune if no more points can be gained
		if ( currentTime > latestCloseTime ) {
			
			return;
			
		}

		// Try visiting each control
		for ( int i = 0 ; i < controls.size() ; i++ ) {
			
			Control control = controls.get(i);
			String code = control.code;
			
			if ( visitedControls.contains(code) ) continue;

			Vertex nextVertex = codeToVertex.get(code);
			for ( Edge edge : graph.incidentEdges(currentVertex) ) {
				
				if ( graph.opposite(currentVertex,edge).equals(nextVertex) ) {
					
					double travelTime = (Double) edge.getObject();
					double arrivalTime = currentTime + travelTime;

					// Wait until open time if arriving early
					double visitTime = Math.max(arrivalTime,control.openTime);

					// Check if visit is within time window
					int points = (visitTime <= control.closeTime) ? control.points : 0;

					// Estimate maximum possible score for branch-and-bound
					int maxPossibleScore = currentScore + points;
					for ( Control other : controls ) {
						
						if ( !visitedControls.contains(other.code)
						    && !other.code.equals(code) ) {
							
							maxPossibleScore += other.points;
							
						}
					}
					
					// Subtract estimated penalty for overtime from max possible score for pruning
					maxPossibleScore -=
					    (int) Math.ceil(Math.max(0,arrivalTime - timeLimit))
					        * (int) penaltyPerMinute;

					if ( maxPossibleScore <= bestSolution.totalScore ) {
						// Prune if we can’t beat the best score
						continue;
					}

					// Add control to path
					currentPath.add(new Visit(code,i + 1,visitTime,points));
					visitedControls.add(code);

					// Recurse
					backtrack(graph,controls,codeToVertex,nextVertex,visitTime,
					          currentScore + points,currentPenalty,timeLimit,
					          penaltyPerMinute,latestCloseTime,currentPath,
					          visitedControls,bestSolution);

					// Backtrack
					currentPath.remove(currentPath.size() - 1);
					visitedControls.remove(code);
				}
			}
		}
	}

	private static void printResults ( Solution solution, double timeLimit,
	                                   double penaltyPerMinute ) {
		
		// Print the results in a descriptive format with totals and control visits.
		System.out.println("Total Time:\t" + solution.totalTime);
		System.out.println("Total Score:\t" + solution.totalScore);
		System.out.println("Raw Score:\t" + solution.rawScore);
		System.out.println("Total Penalty:\t" + solution.penalty);
		System.out.println("\nControls Visited (end -> start) :");
		int printCap = 2;

		for ( int i = 0 ; i < solution.visits.size() ; i++ ) {
			
			Visit visit = solution.visits.get(i);
			String entry =
			    String.format("{ ID: %d | Code: %s | Time: %.1f | Points: %d }",
			                  visit.id,visit.code,visit.time,visit.points);
			
			if ( printCap != 0 ) {
				System.out.print(" -> " + entry + " ");
				printCap--;
			} else {
				System.out.println(" -> " + entry + " ->");
				printCap = 2;
			}
			
		}
	}
}
