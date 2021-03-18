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
	

	// initialize the board
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

	private void createGrid() {
		// Create a temporary array 
		BoardCell[] temp;
		grid = new BoardCell[numRows][numCols];
		for(int i = 0; i < numRows; i++) {
			// Create a new array in temp, allocate memory
			temp = new BoardCell[numRows];
			for(int j = 0; j < numCols; j++) {
				// Create a temporary cell, add it to the temporary list
				BoardCell currCell = new BoardCell(i, j);
				String currCellInfo = stringGrid.get(i)[j];
				currCell.setInitial(currCellInfo.charAt(0));
				currCell.setRoom(roomMap.containsKey(currCell.getInitial()) && currCell.getInitial() != 'X' && currCell.getInitial() != 'W');
				if(stringGrid.size() > 1) { 
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
	
	private void createAdjacencyList() {
		// Create adjacency lists
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				BoardCell currentCell = grid[i][j];
				if(currentCell.getInitial() == 'W') {
					BoardCell belowCell = grid[i + 1][j];
					BoardCell rightCell = grid[i][j + 1];
					BoardCell aboveCell = grid[i - 1][j];
					BoardCell leftCell = grid[i][j - 1];
					if(currentCell.isDoorway()) {
						downDoorAdjacency(i, currentCell, belowCell);
						rightDoorAdjacency(j, currentCell, rightCell);
						aboveDoorAdjacency(i, currentCell, aboveCell);
						leftDoorAdjacency(j, currentCell, leftCell);
					} else {
						addAdjacentCells(i, j, currentCell, belowCell, rightCell, aboveCell, leftCell);
					}
				} else if(currentCell.getSecretPassage() != '\0') {
					BoardCell secretPassageExit = roomMap.get(currentCell.getSecretPassage()).getCenterCell();
					BoardCell currentRoomCenter = roomMap.get(currentCell.getInitial()).getCenterCell();
					currentRoomCenter.addAdj(secretPassageExit);
				}
			}
		}
	}

	private void addAdjacentCells(int i, int j, BoardCell currentCell, BoardCell belowCell, BoardCell rightCell,
			BoardCell aboveCell, BoardCell leftCell) {
		if(i + 1 < numRows && belowCell.getInitial() == 'W') {
			currentCell.addAdj(belowCell);
		}
		if(j + 1 < numCols && rightCell.getInitial() == 'W') {
			currentCell.addAdj(rightCell);
		}
		if(i - 1 >= 0 && aboveCell.getInitial() == 'W') {
			currentCell.addAdj(aboveCell);
		}
		if(j - 1 >= 0 && leftCell.getInitial() == 'W') {
			currentCell.addAdj(leftCell);
		}
	}

	private void leftDoorAdjacency(int j, BoardCell currentCell, BoardCell leftCell) {
		if(j - 1 >= 0) {
			if(currentCell.isDoorDirection(DoorDirection.LEFT)) {
				BoardCell centerCell = roomMap.get(leftCell.getInitial()).getCenterCell();
				currentCell.addAdj(centerCell);
				centerCell.addAdj(currentCell);
			} else if(leftCell.getInitial() == 'W') {
				currentCell.addAdj(leftCell);
			}
		}
	}

	private void aboveDoorAdjacency(int i, BoardCell currentCell, BoardCell aboveCell) {
		if(i - 1 >= 0) {
			if(currentCell.isDoorDirection(DoorDirection.UP)) {
				BoardCell centerCell = roomMap.get(aboveCell.getInitial()).getCenterCell();
				currentCell.addAdj(centerCell);
				centerCell.addAdj(currentCell);
			} else if(aboveCell.getInitial() == 'W') {
				currentCell.addAdj(aboveCell);
			}
		}
	}

	private void rightDoorAdjacency(int j, BoardCell currentCell, BoardCell rightCell) {
		if(j + 1 < numCols) {
			if(currentCell.isDoorDirection(DoorDirection.RIGHT)) {
				BoardCell centerCell = roomMap.get(rightCell.getInitial()).getCenterCell();
				currentCell.addAdj(centerCell);
				centerCell.addAdj(currentCell);
			} else if(rightCell.getInitial() == 'W') {
				currentCell.addAdj(rightCell);
			}
		}
	}

	private void downDoorAdjacency(int i, BoardCell currentCell, BoardCell belowCell) {
		if(i + 1 < numRows) {
			if(currentCell.isDoorDirection(DoorDirection.DOWN)) {
				BoardCell centerCell = roomMap.get(belowCell.getInitial()).getCenterCell();
				currentCell.addAdj(centerCell);
				centerCell.addAdj(currentCell);
			} else if(belowCell.getInitial() == 'W') {
				currentCell.addAdj(belowCell);
			}
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

		for(int i = 0; i < numRows; i++) {
			// Test that an exception is thrown for a config file that specifies
			// a room that is not in the legend. See first test for other important
			// comments.
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
