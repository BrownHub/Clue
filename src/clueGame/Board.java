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
				// member variables
				BoardCell currCell = new BoardCell(i, j);
				currCell.setInitial(stringGrid.get(i)[j].charAt(0));
				// isRoom
				if(roomMap.containsKey(stringGrid.get(i)[j].charAt(0)) && stringGrid.get(i)[j].charAt(0) != 'X' && stringGrid.get(i)[j].charAt(0) != 'W') {
					currCell.setRoom(true);
				} else {
					currCell.setRoom(false);
				}

				if(stringGrid.get(i)[j].contains("v")) {
					currCell.setDoorway(true);
					currCell.setDoorDirection(DoorDirection.DOWN);

				} else if(stringGrid.get(i)[j].contains("<")) {
					currCell.setDoorway(true);
					currCell.setDoorDirection(DoorDirection.LEFT);

				} else if(stringGrid.get(i)[j].contains(">")) {
					currCell.setDoorway(true);
					currCell.setDoorDirection(DoorDirection.RIGHT);

				} else if(stringGrid.get(i)[j].contains("^")) {
					currCell.setDoorway(true);
					currCell.setDoorDirection(DoorDirection.UP);

				} else {
					currCell.setDoorway(false);
					currCell.setDoorDirection(DoorDirection.NONE);
				}

				if(stringGrid.get(i)[j].contains("#")) {
					currCell.setLabel(true);
					roomMap.get(stringGrid.get(i)[j].charAt(0)).setLabelCell(currCell);
				} else {
					currCell.setLabel(false);
				}

				if(stringGrid.get(i)[j].contains("*")) {
					currCell.setRoomCenter(true);
					roomMap.get(stringGrid.get(i)[j].charAt(0)).setCenterCell(currCell);
				} else {
					currCell.setRoomCenter(false);
				}

				if(stringGrid.get(i)[j].length() > 1) {
					if(!stringGrid.get(i)[j].contains("#") && 
							!stringGrid.get(i)[j].contains("*") && 
							!stringGrid.get(i)[j].contains("v") && 
							!stringGrid.get(i)[j].contains("<") && 
							!stringGrid.get(i)[j].contains(">") && 
							!stringGrid.get(i)[j].contains("^")) {
						currCell.setSecretPassage(stringGrid.get(i)[j].charAt(1));
					}
				}
				temp[j] = currCell;
			}
			grid[i] = temp;
		}

		// Create adjacency lists
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if(grid[i][j].getInitial() == 'W') {
					if(grid[i][j].isDoorway()) {
						if(i + 1 < numRows) {
							if(grid[i][j].getDoorDirection() == DoorDirection.DOWN) {
								grid[i][j].addAdj(roomMap.get(grid[i + 1][j].getInitial()).getCenterCell());
								roomMap.get(grid[i + 1][j].getInitial()).getCenterCell().addAdj(grid[i][j]);
							} else if(grid[i + 1][j].getInitial() == 'W') {
								grid[i][j].addAdj(grid[i + 1][j]);
							}
						}
						if(j + 1 < numCols) {
							if(grid[i][j].getDoorDirection() == DoorDirection.RIGHT) {
								grid[i][j].addAdj(roomMap.get(grid[i][j + 1].getInitial()).getCenterCell());
								roomMap.get(grid[i][j + 1].getInitial()).getCenterCell().addAdj(grid[i][j]);
							} else if(grid[i][j + 1].getInitial() == 'W') {
								grid[i][j].addAdj(grid[i][j + 1]);
							}
						}
						if(i - 1 >= 0) {
							if(grid[i][j].getDoorDirection() == DoorDirection.UP) {
								grid[i][j].addAdj(roomMap.get(grid[i - 1][j].getInitial()).getCenterCell());
								roomMap.get(grid[i - 1][j].getInitial()).getCenterCell().addAdj(grid[i][j]);
							} else if(grid[i - 1][j].getInitial() == 'W') {
								grid[i][j].addAdj(grid[i - 1][j]);
							}
						}
						if(j - 1 >= 0) {
							if(grid[i][j].getDoorDirection() == DoorDirection.LEFT) {
								grid[i][j].addAdj(roomMap.get(grid[i][j - 1].getInitial()).getCenterCell());
								roomMap.get(grid[i][j - 1].getInitial()).getCenterCell().addAdj(grid[i][j]);
							} else if(grid[i][j - 1].getInitial() == 'W') {
								grid[i][j].addAdj(grid[i][j - 1]);
							}
						}
					} else {
						if(i + 1 < numRows && grid[i + 1][j].getInitial() == 'W') {
							grid[i][j].addAdj(grid[i + 1][j]);
						}
						if(j + 1 < numCols && grid[i][j + 1].getInitial() == 'W') {
							grid[i][j].addAdj(grid[i][j + 1]);
						}
						if(i - 1 >= 0 && grid[i - 1][j].getInitial() == 'W') {
							grid[i][j].addAdj(grid[i - 1][j]);
						}
						if(j - 1 >= 0 && grid[i][j - 1].getInitial() == 'W') {
							grid[i][j].addAdj(grid[i][j - 1]);
						}
					}
				} else if(grid[i][j].getSecretPassage() != '\0') {
					roomMap.get(grid[i][j].getInitial()).getCenterCell().addAdj(roomMap.get(grid[i][j].getSecretPassage()).getCenterCell());
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
