package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	
	private Board board;
	
	public ClueGame() {
		setSize(700, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Create the board
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();	
		add(board, BorderLayout.CENTER);
		add(new GameControlPanel(), BorderLayout.SOUTH);
		add(new KnownCardsPanel(board), BorderLayout.EAST);
		setVisible(true);
		JOptionPane.showMessageDialog(new JFrame(), "You are " + board.getThePlayer().getName() + ". \nCan you find who done it?", "Welcome to Clue", JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String args[]) {
		ClueGame clue = new ClueGame();
	}
	
}
