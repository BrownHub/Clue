package clueGame;

public class Room {
	// member variables
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	// constructor
	public Room(String aName, BoardCell center, BoardCell label) {
		super();
		setName(aName);
		setCenterCell(center);
		setLabelCell(label);
	}

	// Getters and setters for member variables
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
	
	
}
