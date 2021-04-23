package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ComputerPlayer extends Player {

	//Instance variables
	private Set<Card> unseenWeapons;
	private Set<Card> unseenPersons;
	private Set<Card> unseenRooms;
	private Card stub = new Card();
	
	public ComputerPlayer() {
		super();
	}
	
	//Constructs player with unseen card decks
	public ComputerPlayer(String name, Color color, int row, int col, 
			Set<Card> weapons, Set<Card> persons, Set<Card> rooms) {
		super(name, color, row, col);
		unseenWeapons = weapons;
		unseenPersons = persons;
		unseenRooms = rooms;
	}
	
	//Constructs player without setting unseen card decks
	public ComputerPlayer(String name, Color color, int row, int col) {
		super(name, color, row, col);
	}
	
	//Moves computer player on board given a set of possible targets
	public BoardCell selectTargets(Set<BoardCell> targets) {
		boolean tempFlag = false;
		Card tempCard = new Card();
		ArrayList<BoardCell> roomList = new ArrayList<>();
		ArrayList<Card> cardList = new ArrayList<>();
		for(BoardCell b : targets) {	//Searches possible targets for rooms
			for(Card room : unseenRooms) {
				if(room.getName().charAt(0) == b.getInitial()) {
					tempFlag = true;
					tempCard = room;
				}
			}
			if(b.isRoom() && tempFlag) {
				roomList.add(b);
				cardList.add(tempCard);
			}
		}
		if(!roomList.isEmpty()) { //Moves to room if possible and unvisited
			int randIndex = new Random().nextInt(roomList.size());
			removeSeen(cardList.get(randIndex));
			return roomList.get(randIndex);
		}
		
		//Moves to walkway location
		int randIndex = new Random().nextInt(targets.size());
		int currIndex = 0;
		for(BoardCell b : targets) {
			if(currIndex == randIndex) {
				return b;
			}
			currIndex++;
		}
		return null;
	}
	
	//Computer player makes a suggestion
	public Solution createSuggestion(Room room) {
		int random = new Random().nextInt(unseenPersons.size());
		int randomCounter = 0;
		Card tempPerson = new Card();
		Card tempWeapon = new Card();
		Card tempRoom = new Card(room.getName(), CardType.ROOM);
		for (Card person : unseenPersons) {	//Chooses random person
			if (randomCounter == random) {
				tempPerson = person;
				break;
			}
			randomCounter++;
		}
		
		random = new Random().nextInt(unseenWeapons.size());
		randomCounter = 0;
		for (Card weapon : unseenWeapons) {	//Chooses random weapon
			if (randomCounter == random) {
				tempWeapon = weapon;
				break;
			}
			randomCounter++;
		}
		
		return new Solution(tempPerson, tempRoom, tempWeapon);
	}
	
	@Override
	public void addCard(Card c) {
		super.addCard(c);
		removeSeen(c);
	}
	
	//Remove seen card from appropriate list of unseen cards
	public void removeSeen(Card c) {
		switch (c.getType()) {
		case WEAPON:
			unseenWeapons.remove(c);
			break;
		case PERSON:
			unseenPersons.remove(c);
			break;
		case ROOM:
			unseenRooms.remove(c);
			break;
		}
		
	}
	
	//Checks to see if computer player can win the game
	public void solveMystery() {
		if(unseenRooms.size() == 1 && unseenPersons.size() == 1 && unseenWeapons.size() == 1) {
			String solutionRoom = "stub";
			for(Card r : unseenRooms) {
				solutionRoom = r.getName();
			}
			
			String solutionPerson = "stub";
			for(Card p : unseenPersons) {
				solutionPerson = p.getName();
			}
			
			String solutionWeapon = "stub";
			for(Card w : unseenPersons) {
				solutionWeapon = w.getName();
			}
			JOptionPane.showMessageDialog(new JFrame(), getName() + " made the accusation: " + solutionPerson + ", "  + solutionRoom + ", " + solutionWeapon, "Accusation made", JFrame.EXIT_ON_CLOSE);
			Board.getCurrentBoard().handleAccusation(new Solution(solutionPerson, solutionRoom, solutionWeapon), this);
		}
	}
	
	//Setters/getters
	public void setUnseenWeapons(Set<Card> weapons) {
		unseenWeapons = weapons;
	}
	
	public Set<Card> getUnseenWeapons(){
		return unseenWeapons;
	}
	
	public void setUnseenPersons(Set<Card> persons) {
		unseenPersons = persons;
	}
	
	public Set<Card> getUnseenPersons(){
		return unseenPersons;
	}
	
	public void setUnseenRooms(Set<Card> rooms) {
		unseenRooms = rooms;
	}
	
	public Set<Card> getUnseenRooms(){
		return unseenRooms;
	}

	public void addUnseenRoomCard(Card roomCard) {
		unseenRooms.add(roomCard);
		
	}
}
