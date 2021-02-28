package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
	// member variables
	private Set<TestBoardCell> adjList;
	private boolean isRoom;
	private boolean isOccupied;
	private int row, col;
	
	// constructor
	public TestBoardCell(int row, int column) {
		super();
		adjList = new HashSet<TestBoardCell>();
	}
	
	// Getter and setter functions
	public void addAdjacency(TestBoardCell cell) {
		adjList.add(cell);
	}
	
	public Set<TestBoardCell> getAdjList() {
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
