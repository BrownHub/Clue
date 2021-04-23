package clueGame;

import javax.swing.JComboBox;

public class Solution {
	public Card person;
	public Card room;
	public Card weapon;
	
	public Solution(Card p, Card r, Card w) {
		person = p;
		room = r;
		weapon = w;
	}

	public Solution(String p, String r, String w) {
		buildSolution(p, r, w);
	}
	
	// builds a solution based on string input
	private void buildSolution(String p, String r, String w) {
		for (Card roomCard: Board.getCurrentBoard().getRoomDeck()) {
			if(roomCard.getName().equals(r)) {
				this.room = roomCard;
			}
		}
		
		for (Card personCard: Board.getCurrentBoard().getPlayerDeck()) {
			if(personCard.getName().equals(p)) {
				this.person = personCard;
			}
		}
		
		for (Card weaponCard: Board.getCurrentBoard().getWeaponDeck()) {
			if(weaponCard.getName().equals(w)) {
				this.weapon = weaponCard;
			}
		}
	}
	
	public boolean isEqual(Solution solB) {
		return (this.person.equals(solB.person) && this.room.equals(solB.room) && this.weapon.equals(solB.weapon));
	}
	
	public String toString() {
		return "It was " + person.getName() + " in the " + room.getName() + " with the " + weapon.getName() + ".";
	}
}
