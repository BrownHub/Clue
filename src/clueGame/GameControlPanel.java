package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {

	// Member variables
	private JTextField playerTurn;
	private JTextField rollDisplay;
	private JTextArea playerGuess;
	private JTextArea guessResult;
	private JButton next;
	private JButton makeAccusation;
	private Queue<Player> playerQueue;
	private Random rand;
	private int rollValue;
	private boolean moveFinished;
	private static GameControlPanel control;
	
	public static GameControlPanel getInstance() {
		control = new GameControlPanel();
		return control;
	}
	
	public static GameControlPanel getCurrentPanel() {
		return control;
	}
	// Constructor, creates and implements each panel
	public GameControlPanel() {
		rand = new Random();
		playerQueue = new LinkedList<>();
		playerQueue.add(Board.getCurrentBoard().getThePlayer());
		for(Player p : Board.getCurrentBoard().getPlayerSet()) {
			if(!(p instanceof HumanPlayer)) {
				playerQueue.add(p);
			}
		}
		setLayout(new GridLayout(2, 2));
		add(createTurnPanel());
		add(createButtonPanel());
		add(createGuessPanel());
		add(createGuessResultPanel());
		setMoveFinished(false);
		setTurnOrder();
		handlePlayerTurn();
	}

	// creates panel handling the turns of the player
	private JPanel createTurnPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));

		JLabel turnLabel = new JLabel("Player turn");
		playerTurn = new JTextField(playerQueue.peek().getName());
		playerTurn.setEditable(false);
		playerTurn.setBackground(playerQueue.peek().getColor());

		JLabel rollLabel = new JLabel("Roll");
		rollDisplay = new JTextField(rollValue);
		rollDisplay.setEditable(false);

		panel.add(turnLabel);
		panel.add(rollLabel);
		panel.add(playerTurn);
		panel.add(rollDisplay);

		return panel;
	}

	// creates button panel handling making accusations and next player's turn
	private JPanel createButtonPanel() {

		JPanel panel = new JPanel();
		makeAccusation = new JButton("Make Accusation");
		next = new JButton("Next!");
		panel.add(makeAccusation);
		panel.add(next);
		class AccusationListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame accusationFrame = new SolutionPanel(true, getCurrentPlayer());
			}
		}
		class NextListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(moveFinished) {	// Ensures that the player moves before advancing turns
					resetValidTargets(Board.getCurrentBoard().getGrid());
					setPlayerTurn();
					handlePlayerTurn();
				}
			}
		}

		next.addActionListener(new NextListener());
		makeAccusation.addActionListener(new AccusationListener());
		return panel;
	}
	
	private void setTurnOrder() {
		while(getCurrentPlayer() instanceof ComputerPlayer) {
			setPlayerTurn();
		}
	}
	
	private void handlePlayerTurn() {
		// TODO: If called to a room by suggestion, players should be allowed to make a suggestion
		// TODO: Computer Players cannot make accusations
		
		setRoll();
		Board.getCurrentBoard().resetOccupied();
		Board.getCurrentBoard().calcTargets(getCurrentPlayer().getPlayerCell(), rollValue);

		if (getCurrentPlayer() instanceof ComputerPlayer) {
			BoardCell newCell = ((ComputerPlayer) getCurrentPlayer()).selectTargets(Board.getCurrentBoard().getTargets());
			getCurrentPlayer().setCell(newCell);

			if(getCurrentPlayer().getPlayerCell().isRoom()) {
				Room suggestionRoom;
				suggestionRoom = Board.getCurrentBoard().getRoom(getCurrentPlayer().getPlayerCell());
				setGuess(((ComputerPlayer) getCurrentPlayer()).createSuggestion(suggestionRoom));
			}
		} else {
			setValidTargets(Board.getCurrentBoard().getTargets());
			
		}
		Board.getCurrentBoard().repaint();
	}

	private void setValidTargets(Set<BoardCell> targets) {
		for(BoardCell cell : targets) {
			cell.setTarget(true);
		}
	}

	private void resetValidTargets(BoardCell[][] grid) {
		for(BoardCell[] cellList : grid) {
			for(BoardCell cell : cellList) {
				cell.setTarget(false);
			}
		}
	}

	// creates panel for handling player guesses
	private JPanel createGuessPanel() {
		JPanel panel = new JPanel();
		playerGuess = new JTextArea();
		playerGuess.setEditable(false);
		playerGuess.setText("No guesses yet");
		panel.add(playerGuess);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));

		return panel;
	}

	// updates the player's guess result panel
	private JPanel createGuessResultPanel() {
		JPanel panel = new JPanel();
		guessResult = new JTextArea();
		guessResult.setEditable(false);
		guessResult.setText("No guesses yet");
		panel.add(guessResult);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));

		return panel;
	}

	// setters for each panel
	private void setPlayerTurn() {
		playerQueue.add(playerQueue.poll());
		playerTurn.setText(getCurrentPlayer().getName());
		playerTurn.setBackground(getCurrentPlayer().getColor());
		if(getCurrentPlayer() instanceof HumanPlayer) {
			setMoveFinished(false);
		}
	}

	private void setRoll() {
		rollValue = rand.nextInt(6) + 1;
		rollDisplay.setText(String.valueOf(rollValue));
	}

	public void setGuess(Solution s) {
		playerGuess.setText(getCurrentPlayer().getName() + " made the guess: " + s);
		Board.getCurrentBoard().getPlayerFromSet(s.person.getName()).setCell(getCurrentPlayer().getRow(), getCurrentPlayer().getCol());
		setGuessResult(Board.getCurrentBoard().handleSuggestion(getCurrentPlayer(), s));
	}

	private void setGuessResult(Card c) {
		if(c == null) {
			guessResult.setText("No player can disprove the guess");
		} else {
			Player hasCard = new ComputerPlayer();
			for(Player p : Board.getCurrentBoard().getPlayerSet()) {
				if(p.isInHand(c)) {
					hasCard = p;
				}
			}

			guessResult.setText(hasCard.getName() + " has the card " + c.getName());

		}
	}

	public Player getCurrentPlayer() {
		return playerQueue.peek();
	}

	public void setMoveFinished(boolean b) {
		moveFinished = b;
	}
	
	public boolean getMoveFinished() {
		return moveFinished;
	}
	public void removeCurrentPlayer() {
		playerQueue.poll();
		playerTurn.setText(getCurrentPlayer().getName());
		playerTurn.setBackground(getCurrentPlayer().getColor());
	}
}
