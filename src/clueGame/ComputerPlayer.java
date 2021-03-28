package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

	private Set<Card> unseenWeapons;
	private Set<Card> unseenPersons;
	private Set<Card> unseenRooms;
	private Card stub = new Card();
	
	public ComputerPlayer(String name, Color color, int row, int col, 
			Set<Card> weapons, Set<Card> persons, Set<Card> rooms) {
		super(name, color, row, col);
		unseenWeapons = weapons;
		unseenPersons = persons;
		unseenRooms = rooms;
	}
	
	public ComputerPlayer(String name, Color color, int row, int col) {
		super(name, color, row, col);
	}
	
	public BoardCell selectTargets(Set<BoardCell> targets) {
		boolean tempFlag = false;
		ArrayList<BoardCell> roomList = new ArrayList<>();
		for(BoardCell b : targets) {
			for(Card room : unseenRooms) {
				if(room.getName().charAt(0) == b.getInitial()) {
					tempFlag = true;
				}
			}
			if(b.isRoom() && tempFlag) {
				roomList.add(b);
			}
		}
		if(roomList.size() > 0) {
			int randIndex = new Random().nextInt(roomList.size());
			return roomList.get(randIndex);
		}
		
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
	
	public Solution createSuggestion(Room room) {
		int random = new Random().nextInt(unseenPersons.size());
		int randomCounter = 0;
		Card tempPerson = new Card();
		Card tempWeapon = new Card();
		Card tempRoom = new Card(room.getName(), CardType.ROOM);
		
		for (Card person : unseenPersons) {
			if (randomCounter == random) {
				tempPerson = person;
				break;
			}
			randomCounter++;
		}
		
		random = new Random().nextInt(unseenWeapons.size());
		randomCounter = 0;
		for (Card weapon : unseenWeapons) {
			if (randomCounter == random) {
				tempWeapon = weapon;
				break;
			}
			randomCounter++;
		}
		
		return new Solution(tempPerson, tempRoom, tempWeapon);
	}
	
	public void addCard(Card c) {
		super.addCard(c);
		removeSeen(c);
	}
	
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
}
