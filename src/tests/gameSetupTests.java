package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.awt.Color;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;

public class gameSetupTests {
	private static Board board;
	private static Set<String> playerNames = new HashSet<>(Arrays.asList("Tony", "Marco", "Mario", "Anna", "Rosa", "Laura"));
	private static Set<Color> playerColors = new HashSet<>(Arrays.asList(Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED, Color.PINK));
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	@Test
	public void testLoadPeople() {
		// Check that each person loaded in properly - implemented
		// Each has proper Name, Color, and Human/Computer - implemented
		// 6 player objects, 5 Computer, 1 Human - implemented
		// Each has a different name and color - implemented
		Set<Player> playerList = board.getPlayerSet();
		
		int humanCount = 0;
		int computerCount = 0;
		Set<Color> checkUniqueColors = new HashSet<>();
		Set<String> checkUniqueNames = new HashSet<>();
		
		for(Player currPlayer : playerList) {
			
			if(currPlayer instanceof HumanPlayer) {
				humanCount++;
			} else if (currPlayer instanceof ComputerPlayer) {
				computerCount++;
			}
			
			String currName = currPlayer.getName();
			assertTrue(playerNames.contains(currName));
			assertFalse(checkUniqueNames.contains(currName));
			checkUniqueNames.add(currName);

			Color currColor = currPlayer.getColor();
			assertTrue(playerColors.contains(currColor));
			assertFalse(checkUniqueColors.contains(currColor));			
			checkUniqueColors.add(currColor);
		}
		
		assertTrue(humanCount == 1);
		assertTrue(computerCount == 5);
	}
	
	@Test
	public void testPlayerLocations() {
		// Check that each person starts at a valid location on the board
		// check that each player has a unique location on the board
		Set<Player> playerList = board.getPlayerSet();
		
		Set<BoardCell> checkUniqueLocations = new HashSet<>();
		int currRow;
		int currCol;
		BoardCell currCell;
		for(Player currPlayer : playerList) {
			currRow = currPlayer.getRow();
			currCol = currPlayer.getCol();
			currCell = board.getCell(currRow, currCol);
			
			assertTrue(currCell.getInitial() == 'W');
			
			assertFalse(checkUniqueLocations.contains(currCell));
			
			checkUniqueLocations.add(currCell);			
		}
	}
	
	@Test
	public void testCards() {
		// Check that the deck has been created properly - implemented
		// Check that the solution deck is set - implemented
		// Check that the players cards are from the deck - implemented
		// Check that the players cards are unique - implemented
		// Check that the players cards do not contain the solution cards - implemented
		// 9 rooms, 6 players, 6 weapons	- implemented
		// Deck size could change, so use >= 3	- implemented
		
		Set<Player> playerList = board.getPlayerSet();
		Set<Card> deck = board.getDeck();
		
		Set<Card> uniqueCards = new HashSet<>();
		
		Set<String> cardTypes = new HashSet<>();
		cardTypes.add("Room");
		cardTypes.add("Player");
		cardTypes.add("Weapon");
		
		assertTrue(deck.size() >= 21);
		
		for(Card currCard : deck) {
			assertTrue(cardTypes.contains(currCard.getType()));
			assertFalse(uniqueCards.contains(currCard));
			uniqueCards.add(currCard);
		}
		
		Set<Card> solutionDeck = board.getSolutionDeck();
		assertTrue(solutionDeck.size() == 3);
		
		for(Player currPlayer : playerList) {
			Set<Card> currHand = currPlayer.getHand();
			
			assertTrue(currHand.size() >= 3);
			
			for(Card currCard : currHand) {
				assertTrue(deck.contains(currCard));
				assertFalse(solutionDeck.contains(currCard));
			}
			
		}
	}
}
