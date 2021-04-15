package clueGame;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame implements MouseListener {
	
	private GameControlPanel control;
	
	public ClueGame() {
		setSize(700, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Create the board
		Board.getInstance();
		Board.getCurrentBoard().setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		Board.getCurrentBoard().initialize();	
		add(Board.getCurrentBoard(), BorderLayout.CENTER);
		control = new GameControlPanel();
		add(control, BorderLayout.SOUTH);
		add(KnownCardsPanel.getInstance(), BorderLayout.EAST);
		addMouseListener(this);
		setVisible(true);
		JOptionPane.showMessageDialog(new JFrame(), "You are " + Board.getCurrentBoard().getThePlayer().getName() + ". \nCan you find who done it?", "Welcome to Clue", JFrame.EXIT_ON_CLOSE);
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	// Moves the player to the selected target if it is valid, otherwise returns an error
	@Override
	public void mousePressed(MouseEvent e) {
		boolean notValid = true;
		for(BoardCell b : Board.getCurrentBoard().getTargets()) {
			if(b.isTarget() && b.isClicked(e.getX(), e.getY(), Board.getCurrentBoard().getWidth() / Board.getCurrentBoard().getNumColumns(), Board.getCurrentBoard().getHeight() / Board.getCurrentBoard().getNumRows(),Board.getCurrentBoard().getWidth(),Board.getCurrentBoard().getNumColumns())) {
				Board.getCurrentBoard().getThePlayer().setCell(b);
				Board.getCurrentBoard().repaint();
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
