package clueGame;

import java.awt.GridLayout;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class KnownCardsPanel extends JPanel {
	private Board board;
	private JPanel seenPeople;
	private JPanel seenRooms;
	private JPanel seenWeapons;
	public KnownCardsPanel(Board board) {
		this.board = board;
		setLayout(new GridLayout(3, 1));
		add(createPeople());
		add(createRooms());
		add(createWeapons());
	}
	
	private void updateSeenPeople(Card seenCard) {
		seenPeople.remove(new JTextField("None"));
		JTextField newCard = new JTextField(seenCard.getName());
		newCard.setEditable(false);
		seenPeople.add(newCard);
	}
	
	private void updateSeenRooms(Card seenCard) {
		seenRooms.remove(new JTextField("None"));
		JTextField newCard = new JTextField(seenCard.getName());
		newCard.setEditable(false);
		seenRooms.add(newCard);
	}
	
	private void updateSeenWeapon(Card seenCard) {
		seenWeapons.remove(new JTextField("None"));
		JTextField newCard = new JTextField(seenCard.getName());
		newCard.setEditable(false);
		seenWeapons.add(newCard);
	}
	
	private JPanel createPeople() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		JLabel inHand = new JLabel("In Hand:");
		JLabel seen = new JLabel("Seen:");

		panel.add(inHand);
		boolean hasPerson = false;
		for (Card card : board.getThePlayer().getHand()) {
			if (card.getType() == CardType.PERSON) {
				JTextField cardName = new JTextField(card.getName());
				cardName.setEditable(false);
				panel.add(cardName);
				hasPerson = true;
			}
		}
		if (!hasPerson) {
			JTextField noneInHand = new JTextField("None");
			noneInHand.setEditable(false);
			panel.add(noneInHand);
		}

		panel.add(seen);
		JTextField noneSeen = new JTextField("None");
		noneSeen.setEditable(false);
		panel.add(noneSeen);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "People"));

		return panel;
	}

	private JPanel createRooms() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		JLabel inHand = new JLabel("In Hand:");
		JLabel seen = new JLabel("Seen:");
		
		panel.add(inHand);
		boolean hasPerson = false;
		for (Card card : board.getThePlayer().getHand()) {
			if (card.getType() == CardType.ROOM) {
				JTextField cardName = new JTextField(card.getName());
				cardName.setEditable(false);
				panel.add(cardName);
				hasPerson = true;
			}
		}
		if (!hasPerson) {
			JTextField noneInHand = new JTextField("None");
			noneInHand.setEditable(false);
			panel.add(noneInHand);
		}

		panel.add(seen);
		JTextField noneSeen = new JTextField("None");
		noneSeen.setEditable(false);
		panel.add(noneSeen);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));


		return panel;
	}

	private JPanel createWeapons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		JLabel inHand = new JLabel("In Hand:");
		JLabel seen = new JLabel("Seen:");
		
		panel.add(inHand);
		boolean hasPerson = false;
		for (Card card : board.getThePlayer().getHand()) {
			if (card.getType() == CardType.WEAPON) {
				JTextField cardName = new JTextField(card.getName());
				cardName.setEditable(false);
				panel.add(cardName);
				hasPerson = true;
			}
		}
		if (!hasPerson) {
			JTextField noneInHand = new JTextField("None");
			noneInHand.setEditable(false);
			panel.add(noneInHand);
		}

		panel.add(seen);
		JTextField noneSeen = new JTextField("None");
		noneSeen.setEditable(false);
		panel.add(noneSeen);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));


		return panel;
	}

	public static void main(String[] args) {
		// Create board
		Board board = Board.getInstance();
		// set the file names
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize 
		board.initialize();
		KnownCardsPanel panel = new KnownCardsPanel(board);
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
