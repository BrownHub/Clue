package experiment;

import java.util.ArrayList;
import java.util.Set;

public class TestBoard {
	
	// member variables
	private ArrayList<ArrayList<TestBoardCell>> board;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	
	// Size of the board
	private static int BOARD_ROWS_COLS = 4;
	
	// constructor which sets up the board
	public TestBoard() {
<<<<<<< HEAD
		super();
		// IMPLEMENT
		/*
=======
>>>>>>> ad9463ed2840d5962f3a720a64fa81a8d697b587
		for(int i = 0; i < BOARD_ROWS_COLS; i++) {	
			// Create a temporary list containing a new row
			ArrayList<TestBoardCell> temp = new ArrayList<TestBoardCell>();
			for(int j = 0; j < BOARD_ROWS_COLS; j++) {
				// Create a temporary cell, add it to the temporary list
				TestBoardCell currCell = new TestBoardCell(i, j);
				temp.add(currCell);
			}
			// add the temporary list to the board
			board.add(temp);
		}
<<<<<<< HEAD
		*/
=======
>>>>>>> ad9463ed2840d5962f3a720a64fa81a8d697b587
	}
	
	// recursive function which calculates the possible cells the player can move to
	public void calcTargets(TestBoardCell startCell, int pathlength) {
<<<<<<< HEAD
		// IMPLEMENT
		/*
=======
>>>>>>> ad9463ed2840d5962f3a720a64fa81a8d697b587
		for(TestBoardCell adjCell : startCell.getAdjList()) {
			// if you have already visited the cell, skip
			if(!visited.contains(adjCell)) {
				// add the cell to the visited list
				visited.add(adjCell);
				// base case: the adjacent cells are the targets
				if(pathlength == 1) {
					targets.add(adjCell);
				// recursive case, the targets are contained by the next cell grouping
				} else {
					calcTargets(adjCell, pathlength - 1);
				}
				// remove the cell from the visited list
				visited.remove(adjCell);
			}
		}
<<<<<<< HEAD
		*/
=======
>>>>>>> ad9463ed2840d5962f3a720a64fa81a8d697b587
	}
	
	// getters for targets and cells
	public Set<TestBoardCell> getTargets(){
<<<<<<< HEAD
		return null;
	}

	public TestBoardCell getCell(int row, int col) {
		return null;
=======
		return targets;
	}

	public TestBoardCell getCell(int row, int col) {
		return board.get(row).get(col);
>>>>>>> ad9463ed2840d5962f3a720a64fa81a8d697b587
	}
}
