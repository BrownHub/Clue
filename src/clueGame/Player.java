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
	boolean moved;
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
		moved = false;
	}
	
	
	public Player() {
		this.name = "Empty";
		hand = new HashSet<>();
	}

	// returns a card that disproves a given suggestion
	public Card disproveSuggestion(Solution aSolution) {
		ArrayList<Card> matches = new ArrayList<>();
		
		for (Card cardInHand : hand) {
			if (cardInHand.isEquals(aSolution.person) || 
					cardInHand.isEquals(aSolution.room) || 
							cardInHand.isEquals(aSolution.weapon)) {
				matches.add(cardInHand);
			}
		}
		
		if (!matches.isEmpty()) {
			return matches.get(new Random().nextInt(matches.size()));
		}
		return null;
	}
	
	// checks if a card is in the player's hand
	public boolean isInHand(Card c) {
		for (Card cardInHand : hand) {
			if (cardInHand.equals(c)) {
				return true;
			}
		}
		return false;
	}
	
	// draws the player on the board
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
	public BoardCell getPlayerCell() {
		return Board.getCurrentBoard().getCell(row, col);
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCell(BoardCell cell) {
		this.row = cell.getRow();
		this.col = cell.getCol();
	}
	
	public void setCell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
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
	
	public boolean getMoved() {
		return moved;
	}
	
	public void setMoved(boolean b) {
		moved = b;
	}

}
