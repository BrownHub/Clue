package clueGame;

import java.awt.BorderLayout;
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
		
		// Accusation Listener handles functionality for when the player presses the make accusation button
		class AccusationListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!moveFinished && getCurrentPlayer() instanceof HumanPlayer) {	// accusations can only be made at the beginning of a that player's turn
					JFrame accusationFrame = new SolutionPanel(true, getCurrentPlayer());
				} else {
					JOptionPane.showMessageDialog(new JFrame(), "You can only make an accusation at the beginning of your turn", "ERROR:", JFrame.EXIT_ON_CLOSE);
				}
			}
		}
		
		// Next Listener handles functionality for when the player presses the next turn button
		// Effectively handles moving through each player's turn
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
	
	// Ensures that the first turn is the human player's turn
	private void setTurnOrder() {
		while(getCurrentPlayer() instanceof ComputerPlayer) {
			setPlayerTurn();
		}
	}
	
	// handles the current player's turn
	private void handlePlayerTurn() {
		// gets the targets for the player to move to
		setRoll();
		Board.getCurrentBoard().resetOccupied();
		Board.getCurrentBoard().calcTargets(getCurrentPlayer().getPlayerCell(), rollValue);
		
		if (getCurrentPlayer() instanceof ComputerPlayer) {	// computer player turn
			((ComputerPlayer) getCurrentPlayer()).solveMystery();	// check if the computer player can win
			if(getCurrentPlayer().getMoved()) {	// if the player was moved to a room, they may make a guess
				getCurrentPlayer().setMoved(false);
				Room suggestionRoom;
				suggestionRoom = Board.getCurrentBoard().getRoom(getCurrentPlayer().getPlayerCell());
				handleGuess(((ComputerPlayer) getCurrentPlayer()).createSuggestion(suggestionRoom));
			} else {
				// handles moving the computer players
				BoardCell newCell = ((ComputerPlayer) getCurrentPlayer()).selectTargets(Board.getCurrentBoard().getTargets());
				getCurrentPlayer().setCell(newCell);
				
				// handles the computer players making a suggestion
				if(getCurrentPlayer().getPlayerCell().isRoom()) {
					Room suggestionRoom;
					suggestionRoom = Board.getCurrentBoard().getRoom(getCurrentPlayer().getPlayerCell());
					handleGuess(((ComputerPlayer) getCurrentPlayer()).createSuggestion(suggestionRoom));
				}
			}
		} else { // human player turn
			boolean temp = false;
			if(getCurrentPlayer().getMoved()) {	// check if the human player wants to make a suggestion if they were moved
				temp = ClueGame.getCurrentGame().promptSuggestion();
				getCurrentPlayer().setMoved(false);
			}
			
			if(!temp) {	// if the player did not make a suggestion, they may move
				setValidTargets(Board.getCurrentBoard().getTargets());
			} else {
				moveFinished = true;
			}
		}
		Board.getCurrentBoard().repaint();
	}
	
	// determines the valid targets painted by the board
	private void setValidTargets(Set<BoardCell> targets) {
		for(BoardCell cell : targets) {
			cell.setTarget(true);
		}
	}
	
	// resets the valid targets painted by the board
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

	// determines the next player's turn
	private void setPlayerTurn() {
		playerQueue.add(playerQueue.poll());
		playerTurn.setText(getCurrentPlayer().getName());
		playerTurn.setBackground(getCurrentPlayer().getColor());
		if(getCurrentPlayer() instanceof HumanPlayer) {
			setMoveFinished(false);
		}
	}
	
	// sets the current dice roll
	private void setRoll() {
		rollValue = rand.nextInt(6) + 1;
		rollDisplay.setText(String.valueOf(rollValue));
	}
	
	// handles the guess by the player
	public void handleGuess(Solution s) {
		playerGuess.setLineWrap(true);
		playerGuess.setWrapStyleWord(true);
		playerGuess.setText(getCurrentPlayer().getName() + " made the guess: " + s);
		Board.getCurrentBoard().getPlayerFromSet(s.person.getName()).setMoved(true);
		setGuessResult(Board.getCurrentBoard().handleSuggestion(getCurrentPlayer(), s));
	}
	
	// determines if the guess resulted in a new clue
	private void setGuessResult(Card c) {
		if(c == null) {
			guessResult.setText("No new clue");
		} else {
			Player hasCard = new ComputerPlayer();
			for(Player p : Board.getCurrentBoard().getPlayerSet()) {
				if(p.isInHand(c)) {
					hasCard = p;
				}
			}
			
			if(hasCard instanceof HumanPlayer) {
				guessResult.setText("You disproved with " + c.getName());
			} else {
				guessResult.setText("Suggestion disproved by " + hasCard.getName());
			}

		}
	}
	
	// removes a player if they lost the game
	public void removeCurrentPlayer() {
		playerQueue.poll();
		playerTurn.setText(getCurrentPlayer().getName());
		playerTurn.setBackground(getCurrentPlayer().getColor());
	}
	
	// Getters and setters
	public Player getCurrentPlayer() {
		return playerQueue.peek();
	}

	public void setMoveFinished(boolean b) {
		moveFinished = b;
	}

	public boolean getMoveFinished() {
		return moveFinished;
	}
	
}
