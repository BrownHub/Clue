package clueGame;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClueGame extends JFrame implements MouseListener {
	
	private GameControlPanel control;
	private static ClueGame instance;
	
	private static ClueGame getInstance() {
		instance = new ClueGame();
		return instance;
	}
	
	public static ClueGame getCurrentGame() {
		return instance;
	}
	
	
	public ClueGame() {
		setSize(700, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Create the board
		Board.getInstance();
		Board.getCurrentBoard().setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		Board.getCurrentBoard().initialize();	
		add(Board.getCurrentBoard(), BorderLayout.CENTER);
		control = GameControlPanel.getInstance();
		add(control, BorderLayout.SOUTH);
		add(KnownCardsPanel.getInstance(), BorderLayout.EAST);
		addMouseListener(this);
		setVisible(true);
		JOptionPane.showMessageDialog(new JFrame(), "You are " + Board.getCurrentBoard().getThePlayer().getName() + ". \nCan you find who done it?", "Welcome to Clue", JFrame.EXIT_ON_CLOSE);
	}
	
	public void playerWins(Player p) {
		JOptionPane.showMessageDialog(new JFrame(), p.getName() + " wins!", "Game Over!", JFrame.EXIT_ON_CLOSE);
		dispose();
	}
	
	public void playerLoses(Player p) {
		JOptionPane.showMessageDialog(new JFrame(), "Incorrect Accusation.\nYou lose!", "Game Over!", JFrame.EXIT_ON_CLOSE);
		dispose();
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
			if(control.getCurrentPlayer().getPlayerCell().isRoom()) {
				int opt = JOptionPane.showConfirmDialog(new JFrame(), "Would you like to make a Suggestion?", "Make a Suggestion:", JOptionPane.YES_NO_OPTION);
				if(opt == JOptionPane.YES_OPTION) {
					//TODO: implement player accusation
				}	
			}
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
		ClueGame clue = ClueGame.getInstance();
	}
	
}
