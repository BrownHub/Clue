package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;


public class GameSolutionTest {
	
	private static Board board;
	private static Card weapon;
	private static Card weapon2;
	private static Card room;
	private static Card room2;
	private static Card person;
	private static Card person2;

	@BeforeAll
	public static void setup() {
		// Get an instance of the board
		board = Board.getInstance();

		// load ClueLayout.csv
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");

		// initialize the board
		board.initialize();
		weapon = new Card("Gun", CardType.WEAPON);
		weapon2 = new Card("Not Gun", CardType.WEAPON);
		room = new Card("House", CardType.ROOM);
		room2 = new Card("Not House", CardType.ROOM);
		person = new Card("Dude", CardType.PERSON);
		person2 = new Card("Not Dude", CardType.PERSON);
	}
	
	@Test
	public void testCheckAccusation() {
		Solution correctAnswer = board.getTheAnswer();
		Set<Card> deck = board.getDeckWithoutSolution();
		Solution incorrectAnswer = new Solution(board.getRandomCard(deck), board.getRandomCard(deck), board.getRandomCard(deck));
		assertTrue(board.checkAccusation(correctAnswer));
		assertFalse(board.checkAccusation(incorrectAnswer));
	}
	
	@Test
	public void testDisproveSuggestion() {
		ComputerPlayer playerA = new ComputerPlayer("playerA", Color.BLUE, 0, 0, null, null, null);
		
		Solution aSolution = new Solution(person, room2, weapon2);
		Solution bSolution = new Solution(person, room, weapon);
		Solution cSolution = new Solution(person2, room2, weapon2);

		playerA.addCard(weapon);
		playerA.addCard(room);
		playerA.addCard(person);
		
		// Shows that playerA returns a card that matches
		assertTrue(playerA.disproveSuggestion(aSolution) == person);
		
		// Show that if the player has no matching cards it returns null
		assertTrue(playerA.disproveSuggestion(cSolution) == null);
		
		// Show that if more than one card matches, it will return a random match
		int numPersonMatches = 0;
		int numRoomMatches = 0;
		int numWeaponMatches = 0;
		Card temp;
		for(int i = 0; i < 20; i++) {
			temp = playerA.disproveSuggestion(bSolution);
			if(temp.getType() == CardType.WEAPON) {
				numWeaponMatches++;
			}
			if(temp.getType() == CardType.PERSON) {
				numPersonMatches++;
			}
			if(temp.getType() == CardType.ROOM) {
				numRoomMatches++;
			}
		}
		assertTrue(numRoomMatches >= 3);
		assertTrue(numPersonMatches >= 3);
		assertTrue(numWeaponMatches >= 3);
	}
	
	@Test
	public void testHandleSuggestion() {
		ComputerPlayer playerA = new ComputerPlayer("playerA", Color.BLUE, 0, 0, null, null, null);
		HumanPlayer playerB = new HumanPlayer("playerB", Color.GREEN, 1, 1);
		ComputerPlayer playerC = new ComputerPlayer("playerC", Color.PINK, 2, 2, null, null, null);
		ArrayList<Player> playerList = new ArrayList<>();
		
		Solution aSolution = new Solution(person, room2, weapon2);
		Solution bSolution = new Solution(person, room, weapon);
		Solution cSolution = new Solution(person2, room2, weapon2);
		
		playerA.addCard(weapon);
		playerA.addCard(room);
		playerA.addCard(person);
		playerList.add(playerA);
		
		assertTrue(board.handleSuggestion(playerB, cSolution, playerList) == null);

		playerB.addCard(weapon2);
		playerB.addCard(room2);
		playerB.addCard(person2);
		playerList.add(playerB);
		
		assertTrue(board.handleSuggestion(playerC, aSolution, playerList) == person);
		assertTrue(board.handleSuggestion(playerB, bSolution, playerList) == null);
		
		
	}
}