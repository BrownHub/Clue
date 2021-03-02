package tests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;


public class FileInitTests {
	// used to check if the board was loaded properly
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 29;
	public static final int NUM_COLUMNS = 18;
	public static final int NUM_DOORS = 16;

	private static Board board;

	@BeforeAll
	public static void setup() {
		// Get an instance of the board
		board = Board.getInstance();

		// load ClueLayout.csv
		board.loadConfigFiles("ClueLayout.csv");

		// load ClueSetup.txt
		board.loadSetupConfig("ClueSetup.txt");

		// initialize the board
		board.initialize();
	}
	@Test
	public void testRoomLabels() {
		// ensure that the names of the rooms are used
		assertEquals("Canal", board.getRoom('C').getName() );
		assertEquals("Pizza Place", board.getRoom('P').getName() );
		assertEquals("Shed", board.getRoom('S').getName() );
		assertEquals("Burger Place", board.getRoom('B').getName() );
		assertEquals("Walkway", board.getRoom('W').getName() );
	}
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	@Test
	public void FourDoorDirections() {
		BoardCell cell = board.getCell(4, 17);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(0, 6);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(0, 11);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(17, 0);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
	}
	
	// Test if the proper number of doors were initialized
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for(int row = 0; row < board.getNumRows(); row++) {
			for(int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if(cell.isDoorway()) {
					numDoors++;
				}
			}
		}
		assertEquals(NUM_DOORS, numDoors);
	}
	
	@Test
	public void testRooms() {
		// check if a room is working properly
		BoardCell cell = board.getCell(0, 0);
		Room room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Music Store");
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		// check if a room is a label cell
		cell = board.getCell(0, 9);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Alley");
		assertTrue(cell.isLabel());
		assertTrue(room.getLabelCell() == cell);
		
		// check if a room is a center cell
		cell = board.getCell(19, 9);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Pizza Place");
		assertTrue(cell.isRoomCenter());
		assertTrue(room.getCenterCell() == cell);
		
		// check if a cell is a secret passage
		cell = board.getCell(29, 0);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Left Apartments");
		assertTrue(cell.getSecretPassage() == 'K');
		
		// check if a cell is a walkway
		cell = board.getCell(4, 0);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Walkway");
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isLabel());
		
		// check if a cell is an unused space
		cell = board.getCell(2, 3);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Unused");
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isLabel());
	}
}