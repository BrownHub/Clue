package clueGame;

public class Card {
	private String name;
	private String type;
	public Card(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	// getters and setters
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
}
