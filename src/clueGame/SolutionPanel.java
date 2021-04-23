package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SolutionPanel extends JFrame {
	private Player player;
	
	public SolutionPanel(boolean isAccusation, Player player) {
		this.player = player;
		setSize(450, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (isAccusation) {
			JOptionPane.showMessageDialog(new JFrame(), "If you make an incorrect accusation, you lose!", "WARNING", JFrame.EXIT_ON_CLOSE);
			setTitle("Make Accusation");
			add(createAccusationPanel());
		} else {
			setTitle("Make Suggestion");
			add(createSuggestionPanel());
		}
		setVisible(true);
	}


	private JPanel createAccusationPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));
		
		JLabel roomLabel = new JLabel("Room");
		JLabel personLabel = new JLabel("Person");
		JLabel weaponLabel = new JLabel("Weapon");
		
		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Cancel");
		
		JComboBox<String> roomBox = new JComboBox<>();
		for (Card room: Board.getCurrentBoard().getRoomDeck()) {
			roomBox.addItem(room.getName());
		}
		JComboBox<String> personBox = new JComboBox<>();
		for (Card person: Board.getCurrentBoard().getPlayerDeck()) {
			personBox.addItem(person.getName());
		}
		JComboBox<String> weaponBox = new JComboBox<>();
		for (Card weapon: Board.getCurrentBoard().getWeaponDeck()) {
			weaponBox.addItem(weapon.getName());
		}
		
		panel.add(roomLabel);
		panel.add(roomBox);
		panel.add(personLabel);
		panel.add(personBox);
		panel.add(weaponLabel);
		panel.add(weaponBox);		
		panel.add(submit);
		panel.add(cancel);

		submit.setActionCommand("Submit");
		cancel.setActionCommand("Cancel");
		class Listener implements ActionListener {
			@Override
			public void actionPerformed (ActionEvent e) {
				String action = e.getActionCommand();
				if(action.equals("Submit")) {		
					String room = (String) roomBox.getSelectedItem();
					String person = (String) personBox.getSelectedItem();
					String weapon = (String) weaponBox.getSelectedItem();
					
					Board.getCurrentBoard().handleAccusation(new Solution(person, room, weapon), player);
				}
				dispose();
			}

		}
		
		submit.addActionListener(new Listener());
		cancel.addActionListener(new Listener());
		return panel;
	}
	
	private JPanel createSuggestionPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));
		
		JLabel roomLabel = new JLabel("Room");
		JLabel personLabel = new JLabel("Person");
		JLabel weaponLabel = new JLabel("Weapon");
		
		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Cancel");
		
		String guessedRoomLabel = Board.getCurrentBoard().getRoom(player.getPlayerCell()).getName();
		JLabel guessedRoom = new JLabel(guessedRoomLabel);
		JComboBox<String> personBox = new JComboBox<>();
		for (Card person: Board.getCurrentBoard().getPlayerDeck()) {
			personBox.addItem(person.getName());
		}
		JComboBox<String> weaponBox = new JComboBox<>();
		for (Card weapon: Board.getCurrentBoard().getWeaponDeck()) {
			weaponBox.addItem(weapon.getName());
		}
		panel.add(roomLabel);
		panel.add(guessedRoom);
		panel.add(personLabel);
		panel.add(personBox);
		panel.add(weaponLabel);
		panel.add(weaponBox);		
		panel.add(submit);
		panel.add(cancel);
		
		class Listener implements ActionListener {
			@Override
			public void actionPerformed (ActionEvent e) {
				String action = e.getActionCommand();
				if(action.equals("Submit")) {		
					String room = guessedRoomLabel;
					String person = (String) personBox.getSelectedItem();
					String weapon = (String) weaponBox.getSelectedItem();
					
					Solution playerGuess = new Solution(person, room, weapon);
					GameControlPanel.getCurrentPanel().setGuess(playerGuess);
				}
				dispose();
			}

		}
		
		submit.addActionListener(new Listener());
		cancel.addActionListener(new Listener());
		return panel;
	}
	
	public static void main(String args[]) {
		Board board = Board.getInstance();
		Board.getCurrentBoard().setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		SolutionPanel panel = new SolutionPanel(false, board.getThePlayer());
	}
}