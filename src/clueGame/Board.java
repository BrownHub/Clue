package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {

	// member variables
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private String layoutConfigFile;
	private String setuptConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance;
	
	// Size of the board
	private int numRows;
	private int numCols;

	// constructor which sets up the board
	public Board() {
		super();
		/*
		// Create a temporary array 
		BoardCell[] temp;
		grid = new BoardCell[ROWS][COLS];
		for(int i = 0; i < ROWS; i++) {	
			// Create a new array in temp, allocate memory
			temp = new BoardCell[ROWS];
			for(int j = 0; j < COLS; j++) {
				// Create a temporary cell, add it to the temporary list
				BoardCell currCell = new BoardCell(i, j);
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

		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		*/
	}
	/*
	// recursive function which calculates the possible cells the player can move to
	public void calcTargets(BoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}

	private void findAllTargets(BoardCell startCell, int pathlength) {
		for(BoardCell adjCell : startCell.getAdjList()) {
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
	*/
	
	public void initialize() {
		
	}
	
	public void loadConfigFiles() {
		
	}
	
	public void loadSetupConfig() {
		
	}
	
	public void loadLayoutConfig() {
		
	}
	
	// getters for targets and cells
	public Set<BoardCell> getTargets(){
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
}
