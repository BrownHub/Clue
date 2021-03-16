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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				String currCellInfo = stringGrid.get(i)[j];
				currCell.setInitial(currCellInfo.charAt(0));
				if(roomMap.containsKey(currCellInfo.charAt(0)) && currCellInfo.charAt(0) != 'X' && currCellInfo.charAt(0) != 'W') {
					currCell.setRoom(true);
				} else {
					currCell.setRoom(false);
				}

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
						roomMap.get(currCellInfo.charAt(0)).setLabelCell(currCell);
						currCell.setRoomCenter(false);
						break;
					case '*':
						currCell.setDoorway(false);
						currCell.setDoorDirection(DoorDirection.NONE);
						currCell.setLabel(false);
						currCell.setRoomCenter(true);
						roomMap.get(currCellInfo.charAt(0)).setCenterCell(currCell);
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

		// Create adjacency lists
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				BoardCell current_cell = grid[i][j];
				if(current_cell.getInitial() == 'W') {
					BoardCell below_cell = grid[i + 1][j];
					BoardCell right_cell = grid[i][j + 1];
					BoardCell above_cell = grid[i - 1][j];
					BoardCell left_cell = grid[i][j - 1];
					if(current_cell.isDoorway()) {
						if(i + 1 < numRows) {
							if(current_cell.getDoorDirection() == DoorDirection.DOWN) {
								current_cell.addAdj(roomMap.get(below_cell.getInitial()).getCenterCell());
								roomMap.get(below_cell.getInitial()).getCenterCell().addAdj(current_cell);
							} else if(below_cell.getInitial() == 'W') {
								current_cell.addAdj(below_cell);
							}
						}
						if(j + 1 < numCols) {
							if(current_cell.getDoorDirection() == DoorDirection.RIGHT) {
								current_cell.addAdj(roomMap.get(right_cell.getInitial()).getCenterCell());
								roomMap.get(right_cell.getInitial()).getCenterCell().addAdj(current_cell);
							} else if(right_cell.getInitial() == 'W') {
								current_cell.addAdj(right_cell);
							}
						}
						if(i - 1 >= 0) {
							if(current_cell.getDoorDirection() == DoorDirection.UP) {
								current_cell.addAdj(roomMap.get(above_cell.getInitial()).getCenterCell());
								roomMap.get(above_cell.getInitial()).getCenterCell().addAdj(current_cell);
							} else if(above_cell.getInitial() == 'W') {
								current_cell.addAdj(above_cell);
							}
						}
						if(j - 1 >= 0) {
							if(current_cell.getDoorDirection() == DoorDirection.LEFT) {
								current_cell.addAdj(roomMap.get(left_cell.getInitial()).getCenterCell());
								roomMap.get(left_cell.getInitial()).getCenterCell().addAdj(current_cell);
							} else if(left_cell.getInitial() == 'W') {
								current_cell.addAdj(left_cell);
							}
						}
					} else {
						if(i + 1 < numRows && below_cell.getInitial() == 'W') {
							current_cell.addAdj(below_cell);
						}
						if(j + 1 < numCols && right_cell.getInitial() == 'W') {
							current_cell.addAdj(right_cell);
						}
						if(i - 1 >= 0 && above_cell.getInitial() == 'W') {
							current_cell.addAdj(above_cell);
						}
						if(j - 1 >= 0 && left_cell.getInitial() == 'W') {
							current_cell.addAdj(left_cell);
						}
					}
				} else if(current_cell.getSecretPassage() != '\0') {
					BoardCell secretPassageExit = roomMap.get(current_cell.getSecretPassage()).getCenterCell();
					BoardCell currentRoomCenter = roomMap.get(current_cell.getInitial()).getCenterCell();
					currentRoomCenter.addAdj(secretPassageExit);
				}
			}
		}

		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();

	}

	// initializes the roomMap according to the setup config
	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		roomMap = new HashMap();
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
		stringGrid = new ArrayList();
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
