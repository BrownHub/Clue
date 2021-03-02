package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {

	// member variables
	private BoardCell[][] grid;
	private ArrayList<String[]> stringGrid = new ArrayList();
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance = new Board();

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
		try {
			File layoutConfig = new File(layoutConfigFile);
			Scanner fin = new Scanner(layoutConfig);
			String[] temp;
			while(fin.hasNext()) {
				temp = fin.nextLine().split(",");
				stringGrid.add(temp);
				numCols = temp.length;
				numRows++;
			}
			grid = new BoardCell[numRows][numCols];
			File setupConfig = new File(setupConfigFile); 
			fin = new Scanner(setupConfig);
			BoardCell currCenterCell = null;
			BoardCell currLabelCell = null;
			while(fin.hasNext()) {
				temp = fin.nextLine().split(", ");
				if(!temp[0].contains("//")) {
					for(int i = 0; i < numRows; i++) {
						for(int j = 0; j < numCols; j++) {
							if(stringGrid.get(i)[j] == temp[2].charAt(0) + "*") {
								currCenterCell = new BoardCell(i, j);
							}
							if(stringGrid.get(i)[j] == temp[2].charAt(0) + "#") {
								currLabelCell = new BoardCell(i, j);
							}
						}
					}
					if(temp[0] == "Room") {
						roomMap.put(temp[2].charAt(0), new Room(temp[1], currCenterCell, currLabelCell));
					}
				}
			}
			// Room, Canal, C
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not read file layout");
		}
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

	public void loadConfigFiles() {

	}

	// initializes the roomMap according to the setup config
	public void loadSetupConfig() {

	}

	public void loadLayoutConfig() {	

	}

	// getters for targets and cells

	public void setConfigFiles(String boardCSV, String boardSetup) {
		layoutConfigFile = "data\\" + boardCSV;
		setupConfigFile = "data\\" + boardSetup;
	}

	public Set<BoardCell> getTargets(){
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return new BoardCell(0, 0);
	}

	public Room getRoom(BoardCell cell) {
		return new Room("Stub", new BoardCell(0,0), new BoardCell(0,0));
	}

	public Room getRoom(char c) {
		return new Room("Stub", new BoardCell(0,0), new BoardCell(0,0));
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numCols;
	}
}
