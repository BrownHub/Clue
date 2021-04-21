package clueGame;

public class Card {
	private String name;
	private CardType type;
	public Card(String name, CardType type) {
		this.name = name;
		this.type = type;
	}
	
	public Card() {
		this.name = "stub";
	}
	// getters and setters
	public String getName() {
		return name;
	}
	
	public CardType getType() {
		return type;
	}
	
	
	public boolean isEquals(Card c) {
		if (c == this) {
			return true;
		}
		
		return c.getName() == this.name & c.getType() == this.type;
	}
}
