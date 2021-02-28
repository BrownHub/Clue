package tests;


import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;


public class BoardTestsExp {
	private TestBoard board;
	
	@BeforeEach
	public void setUp() {
		board = new TestBoard();
	}
	
	@Test
	public void testAdjacency() {
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1,  0)));
		Assert.assertTrue(testList.contains(board.getCell(0,  1)));
		Assert.assertEquals(2, testList.size());
		
		cell = board.getCell(3, 3);
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(3,  2)));
		Assert.assertTrue(testList.contains(board.getCell(2,  3)));
		Assert.assertEquals(2, testList.size());
		
		cell = board.getCell(1, 3);
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0,  3)));
		Assert.assertTrue(testList.contains(board.getCell(1,  2)));
		Assert.assertTrue(testList.contains(board.getCell(2,  3)));
		Assert.assertEquals(3, testList.size());
		
		cell = board.getCell(1, 0);
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0,  0)));
		Assert.assertTrue(testList.contains(board.getCell(1,  1)));
		Assert.assertTrue(testList.contains(board.getCell(2,  0)));
		Assert.assertEquals(3, testList.size());
		
		cell = board.getCell(1, 1);
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0,  1)));
		Assert.assertTrue(testList.contains(board.getCell(1,  0)));
		Assert.assertTrue(testList.contains(board.getCell(1,  2)));
		Assert.assertTrue(testList.contains(board.getCell(2,  1)));
		Assert.assertEquals(4, testList.size());		
	}
	
	@Test
	public void testTargets() {
		// test roll of 1 at corner
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 1);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		
		// test roll of 6 in center
		cell = board.getCell(1, 1);
		board.calcTargets(cell, 6);
		targets = board.getTargets();
		Assert.assertEquals(7, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 0)));
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
		
		// test roll of 2 at 2, 2
		cell = board.getCell(2, 2);
		board.calcTargets(cell, 2);
		targets = board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
		// test roll of 3 in center
		cell = board.getCell(0, 1);
		board.calcTargets(cell, 3);
		targets = board.getTargets();
		Assert.assertEquals(7, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 0)));
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		
		// test roll of 4 at 2, 2, to show they are different
		cell = board.getCell(2, 2);
		board.calcTargets(cell, 4);
		targets = board.getTargets();
		Assert.assertEquals(7, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 0)));
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));

	}
	
	@Test
	public void testUnavailableCells() {
		// get results before adding occupied and rooms
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(6,  targets.size());
		Assert.assertTrue(targets.contains(board.getCell(0, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		
		// get results post adding occupied and rooms
		board.getCell(0, 2).setOccupied(true);
		board.getCell(1, 2).setRoom(true);
		cell = board.getCell(0, 3);
		board.calcTargets(cell, 3);
		targets = board.getTargets();
		Assert.assertEquals(2,  targets.size());
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
		
	}
}
