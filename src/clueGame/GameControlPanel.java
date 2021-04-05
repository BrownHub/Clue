package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JTextField rollValue;
	private JTextArea playerGuess;
	private JTextArea guessResult;
	private JButton next;
	private JButton makeAccusation;

	// Constructor, creates and implements each panel
	public GameControlPanel() {
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
		playerTurn = new JTextField("Col. Mustard");
		playerTurn.setEditable(false);
		playerTurn.setBackground(Color.orange);
		JLabel rollLabel = new JLabel("Roll");
		rollValue = new JTextField("5");
		rollValue.setEditable(false);
		panel.add(turnLabel);
		panel.add(rollLabel);
		panel.add(playerTurn);
		panel.add(rollValue);

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
				setPlayerTurn();
				setRoll();
				setGuess();
			}
		}
		
		next.addActionListener(new NextListener());
		return panel;
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
		playerTurn.setText("New Text");
	}

	private void setRoll() {
		rollValue.setText("1");
	}

	private void setGuess() {
		playerGuess.setText("new Guess");
		setGuessResult();
	}

	private void setGuessResult() {
		guessResult.setText("New result");
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
