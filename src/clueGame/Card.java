package clueGame;

public class Card {
	private String name;
	private CardType type;
	public Card(String name, CardType type) {
		this.name = name;
		this.type = type;
	}
	
	// getters and setters
	public String getName() {
		return name;
	}
	
	public CardType getType() {
		return type;
	}
}
