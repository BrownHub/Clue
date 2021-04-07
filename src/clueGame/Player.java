package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
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
	
	public Card disproveSuggestion(Solution aSolution) {
		ArrayList<Card> matches = new ArrayList();
		
		for (Card cardInHand : hand) {
			if (cardInHand.equals(aSolution.person) || 
					cardInHand.equals(aSolution.room) || 
							cardInHand.equals(aSolution.weapon)) {
				matches.add(cardInHand);
			}
		}
		
		if (matches.size() > 0) {
			return matches.get(new Random().nextInt(matches.size()));
		}
		return null;
	}
	
	public void draw(Graphics g, int width, int height, int boardWidth, int columns) {
		int size;
		if (width > height) {
			size = height;
		} else {
			size = width;
		}
		
		int offset;
		offset = (boardWidth - (size * columns)) / 2;
		g.setColor(playerColor);
		g.fillOval(size * col + offset, size * row, size, size);
		g.drawOval(size * col + offset, size * row, size, size);
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
