package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	// member variables
	private Set<BoardCell> adjList;
	private boolean isRoom;
	private boolean isOccupied;
	private int row, col;
	private char initial;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	
	// constructor
	public BoardCell(int setRow, int setCol) {
		super();
		adjList = new HashSet<BoardCell>();
		row = setRow;
		col = setCol;
	}
	
	// Getter and setter functions
	public void addAdj(BoardCell adj) {
		adjList.add(adj);
	}
	
	public Set<BoardCell> getAdjList() {
		return adjList;
	}
	
	public void setRoom(boolean newRoom) {
		isRoom = newRoom;
	}
	
	public boolean isRoom() {
		return isRoom;
	}
	
	public void setOccupied(boolean newOccupied) {
		isOccupied = newOccupied;
	}

	public boolean getOccupied() {
		return isOccupied;
	}
}