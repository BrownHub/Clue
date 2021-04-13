package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BoardCell {
	// member variables
	private Set<BoardCell> adjList;
	private boolean isRoom;
	private boolean isOccupied;
	private boolean isTarget;
	private boolean isDoorway;
	private int row; 
	private int col;
	DoorDirection doorDirection;
	private char initial;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage = '\0';

	// constructor
	public BoardCell(int setRow, int setCol) {
		super();
		adjList = new HashSet<>();
		setRow(setRow);
		setCol(setCol);
		setTarget(false);
	}

	//Draws the cell
	public void draw(Graphics g, int width, int height, int boardWidth, int columns) {
		g.setColor(Color.black);	
		//Set size of grid squares
		int size;
		if (width > height) {
			size = height;
		} else {
			size = width;
		}

		//Set centering offset
		int offset;
		offset = (boardWidth - (size * columns)) / 2;

		//Display grid square details
		if(isTarget) {
			g.setColor(Color.RED);
			g.fillRect(size * col + offset, size * row, size, size);
		} else if(isRoom) {
			//Display room
			g.setColor(Color.GRAY);
			g.fillRect(size * col + offset, size * row, size, size);
		} else if(isDoorway) {
			//Display doorway
			g.setColor(Color.YELLOW);
			g.fillRect(size * col + offset, size * row, size, size);
			g.setColor(Color.BLUE);

			switch (doorDirection) {
			case UP:
				g.fillRect(size * col + offset, size * row, size, size / 6);
				break;
			case DOWN:
				g.fillRect(size * col + offset, size * row + size * 5 / 6 + 1, size, size / 6);
				break;
			case LEFT:
				g.fillRect(size * col + offset, size * row, size / 6, size);
				break;
			case RIGHT:
				g.fillRect(size * col + offset +  size * 5 / 6 + 1, size * row, size / 6, size);
				break;
			default:
				break;
			}
			
		} else if(initial == 'X') {
			//Display unused space
			g.setColor(Color.BLACK);
			g.fillRect(size * col + offset, size * row, size, size);
		} else if (initial == 'W') {
			//Display walkway
			g.setColor(Color.YELLOW);
			g.fillRect(size * col + offset, size * row, size, size);
		}
		if (!isRoom) {
			//Display grid
			g.setColor(Color.black);	
			g.drawRect(size * col + offset, size * row, size, size);
		}
	}

	//Display room label
	public void drawLabel(Graphics g, int width, int height, int boardWidth, int columns, Map<Character, Room> roomMap) {
		//Set grid square size
		int size;
		if (width > height) {
			size = height;
		} else {
			size = width;
		}
		//Set centering offset
		int offset;
		offset = (boardWidth - (size * columns)) / 2;
		g.setColor(Color.BLUE); 
		g.drawString(roomMap.get(initial).getName(), size * col + offset, size * row);
	}

	public boolean isClicked(int mouseX, int mouseY, int width, int height, int boardWidth, int columns) {
		//Set size of grid squares
		int size;
		if (width > height) {
			size = height;
		} else {
			size = width;
		}

		//Set centering offset
		int offset;
		offset = (boardWidth - (size * columns)) / 2;
		Rectangle rect = new Rectangle(size * col + size / 4 + offset, size * (row + 1), size, size);
		return (rect.contains(new Point(mouseX, mouseY)));
	}

	// Getter and setter functions
	public void addAdj(BoardCell adj) {
		adjList.add(adj);
	}

	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	public void setRoom(boolean newRoom) {
		isRoom = newRoom;
	}

	public boolean isRoom() {
		return isRoom;
	}

	public void setOccupied(boolean newOccupied) {
		isOccupied = newOccupied;
	}

	public boolean getOccupied() {
		return isOccupied;
	}

	public void setDoorway(boolean t) {
		isDoorway = t;
	}

	public boolean isDoorway() {
		return isDoorway;
	}

	public char getInitial() {
		return initial;
	}

	public void setInitial(char currInitial) {
		initial = currInitial;
	}

	public void setLabel(boolean label) {
		roomLabel = label;
	}
	public boolean isLabel() {
		return roomLabel;
	}

	public void setRoomCenter(boolean center) {
		roomCenter = center;
	}

	public boolean isRoomCenter() {
		return roomCenter;
	}

	public char getSecretPassage() {
		return secretPassage;
	}

	public void setDoorDirection(DoorDirection dir) {
		doorDirection = dir;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public boolean isDoorDirection(DoorDirection dir) {
		return this.doorDirection == dir;
	}
	public void setSecretPassage(char c) {
		secretPassage = c;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int newRow) {
		row = newRow;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int newCol) {
		col = newCol;
	}

	public void setTarget(boolean b) {
		isTarget = b;
	}
	
	public boolean isTarget() {
		return isTarget;
	}

}