package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;


public class GameSolutionTest {
	
	private static Board board;
	private static Set<Card> weapons;
	private static Set<Card> persons;
	private static Set<Card> rooms;
	private static Card weapon1;
	private static Card weapon2;
	private static Card weapon3;
	private static Card room1;
	private static Card room2;
	private static Card room3;
	private static Card person1;
	private static Card person2;
	private static Card person3;

	@BeforeAll
	public static void setup() {
		// Get an instance of the board
		board = Board.getInstance();

		// load ClueLayout.csv
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");

		// initialize the board
		board.initialize();
		//Create cards to test with
				weapon1 = new Card("Gun", CardType.WEAPON);
				weapon2 = new Card("Not Gun", CardType.WEAPON);
				weapon3 = new Card("Spoon", CardType.WEAPON);
				room1 = new Card("House", CardType.ROOM);
				room2 = new Card("Not House", CardType.ROOM);
				room3 = new Card("Apartment", CardType.ROOM);
				person1 = new Card("Dude", CardType.PERSON);
				person2 = new Card("Not Dude", CardType.PERSON);
				person3 = new Card("Other Guy", CardType.PERSON);
				//Add cards to unseen sets
				weapons = new HashSet();
				persons = new HashSet();
				rooms = new HashSet();
				weapons.add(weapon1);
				weapons.add(weapon2);
				weapons.add(weapon3);
				rooms.add(room1);
				rooms.add(room2);
				rooms.add(room3);
				persons.add(person1);
				persons.add(person2);
				persons.add(person3);
	}
	
	@Test
	public void testCheckAccusation() {
		
		// Get a correct solution
		Solution correctAnswer = board.getTheAnswer();
		Set<Card> deck = board.getDeckWithoutSolution();
		
		// get an incorrect solution
		Solution incorrectAnswer = new Solution(board.getRandomCard(deck), board.getRandomCard(deck), board.getRandomCard(deck));
		
		// check that the correct accusation is true
		assertTrue(board.checkAccusation(correctAnswer));
		
		// check that the incorrect accusation is false
		assertFalse(board.checkAccusation(incorrectAnswer));
	}
	
	@Test
	public void testDisproveSuggestion() {
		ComputerPlayer playerA = new ComputerPlayer("playerA", Color.BLUE, 0, 0, weapons, persons, rooms);
		
		// Set test solutions
		Solution aSolution = new Solution(person1, room2, weapon2); // one matching card
		Solution bSolution = new Solution(person1, room1, weapon1); // all matching cards
		Solution cSolution = new Solution(person2, room2, weapon2); // no matching cards
		
		// give playerA three cards
		playerA.addCard(weapon1);
		playerA.addCard(room1);
		playerA.addCard(person1);
		
		// Shows that playerA returns a card that matches
		assertTrue(playerA.disproveSuggestion(aSolution) == person1);
		
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
		// Create three players, 
		ComputerPlayer playerA = new ComputerPlayer("playerA", Color.BLUE, 0, 0, weapons, persons, rooms);
		ComputerPlayer playerB = new ComputerPlayer("playerB", Color.GREEN, 1, 1, weapons, persons, rooms);
		HumanPlayer playerC = new HumanPlayer("playerC", Color.BLACK, 2, 2);
		ComputerPlayer playerD = new ComputerPlayer("playerD", Color.BLACK, 3, 3);

		ArrayList<Player> playerList = new ArrayList<>();
		
		// Create three solutions
		Solution aSolution = new Solution(person1, room1, weapon1);
		Solution bSolution = new Solution(person3, room3, weapon3);
		
		// Give playerA cards that they could use to make a suggestion
		playerA.addCard(weapon1);
		playerA.addCard(room1);
		playerA.addCard(person1);
		playerList.add(playerA);
		
		playerB.addCard(weapon2);
		playerB.addCard(room2);
		playerB.addCard(person2);
		playerList.add(playerB);
		
		// Assert that if only the accusing player can disprove, it returns null
		assertTrue(board.handleSuggestion(playerA, aSolution, playerList) == null);
		
		// Assert that if no one can disprove, it returns null
		assertTrue(board.handleSuggestion(playerA, bSolution, playerList) == null);

		playerC.addCard(weapon1);
		playerC.addCard(room2);
		playerC.addCard(person2);
		playerList.add(playerC);
		// Assert that a suggestion that only a human can disprove returns answer
		assertTrue(board.handleSuggestion(playerA, aSolution, playerList) == weapon1);
		playerD.addCard(weapon2);
		playerD.addCard(room1);
		playerD.addCard(person2);
		playerList.add(playerD);
		// Assert that a suggestion that two players can disprove returns the next player in order
		// It will return weapon1 because playerC can answer weapon1 before playerD can answer room1
		assertTrue(board.handleSuggestion(playerA, aSolution, playerList) == weapon1);
		
		
	}
}