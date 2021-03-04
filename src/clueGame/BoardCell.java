package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	// member variables
	private Set<BoardCell> adjList;
	private boolean isRoom;
	private boolean isOccupied;

	private boolean isDoorway;
	private int row, col;
	DoorDirection doorDirection;
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

	public void setDoorway(boolean t) {
		isDoorway = t;
	}
	
	public boolean isDoorway() {
		return isDoorway;
	}
	
	public char getInitial() {
		return initial;
	}
	
	public void setInitial(char currInitial) {
		initial = currInitial;
	}
	
	public void setLabel(boolean label) {
		roomLabel = label;
	}
	public boolean isLabel() {
		return roomLabel;
	}
	
	public void setRoomCenter(boolean center) {
		roomCenter = center;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public char getSecretPassage() {
		return secretPassage;
	}
	
	public void setDoorDirection(DoorDirection dir) {
		doorDirection = dir;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public void setSecretPassage(char c) {
		secretPassage = c;
	}

}