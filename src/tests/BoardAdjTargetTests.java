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
		
		// Walkways with 4 adjacency rooms
		Set<BoardCell> adjList= board.getAdjList(8, 1);
		assertEquals(4, adjList.size());
		
		// Edge locations (One edge cannot be accessed)
		adjList= board.getAdjList(8, 0);
		assertEquals(3, adjList.size());
		
		adjList= board.getAdjList(6, 18);
		assertEquals(3, adjList.size());
		
		adjList= board.getAdjList(0, 6);
		assertEquals(2, adjList.size());
		
		//Doorways access room centers
		adjList= board.getAdjList(4, 1);
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
		
		// Rooms that are not centers should not have adjacent locations
		adjList = board.getAdjList(13, 5);
		assertEquals(0, adjList.size());
	}
	
	// test to ensure that targets are being handled properly
	// these are bright green
	@Test
	public void testTargets() {
		// test that targets are collected properly - walkways
		board.calcTargets(board.getCell(11, 1), 2);
		Set<BoardCell> targetList= board.getTargets();
		assertEquals(7, targetList.size());
		assertTrue(targetList.contains(board.getCell(10, 0)));
		assertTrue(targetList.contains(board.getCell(11, 3)));
		
		// Leaving a room(without secret passage)
		board.calcTargets(board.getCell(28, 9), 3);
		targetList = board.getTargets();
		assertEquals(8, targetList.size());
		assertTrue(targetList.contains(board.getCell(25, 5)));
		assertTrue(targetList.contains(board.getCell(25, 13)));

		// Leaving a room(with secret passage)
		board.calcTargets(board.getCell(19, 9), 2);
		targetList = board.getTargets();
		assertEquals(6, targetList.size());
		assertTrue(targetList.contains(board.getCell(16, 9)));
		assertTrue(targetList.contains(board.getCell(23, 9)));
		
		// Test that rooms are targets, and that if a room is hit, then it will not continue
		board.calcTargets(board.getCell(22, 16), 3);
		targetList = board.getTargets();
		assertEquals(11, targetList.size());
		assertTrue(targetList.contains(board.getCell(21, 18)));
		
		// Test that occupied cells are being handled correctly
		// Rooms that are occupied should still be able to be entered
		board.getCell(9,9).setOccupied(true);
		board.getCell(10,8).setOccupied(true);
		board.getCell(8,8).setOccupied(true);
		board.getCell(11,6).setOccupied(true);
		board.calcTargets(board.getCell(9, 8), 1);
		board.getCell(9,9).setOccupied(false);
		board.getCell(10,8).setOccupied(false);
		board.getCell(8,8).setOccupied(false);
		board.getCell(11,6).setOccupied(false);
		targetList = board.getTargets();
		assertEquals(1, targetList.size());
		assertTrue(targetList.contains(board.getCell(11, 6)));
		
		// Test that if there are no valid moves, then the player cannot move
		board.getCell(0,6).setOccupied(true);
		board.getCell(0, 11).setOccupied(true);
		board.calcTargets(board.getCell(1, 9), 1);
		board.getCell(0,6).setOccupied(false);
		board.getCell(0, 11).setOccupied(false);
		targetList = board.getTargets();
		assertEquals(0, targetList.size());
		
	}
}
