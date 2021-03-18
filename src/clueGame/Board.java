package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {

	// member variables
	private BoardCell[][] grid;
	private ArrayList<String[]> stringGrid;
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
		theInstance = new Board();

		return theInstance;
	}

	// recursive function which calculates the possible cells the player can move to
	public void calcTargets(BoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}

	// recursive function called in calcTargets which determines targets
	private void findAllTargets(BoardCell startCell, int pathlength) {		
		for(BoardCell adjCell : startCell.getAdjList()) {
			if(!adjCell.getOccupied() && !adjCell.isRoom() && !visited.contains(adjCell)) {
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
			// add room center if moving into a room
			if(adjCell.isRoomCenter() && !visited.contains(adjCell)) {
				targets.add(adjCell);
			}
		}
	}
	

	// load files to create board and adjacency lists
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		createGrid();

		createAdjacencyList();

		targets = new HashSet<>();
		visited = new HashSet<>();

	}

	//Uses a string grid to create cells and add them to the game board
	private void createGrid() {
		// Create a temporary array 
		BoardCell[] temp;
		grid = new BoardCell[numRows][numCols];
		for(int i = 0; i < numRows; i++) {
			// Create a new array in temp, allocate memory
			temp = new BoardCell[numRows];
			for(int j = 0; j < numCols; j++) {
				// Create a temporary cell, set its attributes, and add it to the temporary list
				BoardCell currCell = new BoardCell(i, j);
				String currCellInfo = stringGrid.get(i)[j];
				currCell.setInitial(currCellInfo.charAt(0));
				currCell.setRoom(roomMap.containsKey(currCell.getInitial()) && currCell.getInitial() != 'X' && currCell.getInitial() != 'W');
				if(currCellInfo.length() > 1) { 
					switch (currCellInfo.charAt(1)) {
					case 'v':
						currCell.setDoorway(true);
						currCell.setDoorDirection(DoorDirection.DOWN);
						currCell.setLabel(false);
						currCell.setRoomCenter(false);
						break;
					case '<':
						currCell.setDoorway(true);
						currCell.setDoorDirection(DoorDirection.LEFT);
						currCell.setLabel(false);
						currCell.setRoomCenter(false);
						break;
					case '>':
						currCell.setDoorway(true);
						currCell.setDoorDirection(DoorDirection.RIGHT);
						currCell.setLabel(false);
						currCell.setRoomCenter(false);
						break;
					case '^':
						currCell.setDoorway(true);
						currCell.setDoorDirection(DoorDirection.UP);
						currCell.setLabel(false);
						currCell.setRoomCenter(false);
						break;
					case '#':
						currCell.setDoorway(false);
						currCell.setDoorDirection(DoorDirection.NONE);
						currCell.setLabel(true);
						roomMap.get(currCell.getInitial()).setLabelCell(currCell);
						currCell.setRoomCenter(false);
						break;
					case '*':
						currCell.setDoorway(false);
						currCell.setDoorDirection(DoorDirection.NONE);
						currCell.setLabel(false);
						currCell.setRoomCenter(true);
						roomMap.get(currCell.getInitial()).setCenterCell(currCell);
						break;
					default:
						currCell.setDoorway(false);
						currCell.setDoorDirection(DoorDirection.NONE);
						currCell.setLabel(false);
						currCell.setRoomCenter(false);
						currCell.setSecretPassage(currCellInfo.charAt(1));
						break;
					}
				} else {
					currCell.setDoorway(false);
					currCell.setDoorDirection(DoorDirection.NONE);
					currCell.setLabel(false);
					currCell.setRoomCenter(false);
				}
				temp[j] = currCell;
			}
			grid[i] = temp;
		}
	}
	
	//Adds valid cells to current cell's adjacency list
	private void createAdjacencyList() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				BoardCell currentCell = grid[i][j];
				//Checks if cells are valid before adding to adjacency list
				if(currentCell.getInitial() == 'W') {
					addAdjacentCells(i, j, currentCell);
					
				} else if(currentCell.getSecretPassage() != '\0') {
					BoardCell secretPassageExit = roomMap.get(currentCell.getSecretPassage()).getCenterCell();
					BoardCell currentRoomCenter = roomMap.get(currentCell.getInitial()).getCenterCell();
					currentRoomCenter.addAdj(secretPassageExit);
				}
			}
		}
	}

	//Adds center cell of room and surrounding walkways to adjacency list if cell is a door
	private void addAdjacentCells(int i, int j, BoardCell currentCell) {
		if(i + 1 < numRows) {
			downAdjacency(i, currentCell, grid[i + 1][j]);
		}
		if(j + 1 < numCols) {
			rightAdjacency(j, currentCell, grid[i][j + 1]);
		}
		if(i - 1 >= 0) {
			aboveAdjacency(i, currentCell, grid[i - 1][j]);
		}
		if(j - 1 >= 0) {
			leftAdjacency(j, currentCell, grid[i][j - 1]);
		}
	}

	//For doorway pointing left, adds center cell of room. Otherwise, adds walkway to left.
	private void leftAdjacency(int j, BoardCell currentCell, BoardCell leftCell) {
		if(currentCell.isDoorDirection(DoorDirection.LEFT)) {
			BoardCell centerCell = roomMap.get(leftCell.getInitial()).getCenterCell();
			currentCell.addAdj(centerCell);
			centerCell.addAdj(currentCell);
		} else if(leftCell.getInitial() == 'W') {
			currentCell.addAdj(leftCell);
		}

	}

	//For doorway pointing up, adds center cell of room. Otherwise, adds walkway above.
	private void aboveAdjacency(int i, BoardCell currentCell, BoardCell aboveCell) {
		if(currentCell.isDoorDirection(DoorDirection.UP)) {
			BoardCell centerCell = roomMap.get(aboveCell.getInitial()).getCenterCell();
			currentCell.addAdj(centerCell);
			centerCell.addAdj(currentCell);
		} else if(aboveCell.getInitial() == 'W') {
			currentCell.addAdj(aboveCell);
		}

	}

	//For doorway pointing right, adds center cell of room. Otherwise, adds walkway to right.
	private void rightAdjacency(int j, BoardCell currentCell, BoardCell rightCell) {
		if(currentCell.isDoorDirection(DoorDirection.RIGHT)) {
			BoardCell centerCell = roomMap.get(rightCell.getInitial()).getCenterCell();
			currentCell.addAdj(centerCell);
			centerCell.addAdj(currentCell);
		} else if(rightCell.getInitial() == 'W') {
			currentCell.addAdj(rightCell);
		}
	}

	//For doorway pointing down, adds center cell of room. Otherwise, adds walkway below.
	private void downAdjacency(int i, BoardCell currentCell, BoardCell belowCell) {
		if(currentCell.isDoorDirection(DoorDirection.DOWN)) {
			BoardCell centerCell = roomMap.get(belowCell.getInitial()).getCenterCell();
			currentCell.addAdj(centerCell);
			centerCell.addAdj(currentCell);
		} else if(belowCell.getInitial() == 'W') {
			currentCell.addAdj(belowCell);
		}
	}
	// initializes the roomMap according to the setup config
	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		roomMap = new HashMap<>();
		File setupConfig = new File(setupConfigFile); 
		Scanner fin = new Scanner(setupConfig);
		String[] temp;
		while(fin.hasNext()) {
			temp = fin.nextLine().split(", ");
			if(!temp[0].contains("//")) {
				// Test that an exception is thrown for a config file with a room type
				// that is not Card or Other
				if(!temp[0].contains("Room") && !temp[0].contains("Space")) {
					throw new BadConfigFormatException();
				}
				roomMap.put(temp[2].charAt(0), new Room(temp[1]));
			}
		}
	}
	
	// Test that an exception is thrown for a config file that does not
	// have the same number of columns for each row
	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException {	
		stringGrid = new ArrayList<>();
		File layoutConfig = new File(layoutConfigFile);
		Scanner fin = new Scanner(layoutConfig);
		String[] temp;
		int prev = 0;
		//Splits each line by commas and adds each cell to string grid
		//Counts rows and columns of input
		while(fin.hasNext()) {
			temp = fin.nextLine().split(",");
			stringGrid.add(temp);
			prev = numCols;
			numCols = temp.length;
			if(numCols != prev && numRows > 0) {
				throw new BadConfigFormatException();
			}
			numRows++;
		}

		// Test that an exception is thrown for a config file that specifies
		// a room that is not in the legend. See first test for other important
		// comments.
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if(!roomMap.containsKey(stringGrid.get(i)[j].charAt(0))) {
					throw new BadConfigFormatException();
				}
			}
		}
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
		return grid[row][col];
	}

	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	public Room getRoom(char c) {
		return roomMap.get(c);
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numCols;
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		return getCell(i, j).getAdjList();
	}
}
