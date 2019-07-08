import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();
		// Add root state
		State rootState = new State(maze.getPlayerSquare(), null, 0, 0);
		StateFValuePair root = new StateFValuePair(rootState, (double) rootState.getDepth() +
				generateHeuristic(rootState.getSquare(), maze.getGoalSquare()));
		frontier.add(root);
		maxSizeOfFrontier = 1;
		StateFValuePair currPair = null;
		while (!frontier.isEmpty()) 
		{
			currPair = frontier.poll();
			explored[currPair.getState().getX()][currPair.getState().getY()] = true;
			noOfNodesExpanded++;
			// Update maxDepthSearched
			if (maxDepthSearched < currPair.getState().getDepth())
			{
				maxDepthSearched = currPair.getState().getDepth();
			}
			// Solution found?
			if (currPair.getState().isGoal(maze))
			{
				State currState = currPair.getState();
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
			ArrayList<State> successors = currPair.getState().getSuccessors(explored, maze);
			// Iterate through successor list and update frontier
			for (int index = successors.size() - 1; index >= 0; index--)
			{
				StateFValuePair newPair = new StateFValuePair(successors.get(index), 
						(double) successors.get(index).getDepth() +
						generateHeuristic(successors.get(index).getSquare(), maze.getGoalSquare()));
				boolean swapPair = false;
				for (StateFValuePair frontierPair : frontier)
				{
					// Successor is already in frontier
					if (newPair.getState().getX() == frontierPair.getState().getX() && 
							newPair.getState().getY() == frontierPair.getState().getY())
					{
						// New g(n) is smaller than old g(n), update frontier
						if(newPair.getState().getGValue() < frontierPair.getState().getGValue())
						{
							frontier.remove(frontierPair);
							frontier.add(newPair);
							swapPair = true;
							break;
						}
					}
				}
				if (!swapPair)
				{
					frontier.add(newPair);
				}
			}
			// Update maxSizeOfFrontier
			if (maxSizeOfFrontier < frontier.size())
			{
				maxSizeOfFrontier = frontier.size();
			}
		}
		return false;
	}

	private double generateHeuristic(Square s1, Square s2)
	{
		return (double) Math.sqrt(Math.pow((s1.X - s2.X), 2) + Math.pow(s1.Y - s2.Y, 2));
	}
}
