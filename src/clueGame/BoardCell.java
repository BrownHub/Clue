package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	// member variables
	private Set<BoardCell> adjList;
	private boolean isRoom;
	private boolean isOccupied;
<<<<<<< HEAD
	private boolean isDoorway;
	private int row, col;
	DoorDirection doorDirection;
=======
	private int row, col;
>>>>>>> dc48daf38ce1c3845eabcf315f557a6ab11c6471
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
<<<<<<< HEAD
	
	public boolean isDoorway() {
		return isDoorway;
	}
	
	public char getInitial() {
		return initial;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public char getSecretPassage() {
		return '\0';
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
=======
>>>>>>> dc48daf38ce1c3845eabcf315f557a6ab11c6471
}