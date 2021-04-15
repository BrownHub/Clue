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
		turnOne();
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

		class NextListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(moveFinished) {	// Ensures that the player moves before advancing turns
					resetValidTargets(Board.getCurrentBoard().getGrid());
					handlePlayerTurn();
				}
			}
		}

		next.addActionListener(new NextListener());
		return panel;
	}

	private void handlePlayerTurn() {
		// TODO: If called to a room by suggestion, players should be allowed to make a suggestion
		// TODO: Move players if they were called to a room
		setPlayerTurn();
		setRoll();
		Board.getCurrentBoard().calcTargets(getCurrentPlayer().getPlayerCell(), rollValue);

		if (getCurrentPlayer() instanceof ComputerPlayer) {
			BoardCell newCell = ((ComputerPlayer) getCurrentPlayer()).selectTargets(Board.getCurrentBoard().getTargets());
			getCurrentPlayer().setCell(newCell);
			
			if(getCurrentPlayer().getPlayerCell().isRoom()) {
				Room suggestionRoom = new Room("Suggestion");
				suggestionRoom = Board.getCurrentBoard().getRoom(getCurrentPlayer().getPlayerCell());
				setGuess(((ComputerPlayer) getCurrentPlayer()).createSuggestion(suggestionRoom));
			}
			// TODO: Bug where only HumanPlayer disproves the suggestion
			Board.getCurrentBoard().repaint();
		} else {
			setValidTargets(Board.getCurrentBoard().getTargets());
			Board.getCurrentBoard().repaint();
			// TODO: Player can make a Guess
		}
	}

	private void turnOne() {
		setMoveFinished(false);
		setRoll();
		Board.getCurrentBoard().calcTargets(getCurrentPlayer().getPlayerCell(), rollValue);
		setValidTargets(Board.getCurrentBoard().getTargets());
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

	private void setGuess(Solution s) {
		playerGuess.setText(getCurrentPlayer().getName() + " made the guess: " + s);
		//getCurrentPlayer().
		setGuessResult(Board.getCurrentBoard().handleSuggestion(getCurrentPlayer(), s, Board.getCurrentBoard().getPlayerSet()));
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

	private Player getCurrentPlayer() {
		return playerQueue.peek();
	}

	public void setMoveFinished(boolean b) {
		moveFinished = b;
	}

	// main function which creates the frame and tests the setters
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setSize(750, 180);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Scanner in = new Scanner(System.in);
		System.out.println("Enter to test if setters work properly");
		in.nextLine();
		panel.setRoll();
		panel.setPlayerTurn();
	}

}
