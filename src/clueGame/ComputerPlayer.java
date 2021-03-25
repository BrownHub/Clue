package clueGame;

import java.awt.Color;
import java.util.HashSet;
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
	
	public Solution createSuggestion() {
		// TODO create method
		return new Solution(stub, stub, stub);
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
		Set<Card> stubSet = new HashSet();
		stubSet.add(stub);
		return stubSet;
	}
	
	public void setUnseenPersons(Set<Card> persons) {
		unseenPersons = persons;
	}
	
	public Set<Card> getUnseenPersons(){
		Set<Card> stubSet = new HashSet();
		stubSet.add(stub);
		return stubSet;
	}
	
	public void setUnseenRooms(Set<Card> rooms) {
		unseenRooms = rooms;
	}
	
	public Set<Card> getUnseenRooms(){
		Set<Card> stubSet = new HashSet();
		stubSet.add(stub);
		return stubSet;
	}
}
