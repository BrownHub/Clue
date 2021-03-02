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
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance;
	
	// Size of the board
	private int numRows;
	private int numCols;

	// constructor which sets up the board
	public Board() {
		super();
	}
	
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}

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
	
	// initialize the board
	public void initialize() {
		
		// Create a temporary array 
		BoardCell[] temp;
		grid = new BoardCell[numRows][numCols];
		for(int i = 0; i < numRows; i++) {	
			// Create a new array in temp, allocate memory
			temp = new BoardCell[numRows];
			for(int j = 0; j < numCols; j++) {
				// Create a temporary cell, add it to the temporary list
				BoardCell currCell = new BoardCell(i, j);
				temp[j] = currCell;
			}
			// add the temporary list to the board
			grid[i] = temp;
		}

		// Create adjacency lists
		for(int i = 0; i < numRows; i++) {	
			for(int j = 0; j < numCols; j++) {
				if(i + 1 < numRows) {
					grid[i][j].addAdj(grid[i + 1][j]);
				}
				if(j + 1 < numCols) {
					grid[i][j].addAdj(grid[i][j + 1]);
				}
				if(i - 1 >= 0) {
					grid[i][j].addAdj(grid[i - 1][j]);
				}
				if(j - 1 >= 0) {
					grid[i][j].addAdj(grid[i][j - 1]);
				}
			}
		}

		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		
	}
	
	public void loadConfigFiles(String boardCSV) {
		
	}
	
	public void loadSetupConfig(String boardSetup) {
		
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

	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}
	
	public Room getRoom(char c) {
		return roomMap.get(c);
	}
	
	public void setConfigFiles(String boardCSV, String boardSetup) {
		loadConfigFiles(boardCSV);
		loadSetupConfig(boardSetup);
	}

	public int getNumRows() {
		return 1;
	}

	public int getNumColumns() {
		return 1;
	}

}
