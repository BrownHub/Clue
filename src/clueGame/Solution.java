package clueGame;

public class Solution {
	public Card person;
	public Card room;
	public Card weapon;
	
	public Solution(Card p, Card r, Card w) {
		person = p;
		room = r;
		weapon = w;
	}

	public boolean isEqual(Solution solB) {
		return (this.person.equals(solB.person) && this.room.equals(solB.room) && this.weapon.equals(solB.weapon));
	}
}
