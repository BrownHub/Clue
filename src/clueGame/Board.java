package clueGame;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class Board {

	// member variables
	private BoardCell[][] grid;
	private ArrayList<String[]> stringGrid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private Set<Player> players;
	private Set<Card> deck;
	private Set<Card> deckWithoutSolution;
	private Set<Card> removeDeck;
	private Set<Card> playerDeck;
	private Set<Card> weaponDeck;
	private Set<Card> roomDeck;
	private Solution theAnswer;
	private HumanPlayer thePlayer;
	private static Board theInstance;

	// Size of the board
	private int numRows;
	private int numCols;

	// constructor which sets up the board
	public Board() {
		super();
	}

	// this method returns the only Board
	public static Board getInstance() {
		theInstance = new Board();

		return theInstance;
	}

	// recursive function which calculates the possible cells the player can move to
	public void calcTargets(BoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}

	// recursive function called in calcTargets which determines targets
	private void findAllTargets(BoardCell startCell, int pathlength) {		
		for(BoardCell adjCell : startCell.getAdjList()) {
			// add if its not occupied, is not a room, and is not already visited
			if(!adjCell.getOccupied() && !adjCell.isRoom() && !visited.contains(adjCell)) {
				visited.add(adjCell);
				if(pathlength == 1) {	// base case: the adjacent cells are the targets
					targets.add(adjCell);
				} else {				// recursive case, the targets are contained by the next cell grouping
					findAllTargets(adjCell, pathlength - 1);
				}
				visited.remove(adjCell);
			}
			
			// add room center if moving into a room
			if(adjCell.isRoomCenter() && !visited.contains(adjCell)) {
				targets.add(adjCell);
			}
		}
	}
	

	// load files to create board and adjacency lists
	public void initialize() {
		try {		// try/catch for FileNotFound and BadConfigFormat Exceptions 
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		removeDeck = new HashSet<>();
		for(Card c : deck) {
			removeDeck.add(c);
		}
		if(deck.size() > 9) {
			createTheAnswer();
		
			createPlayerHands();
		}
		
		createGrid();

		createAdjacencyList();

		targets = new HashSet<>();
		visited = new HashSet<>();

	}

	//Uses a string grid to create cells and add them to the game board
	private void createGrid() {
		BoardCell[] temp;	// temporary array will be added to the grid each iteration
		grid = new BoardCell[numRows][numCols];		// allocate memory for the grid
		for(int i = 0; i < numRows; i++) {
			temp = new BoardCell[numRows];		// allocate memory for the temporary array
			for(int j = 0; j < numCols; j++) {
				// Create a temporary cell, set its attributes, and add it to the temporary list
				BoardCell currCell = new BoardCell(i, j);
				String currCellInfo = stringGrid.get(i)[j];
				setAttributes(currCell, currCellInfo);
				temp[j] = currCell;
			}
			grid[i] = temp;
		}
	}
	
	//  the solution deck
	private void createTheAnswer() {
		Card solutionWeapon;
		Card solutionPlayer;
		Card solutionRoom;
		solutionWeapon = getRandomCard(weaponDeck);
		solutionPlayer = getRandomCard(playerDeck);
		solutionRoom = getRandomCard(roomDeck);
		
		theAnswer = new Solution(solutionPlayer, solutionRoom, solutionWeapon);
		removeDeck.remove(solutionWeapon);
		removeDeck.remove(solutionPlayer);
		removeDeck.remove(solutionRoom);
		
		deckWithoutSolution = new HashSet<>();
		for(Card c : removeDeck) {
			deckWithoutSolution.add(c);
		}
	}
	
	// create the player's hands
	private void createPlayerHands() {
		Card randCard;
		for(Player currPlayer : players) {
			for(int i = 0; i < 3; i++) {
				randCard = getRandomCard(removeDeck);
				removeDeck.remove(randCard);
				currPlayer.addCard(randCard);
			}
		}
	}
	public Card getRandomCard(Set<Card> subDeck) {
		int size = subDeck.size();
		int cardIndex = new Random().nextInt(size);
		int i = 0;
		for(Card currCard : subDeck) {
			if(i == cardIndex) {
				return currCard;
			}
			i++;
		}
		return null;
	}

	//Sets attributes of cell according to information from string grid cell
	private void setAttributes(BoardCell currCell, String currCellInfo) {
		currCell.setInitial(currCellInfo.charAt(0));
		currCell.setRoom(roomMap.containsKey(currCell.getInitial()) && currCell.getInitial() != 'X' && currCell.getInitial() != 'W');
		if(currCellInfo.length() > 1) { 
			switch (currCellInfo.charAt(1)) {
			case 'v':	// attributes of a doorway pointing down
				currCell.setDoorway(true);
				currCell.setDoorDirection(DoorDirection.DOWN);
				currCell.setLabel(false);
				currCell.setRoomCenter(false);
				break;
			case '<':	// attributes of a doorway pointing left
				currCell.setDoorway(true);
				currCell.setDoorDirection(DoorDirection.LEFT);
				currCell.setLabel(false);
				currCell.setRoomCenter(false);
				break;
			case '>':	// attributes of a doorway pointing right
				currCell.setDoorway(true);
				currCell.setDoorDirection(DoorDirection.RIGHT);
				currCell.setLabel(false);
				currCell.setRoomCenter(false);
				break;
			case '^':	// attributes of a doorway pointing up
				currCell.setDoorway(true);
				currCell.setDoorDirection(DoorDirection.UP);
				currCell.setLabel(false);
				currCell.setRoomCenter(false);
				break;
			case '#':	// attributes of a room label cell
				currCell.setDoorway(false);
				currCell.setDoorDirection(DoorDirection.NONE);
				currCell.setLabel(true);
				roomMap.get(currCell.getInitial()).setLabelCell(currCell);
				currCell.setRoomCenter(false);
				break;
			case '*':	// attributes of a room center cell
				currCell.setDoorway(false);
				currCell.setDoorDirection(DoorDirection.NONE);
				currCell.setLabel(false);
				currCell.setRoomCenter(true);
				roomMap.get(currCell.getInitial()).setCenterCell(currCell);
				break;
			default:	// attributes of a secret passage
				currCell.setDoorway(false);
				currCell.setDoorDirection(DoorDirection.NONE);
				currCell.setLabel(false);
				currCell.setRoomCenter(false);
				currCell.setSecretPassage(currCellInfo.charAt(1));
				break;
			}
		} else {	// attributes of a walkway/unused/room
			currCell.setDoorway(false);
			currCell.setDoorDirection(DoorDirection.NONE);
			currCell.setLabel(false);
			currCell.setRoomCenter(false);
		}
	}
	
	//Adds valid cells to current cell's adjacency list
	private void createAdjacencyList() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				BoardCell currentCell = grid[i][j];
				//Checks if cells are valid before adding to adjacency list
				if(currentCell.getInitial() == 'W') {
					addAdjacentCells(i, j, currentCell);
				} else if(currentCell.getSecretPassage() != '\0') {
					// adds the secret passage to the current room center
					BoardCell secretPassageExit = roomMap.get(currentCell.getSecretPassage()).getCenterCell();
					BoardCell currentRoomCenter = roomMap.get(currentCell.getInitial()).getCenterCell();
					currentRoomCenter.addAdj(secretPassageExit);
				}
			}
		}
	}

	//Adds center cell of room and surrounding walkways to adjacency list if cell is a door
	private void addAdjacentCells(int i, int j, BoardCell currentCell) {
		if(i + 1 < numRows) {	// checks if the cell below is valid
			downAdjacency(i, currentCell, grid[i + 1][j]);
		}
		if(j + 1 < numCols) {	// checks if the cell right is valid
			rightAdjacency(j, currentCell, grid[i][j + 1]);
		}
		if(i - 1 >= 0) {	// checks if the cell above is valid
			aboveAdjacency(i, currentCell, grid[i - 1][j]);
		}
		if(j - 1 >= 0) {	// checks if the cell left is valid
			leftAdjacency(j, currentCell, grid[i][j - 1]);
		}
	}

	//For doorway pointing left, adds center cell of room. Otherwise, adds walkway to left.
	private void leftAdjacency(int j, BoardCell currentCell, BoardCell leftCell) {
		if(currentCell.isDoorDirection(DoorDirection.LEFT)) {
			BoardCell centerCell = roomMap.get(leftCell.getInitial()).getCenterCell();
			currentCell.addAdj(centerCell);
			centerCell.addAdj(currentCell);
		} else if(leftCell.getInitial() == 'W') {
			currentCell.addAdj(leftCell);
		}

	}

	//For doorway pointing up, adds center cell of room. Otherwise, adds walkway above.
	private void aboveAdjacency(int i, BoardCell currentCell, BoardCell aboveCell) {
		if(currentCell.isDoorDirection(DoorDirection.UP)) {
			BoardCell centerCell = roomMap.get(aboveCell.getInitial()).getCenterCell();
			currentCell.addAdj(centerCell);
			centerCell.addAdj(currentCell);
		} else if(aboveCell.getInitial() == 'W') {
			currentCell.addAdj(aboveCell);
		}

	}

	//For doorway pointing right, adds center cell of room. Otherwise, adds walkway to right.
	private void rightAdjacency(int j, BoardCell currentCell, BoardCell rightCell) {
		if(currentCell.isDoorDirection(DoorDirection.RIGHT)) {
			BoardCell centerCell = roomMap.get(rightCell.getInitial()).getCenterCell();
			currentCell.addAdj(centerCell);
			centerCell.addAdj(currentCell);
		} else if(rightCell.getInitial() == 'W') {
			currentCell.addAdj(rightCell);
		}
	}

	//For doorway pointing down, adds center cell of room. Otherwise, adds walkway below.
	private void downAdjacency(int i, BoardCell currentCell, BoardCell belowCell) {
		if(currentCell.isDoorDirection(DoorDirection.DOWN)) {
			BoardCell centerCell = roomMap.get(belowCell.getInitial()).getCenterCell();
			currentCell.addAdj(centerCell);
			centerCell.addAdj(currentCell);
		} else if(belowCell.getInitial() == 'W') {
			currentCell.addAdj(belowCell);
		}
	}
	
	// initializes the roomMap and Deck according to the setup config
	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		roomMap = new HashMap<>();
		deck = new HashSet<>();
		roomDeck = new HashSet<>();
		playerDeck = new HashSet<>();
		weaponDeck = new HashSet<>();
		players = new HashSet<>();
		File setupConfig = new File(setupConfigFile); 
		Scanner fin = new Scanner(setupConfig);
		String[] temp;
		while(fin.hasNext()) {
			temp = fin.nextLine().split(", ");
			if(!temp[0].contains("//")) {
				// Test that an exception is thrown for a config file with a room type
				// that is not Card or Other
				Card currCard;
				CardType type;
				switch (temp[0]) {
				case "Room":
					type = CardType.ROOM;
					currCard = new Card(temp[1], type);
					deck.add(currCard);
					roomDeck.add(currCard);
				case "Space":
					roomMap.put(temp[2].charAt(0), new Room(temp[1]));
					break;
				case "Human":
					type = CardType.PERSON;
					currCard = new Card(temp[1], type);
					thePlayer = new HumanPlayer(temp[1], getColor(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]));
					players.add(thePlayer);
					deck.add(currCard);
					playerDeck.add(currCard);
					break;
				case "Computer":
					type = CardType.PERSON;
					currCard = new Card(temp[1], type);
					players.add(new ComputerPlayer(temp[1], getColor(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
					deck.add(currCard);
					playerDeck.add(currCard);
					break;
				case "Weapon":
					type = CardType.WEAPON;
					currCard = new Card(temp[1], type);
					deck.add(currCard);
					weaponDeck.add(currCard);
					break;
				default:
					throw new BadConfigFormatException();
				}
			}
		}
		
		for (Player player: players) {
			if (player instanceof ComputerPlayer) {
				((ComputerPlayer) player).setUnseenWeapons(weaponDeck);
				((ComputerPlayer) player).setUnseenPersons(playerDeck);
				((ComputerPlayer) player).setUnseenRooms(roomDeck);
			}
		}
	}
	
	private Color getColor(String color) {
		switch (color) {
		case "black":
		case "BLACK":
			return Color.black;
		case "blue":
		case "BLUE":
			return Color.blue;
		case "CYAN":
		case "cyan":
			return Color.cyan;
		case "darkGray":
		case "DARK_GRAY":
			return Color.darkGray;
		case "gray":
		case "GRAY":
			return Color.gray;
		case "GREEN":
		case "green":
			return Color.green;
		case "LIGHT_GRAY":
		case "lightGray":
			return Color.lightGray;
		case "magenta":
		case "MAGENTA":
			return Color.magenta;
		case "orange":
		case "ORANGE":
			return Color.orange;
		case "pink":
		case "PINK":
			return Color.pink;
		case "red":
		case "RED":
			return Color.red;
		case "white":
		case "WHITE":
			return Color.white;
		case "yellow":
		case "YELLOW":
			return Color.yellow;
		default:
			break;
		}
		return Color.black;
	}

	// Test that an exception is thrown for a config file that does not
	// have the same number of columns for each row
	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException {	
		stringGrid = new ArrayList<>();
		File layoutConfig = new File(layoutConfigFile);
		Scanner fin = new Scanner(layoutConfig);
		String[] temp;
		int prev = 0;
		//Splits each line by commas and adds each cell to string grid
		//Counts rows and columns of input
		while(fin.hasNext()) {
			temp = fin.nextLine().split(",");
			stringGrid.add(temp);
			prev = numCols;
			numCols = temp.length;
			if(numCols != prev && numRows > 0) {
				throw new BadConfigFormatException();
			}
			numRows++;
		}

		// Test that an exception is thrown for a config file that specifies
		// a room that is not in the legend. See first test for other important
		// comments.
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if(!roomMap.containsKey(stringGrid.get(i)[j].charAt(0))) {
					throw new BadConfigFormatException();
				}
			}
		}
	}
	
	// Checks if the accusation is equal to the answer
	public boolean checkAccusation(Solution userSol) {
		return userSol.isEqual(theAnswer);
	}
	
	
	public Card handleSuggestion(Player p, Solution aSolution, ArrayList<Player> playerList) {
		for (Player player : playerList) {
			if (player != p) {
				Card disprovingCard = player.disproveSuggestion(aSolution);
				if (disprovingCard != null) {
					return disprovingCard;
				}
			}
		}

		return null;
	}
	
	// getters for targets and cells
	public void setConfigFiles(String boardCSV, String boardSetup) {
		layoutConfigFile = "data\\" + boardCSV;
		setupConfigFile = "data\\" + boardSetup;
	}

	public Set<BoardCell> getTargets(){
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	public Room getRoom(char c) {
		return roomMap.get(c);
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numCols;
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		return getCell(i, j).getAdjList();
	}

	public Set<Player> getPlayerSet() {
		return players;
	}
	
	public Set<Card> getDeck() {
		return deck;
	}
	
	public Solution getTheAnswer() {
		return theAnswer;
	}
	
	public Set<Card> getDeckWithoutSolution() {
		return deckWithoutSolution;
	}
	
	public HumanPlayer getThePlayer() {
		return thePlayer;
	}
}
