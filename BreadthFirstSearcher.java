import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search()
	{
		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();
		queue.add(new State(maze.getPlayerSquare(), null, 0, 0));
		maxSizeOfFrontier = 1;
		State currState = null;
		while (!queue.isEmpty()) 
		{
			currState = queue.pop();
			// Update maxDepthSearched
			if (maxDepthSearched < currState.getDepth())
			{
				maxDepthSearched = currState.getDepth();
			}
			// Solution found?
			if (currState.isGoal(maze))
			{
				cost = currState.getGValue();
				currState = currState.getParent();
				// Update maze
				while(currState.getParent() != null)
				{
					maze.setOneSquare(currState.getSquare(), '.');
					currState = currState.getParent();
				}
				return true;
			}
			noOfNodesExpanded++;
			ArrayList<State> successors = currState.getSuccessors(explored, maze);
			for (int index = successors.size() - 1; index >= 0; index--)
			{
				State state = successors.get(index);
				// State is in in the frontier, set to true
				queue.add(state);
				explored[state.getX()][state.getY()] = true;
			}
			// Update maxSizeOfFrontier
			if (maxSizeOfFrontier < queue.size())
			{
				maxSizeOfFrontier = queue.size();
			}
		}
		return false;
	}
}
