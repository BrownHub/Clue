package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {
	int row;
	int col;
	String name;
	Color playerColor;
	Set<Card> hand;
	public Player(String name, Color color, int row, int col) {
		super();
		this.name = name;
		this.playerColor = color;
		this.row = row;
		this.col = col;
		hand = new HashSet<>();
		
	}
	
	// getters and setters	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return playerColor;
	}
	
	public Set<Card> getHand() {
		return hand;
	}
	
	public void addCard(Card c) {
		hand.add(c);
	}
}
