package experiment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TestBoard {

	// member variables
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;

	// Size of the board
	final static int COLS = 4;
	final static int ROWS = 4;

	// constructor which sets up the board
	public TestBoard() {
		super();
		// Create a temporary array 
		TestBoardCell[] temp;
		grid = new TestBoardCell[ROWS][COLS];
		for(int i = 0; i < ROWS; i++) {	
			// Create a new array in temp, allocate memory
			temp = new TestBoardCell[ROWS];
			for(int j = 0; j < COLS; j++) {
				// Create a temporary cell, add it to the temporary list
				TestBoardCell currCell = new TestBoardCell(i, j);
				temp[j] = currCell;
			}
			// add the temporary list to the board
			grid[i] = temp;
		}

		// Create adjacency lists
		for(int i = 0; i < ROWS; i++) {	
			for(int j = 0; j < COLS; j++) {
				if(i + 1 < ROWS) {
					grid[i][j].addAdjacency(grid[i + 1][j]);
				}
				if(j + 1 < COLS) {
					grid[i][j].addAdjacency(grid[i][j + 1]);
				}
				if(i - 1 >= 0) {
					grid[i][j].addAdjacency(grid[i - 1][j]);
				}
				if(j - 1 >= 0) {
					grid[i][j].addAdjacency(grid[i][j - 1]);
				}
			}
		}

		targets = new HashSet<TestBoardCell>();
		visited = new HashSet<TestBoardCell>();
	}

	// recursive function which calculates the possible cells the player can move to
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}

	private void findAllTargets(TestBoardCell startCell, int pathlength) {
		for(TestBoardCell adjCell : startCell.getAdjList()) {
			if(!adjCell.getOccupied() && !adjCell.isRoom()) {
				// if you have already visited the cell, skip
				if(!visited.contains(adjCell)) {
					// add the cell to the visited list
					visited.add(adjCell);
					// base case: the adjacent cells are the targets
					if(pathlength == 1) {
						targets.add(adjCell);
						// recursive case, the targets are contained by the next cell grouping
					} else {
						findAllTargets(adjCell, pathlength - 1);
					}
					// remove the cell from the visited list
					visited.remove(adjCell);
				}
			}
		}
	}

	// getters for targets and cells
	public Set<TestBoardCell> getTargets(){
		return targets;
	}

	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}
}
