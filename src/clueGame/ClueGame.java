package clueGame;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame implements MouseListener {
	
	private Board board;
	private GameControlPanel control;
	public ClueGame() {
		setSize(700, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Create the board
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();	
		add(board, BorderLayout.CENTER);
		control = new GameControlPanel();
		add(control, BorderLayout.SOUTH);
		add(new KnownCardsPanel(board), BorderLayout.EAST);
		addMouseListener(this);
		setVisible(true);
		JOptionPane.showMessageDialog(new JFrame(), "You are " + board.getThePlayer().getName() + ". \nCan you find who done it?", "Welcome to Clue", JFrame.EXIT_ON_CLOSE);
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		boolean notValid = true;
		for(BoardCell b : board.getTargets()) {
			if(b.isTarget() && b.isClicked(e.getX(), e.getY(), board.getWidth() / board.getNumColumns(), board.getHeight() / board.getNumRows(), board.getWidth(), board.getNumColumns())) {
				board.getThePlayer().setCell(b);
				board.repaint();
				notValid = false;
			}
		}
		if(notValid) {
			JOptionPane.showMessageDialog(new JFrame(), "NOT A VALID TARGET", "ERROR", JFrame.EXIT_ON_CLOSE);
		} else {
			control.setMoveFinished(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}
	
	public static void main(String args[]) {
		ClueGame clue = new ClueGame();
	}
	
}
