package tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;

public class ComputerAITest {

	private static Board board;
	private static ComputerPlayer testPlayer;
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
	private static ComputerPlayer noRoomPlayer;
	private static ComputerPlayer oneRoomPlayer;

	@BeforeAll
	public static void setup() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
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
		// Initialize a player to test with
		testPlayer = new ComputerPlayer("testPlayer", Color.blue, 2, 1, weapons, persons, rooms);
		noRoomPlayer = new ComputerPlayer("testPlayer", Color.blue, 9, 0, weapons, persons, rooms);
		oneRoomPlayer = new ComputerPlayer("testPlayer", Color.blue, 9, 9, weapons, persons, rooms);
	}

	@Test
	public void testSelectTargets() {
		board.calcTargets(board.getCell(noRoomPlayer.getRow(), noRoomPlayer.getCol()), 1);
		Set<BoardCell> noRoomTargets = board.getTargets();
		
		// Assert that chosen rooms are random
		int up = 0;
		int down = 0;
		int right = 0;
		for(int i = 0; i < 20; i++) {
			if (board.getCell(9, 1)== noRoomPlayer.selectTargets(noRoomTargets)) {
				right++;
			}
			if (board.getCell(10, 0)== noRoomPlayer.selectTargets(noRoomTargets)) {
				down++;
			}
			if (board.getCell(8, 0)== noRoomPlayer.selectTargets(noRoomTargets)) {	
				up++;
			}
		}
		assertTrue(up > 2);
		assertTrue(down > 2);
		assertTrue(right > 2);
		
		Set<BoardCell> oneRoomTargets = new HashSet<>();
		BoardCell firstTarget = new BoardCell(0, 0);
		BoardCell secondTarget = new BoardCell(0, 0);
		BoardCell roomTarget = new BoardCell(0, 0);
		Card roomCard = new Card("Room", CardType.ROOM);
		roomTarget.setRoom(true);
		roomTarget.setInitial('R');
		oneRoomTargets.add(firstTarget);
		oneRoomTargets.add(secondTarget);
		oneRoomTargets.add(roomTarget);
		oneRoomPlayer.addUnseenRoomCard(roomCard);
		// Assert that if there is a room in the targets that has not been seen, return it as the target
		assertTrue(oneRoomPlayer.selectTargets(oneRoomTargets) == roomTarget);
		
		// Assert that if rooms are seen, then the choice is random between each target
		
		// remove every possible room from the unseen list
		oneRoomPlayer.addCard(roomCard);
		
		up = 0;
		down = 0;
		int room = 0;
		for(int i = 0; i < 25; i++) {
			if (firstTarget == oneRoomPlayer.selectTargets(oneRoomTargets)) {
				down++;
			}
			if (secondTarget == oneRoomPlayer.selectTargets(oneRoomTargets)) {	
				up++;
			}
			if (roomTarget == oneRoomPlayer.selectTargets(oneRoomTargets)) {
				room++;
			}
		}
		assertTrue(up > 2);
		assertTrue(down > 2);
		assertTrue(room > 2);

	}

	@Test
	public void testCreateSuggestion() {
		
		char computerPlayerRoom = board.getCell(testPlayer.getRow(), testPlayer.getCol()).getInitial();
		char suggestionRoom = testPlayer.createSuggestion(board.getRoom(computerPlayerRoom)).room.getName().charAt(0);
		assertEquals(suggestionRoom, computerPlayerRoom);

		int timesGun = 0;
		int timesNotGun = 0;
		int timesSpoon = 0;
		int timesDude = 0;
		int timesNotDude = 0;
		int timesOtherGuy = 0;

		for (int i = 0; i < 20; i++) {
			testPlayer = new ComputerPlayer("testPLayer", Color.blue, 2, 1, weapons, persons, rooms);
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
			for (int j = 0; j < 3; j++) {
				Solution suggestion = testPlayer.createSuggestion(board.getRoom(computerPlayerRoom));

				if(j < 2) {
					testPlayer.removeSeen(suggestion.weapon);
					testPlayer.removeSeen(suggestion.person);
				}

				if(j >= 2) {
					switch (suggestion.weapon.getName()) {
					case "Gun":
						timesGun++;
						break;
					case "Not Gun":
						timesNotGun++;
						break;
					case "Spoon":
						timesSpoon++;
						break;
					}

					switch (suggestion.person.getName()) {
					case "Dude":
						timesDude++;
						break;
					case "Not Dude":
						timesNotDude++;
						break;
					case "Other Guy":
						timesOtherGuy++;
						break;
					}

					assertTrue(testPlayer.getUnseenWeapons().contains(suggestion.weapon));
					assertTrue(testPlayer.getUnseenPersons().contains(suggestion.person));
				}
			}
		}

		assertTrue(timesGun > 1);
		assertTrue(timesNotGun > 1);
		assertTrue(timesSpoon > 1);
		assertTrue(timesDude > 1);
		assertTrue(timesNotDude > 1);
		assertTrue(timesOtherGuy > 1);
	}
}
