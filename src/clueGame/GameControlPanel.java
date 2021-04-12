package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel{
	
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
		rollDisplay = new JTextField("5");
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
				handlePlayerTurn();
			}
		}
		
		next.addActionListener(new NextListener());
		return panel;
	}
	
	private void handlePlayerTurn() {
		setPlayerTurn();
		setRoll();
		
		if (getCurrentPlayer() instanceof ComputerPlayer) {
			Board.getCurrentBoard().calcTargets(getCurrentPlayer().getPlayerCell(), rollValue);
			BoardCell newCell = ((ComputerPlayer) getCurrentPlayer()).selectTargets(Board.getCurrentBoard().getTargets());
			getCurrentPlayer().setCell(newCell);
			
			setGuess();
			Board.getCurrentBoard().repaint();
		}
	}
	
	// creates panel for handling player guesses
	private JPanel createGuessPanel() {
		JPanel panel = new JPanel();
		playerGuess = new JTextArea();
		playerGuess.setEditable(false);
		updateGuess();
		panel.add(playerGuess);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));

		return panel;
	}
	
	// updates the player's guess
	private void updateGuess() {
		playerGuess.setText("I have no guess");
	}
	
	// updates the player's guess result panel
	private JPanel createGuessResultPanel() {
		JPanel panel = new JPanel();
		guessResult = new JTextArea();
		guessResult.setEditable(false);
		updateGuessResult();
		panel.add(guessResult);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));

		return panel;
	}
	
	// updates the result panel
	private void updateGuessResult() {
		guessResult.setText("So you have nothing?");
	}
	
	// setters for each panel
	private void setPlayerTurn() {
		playerQueue.add(playerQueue.poll());
		playerTurn.setText(getCurrentPlayer().getName());
		playerTurn.setBackground(getCurrentPlayer().getColor());
	}

	private void setRoll() {
		rollValue = rand.nextInt(6) + 1;
		rollDisplay.setText(String.valueOf(rollValue));
	}

	private void setGuess() {
		playerGuess.setText("new Guess");
		setGuessResult();
	}

	private void setGuessResult() {
		guessResult.setText("New result");
	}
	
	private Player getCurrentPlayer() {
		return playerQueue.peek();
	}
	
	private int getRollValue() {
		return rollValue;
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
		panel.setGuess();
		panel.setPlayerTurn();

	}

}
