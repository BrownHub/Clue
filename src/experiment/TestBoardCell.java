package experiment;

import java.util.Set;

public class TestBoardCell {
	// member variables
	private Set<TestBoardCell> adjList;
	private boolean isRoom;
	private boolean isOccupied;
	
	// constructor
	public TestBoardCell(int row, int column) {
		super();
	}
	
	// Getter and setter functions
	public void addAdjacency(TestBoardCell cell) {
		adjList.add(cell);
	}
	
	public Set<TestBoardCell> getAdjList() {
<<<<<<< HEAD
		return null;	// IMPLEMENT
=======
		return adjList;
>>>>>>> ad9463ed2840d5962f3a720a64fa81a8d697b587
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
<<<<<<< HEAD
	
=======
>>>>>>> ad9463ed2840d5962f3a720a64fa81a8d697b587
	public boolean getOccupied() {
		return isOccupied;
	}
}
