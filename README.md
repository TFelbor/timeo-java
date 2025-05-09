# CPSC327 : Data Structures & Algs. Project 4 - Time-Based Path Orienteering Solver

TimeO is a Java application that solves the time-based orienteering problem by finding an optimal path to maximize the score when visiting control points within their designated time windows while respecting time limits and penalties.

## Overview

Orienteering is a sport where participants navigate from point to point in unfamiliar terrain using a map and compass. This program implements a solution for a time-constrained variant where:

- Each control point has a score value
- Control points are only valid within specific time windows (open/close times)
- There is a global time limit with penalties for exceeding it
- The goal is to maximize the total score by visiting optimal control points

The algorithm uses backtracking with branch-and-bound optimization to find the best possible route through the control points.

## Requirements

- Java 8 or higher
- No additional libraries required

## Project Structure

- `TimeO.java` - Main implementation file with the algorithms and driver code
- Graph components (dependencies/pre-built):
  - `Graph.java` - Graph interface
  - `Edge.java` - Edge interface
  - `Vertex.java` - Vertex interface
  - `AbstractGraph.java` - Skeletal implementation of Graph
  - `AdjacencyListGraph.java` - Concrete implementation using adjacency lists
  - `AdjacencyMatrixGraph.java` - Alternative implementation using adjacency matrices

## Data Files

The program works with two input files:

1. **Map file** (`westpoint14-timeo.map`): Contains the graph structure with distances
   - First line: `controls <num_controls> <control_codes...>`
   - Remaining lines: `<source> <destination> <distance_forward> <distance_backward>`

2. **Course file** (`westpoint14-timeo.course`): Contains control point information
   - First line: `timelimit <time_limit> <penalty_per_minute>`
   - Second line: `controls <num_controls>`
   - Remaining lines: `<control_code> <points> <open_time> <close_time>`

## Usage

```bash
java TimeO <map_file> <course_file> <pace>
```

### Parameters

- `map_file`: Path to the map file
- `course_file`: Path to the course file
- `pace`: The pace value used to adjust distances (higher values mean slower travel)

### Example

```bash
java TimeO westpoint14-timeo.map westpoint14-timeo.course 1.2
```

## Algorithm Details

The program uses:

1. **Backtracking**: Recursively explores all possible paths
2. **Branch-and-bound**: Prunes paths that cannot lead to a better solution
3. **Graph Theory**: Models the terrain as a weighted graph

The algorithm performs the following steps:
1. Parse map and course files
2. Build a graph representation of controls and paths
3. Use backtracking to explore all possible paths
4. Apply pruning to avoid unpromising paths
5. Return the optimal solution (maximum score)

## Output

The program outputs:
- Total time taken for the route
- Total score (raw score minus penalties)
- Raw score (sum of control point values)
- Total penalty incurred
- List of controls visited in sequence

## License

MIT

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Acknowledgments

This project implements the Time-O variant of the orienteering problem, which is a well-studied optimization problem in operations research.
