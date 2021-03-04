package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTests {
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Create board
		board = Board.getInstance();
		// set the file names
		board.setConfigFiles("ClueLayout306.csv", "ClueSetup306.txt");		
		// Initialize 
		board.initialize();
	}
	
	// Ensure that player does not move around within room
	// These cells are Teal on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms() {
		//Doorways access room centers
		Set<BoardCell> adjList= board.getAdjList(4, 1);
		assertEquals(4, adjList.size());
		assertTrue(adjList.contains(board.getCell(4, 0)));
		assertTrue(adjList.contains(board.getCell(2, 1)));
		
		//Rooms access doorways and secret passages
		adjList = board.getAdjList(23, 0);
		assertEquals(3, adjList.size());
		assertTrue(adjList.contains(board.getCell(22, 2)));
		assertTrue(adjList.contains(board.getCell(17, 0)));
		assertTrue(adjList.contains(board.getCell(21, 18)));
		
		//Unused cells aren't added
		adjList = board.getAdjList(15, 3);
		assertEquals(3, adjList.size());
		assertFalse(adjList.contains(board.getCell(15, 4)));
		
		//Room cells aren't added
		adjList = board.getAdjList(10, 15);
		assertEquals(2, adjList.size());
	}
	
	
	@Test
	public void testTargets() {
		board.calcTargets(board.getCell(11, 1), 2);
		Set<BoardCell> targetList= board.getTargets();
		assertEquals(7, targetList.size());
		assertTrue(targetList.contains(board.getCell(10, 0)));
		assertTrue(targetList.contains(board.getCell(11, 3)));
	}
}
