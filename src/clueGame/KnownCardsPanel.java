package clueGame;

import java.awt.GridLayout;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class KnownCardsPanel {
	
	
	
	public KnownCardsPanel() {
		setLayout(new GridLayout(3, 1)); //TODO What's wrong with set Layout here?
	}
	
	private JPanel createPeople() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		JLabel inHand = new JLabel("In Hand:");
		JLabel seen = new JLabel("Seen:");
		
		panel.add(inHand);
		for (Card card: ) { //TODO access player's hand
			if (card.getType() == CardType.PERSON) {
				JTextField cardName = new JTextField(card.getName());
				cardName.setEditable(false);
				panel.add(cardName);
			}
		}
		
		//TODO Add seen cards
		
		
		return panel;
	}
	
	private JPanel createRooms() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		JLabel inHand = new JLabel("In Hand:");
		JLabel seen = new JLabel("Seen:");
		
		return panel;
	}
	
	private JPanel createWeapons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		JLabel inHand = new JLabel("In Hand:");
		JLabel seen = new JLabel("Seen:");
		
		return panel;
	}
	
	public static void main(String[] args) {
		KnownCardsPanel panel = new KnownCardsPanel();
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		

	}

}
