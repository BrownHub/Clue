package clueGame;

import java.awt.GridLayout;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class KnownCardsPanel extends JPanel {

	// Member variables
	private JPanel seenPeople;
	private JPanel seenRooms;
	private JPanel seenWeapons;
	private boolean emptyPeople = true;
	private boolean emptyRooms = true;
	private boolean emptyWeapons = true;
	private static KnownCardsPanel theInstance;

	// Constructor
	// Sets up the panels
	public KnownCardsPanel() {
		setLayout(new GridLayout(3, 1));
		add(createPeople());
		add(createRooms());
		add(createWeapons());
	}
	
	// this method returns the only Board
	public static KnownCardsPanel getInstance() {
		theInstance = new KnownCardsPanel();
		return theInstance;
	}

	public static KnownCardsPanel getCurrentKnownCards() {
		return theInstance;
	}

	public void updateSeen(Player p, Card seenCard) {
		switch(seenCard.getType()) {
		case WEAPON:
			updateSeenWeapon(p, seenCard);
			break;
		case PERSON:
			updateSeenPeople(p, seenCard);
			break;
		case ROOM:
			updateSeenRooms(p, seenCard);
			break;
		default:
			break;
		}
	}
	// updates the people seen by the player
	public void updateSeenPeople(Player p, Card seenCard) {
		// removes "None"
		if(emptyPeople) {
			emptyPeople = false;
			seenPeople.remove(seenPeople.getComponents().length - 1);
		}

		JTextField newCard = new JTextField(seenCard.getName());
		newCard.setEditable(false);
		newCard.setBackground(p.getColor());
		seenPeople.add(newCard);
	}

	// updates the rooms seen by the player
	public void updateSeenRooms(Player p, Card seenCard) {
		// removes "None"
		if(emptyRooms) {
			emptyRooms = false;
			seenRooms.remove(seenRooms.getComponents().length - 1);
		}

		JTextField newCard = new JTextField(seenCard.getName());
		newCard.setEditable(false);
		newCard.setBackground(p.getColor());
		seenRooms.add(newCard);
	}

	// updates the weapons seen by the player
	public void updateSeenWeapon(Player p, Card seenCard) {
		// removes "None"
		if(emptyWeapons) {
			emptyWeapons = false;
			seenWeapons.remove(seenWeapons.getComponents().length - 1);
		}

		JTextField newCard = new JTextField(seenCard.getName());
		newCard.setEditable(false);
		newCard.setBackground(p.getColor());
		seenWeapons.add(newCard);
	}

	// sets up the panel used to display the people in the player's hand as well as the seen people
	private JPanel createPeople() {
		seenPeople = new JPanel();
		seenPeople.setLayout(new GridLayout(0, 1));
		JLabel inHand = new JLabel("In Hand:");
		JLabel seen = new JLabel("Seen:");

		seenPeople.add(inHand);
		boolean hasPerson = false;
		for (Card card : Board.getCurrentBoard().getThePlayer().getHand()) {
			if (card.getType() == CardType.PERSON) {
				JTextField cardName = new JTextField(card.getName());
				cardName.setEditable(false);
				seenPeople.add(cardName);
				hasPerson = true;
			}
		}

		if (!hasPerson) {
			JTextField noneInHand = new JTextField("None");
			noneInHand.setEditable(false);
			seenPeople.add(noneInHand);
		}

		seenPeople.add(seen);
		JTextField noneSeen = new JTextField("None");
		noneSeen.setEditable(false);
		seenPeople.add(noneSeen);
		seenPeople.setBorder(new TitledBorder(new EtchedBorder(), "People"));

		return seenPeople;
	}

	// sets up the panel used to display the rooms in the player's hand as well as the seen rooms
	private JPanel createRooms() {
		seenRooms = new JPanel();
		seenRooms.setLayout(new GridLayout(0, 1));
		JLabel inHand = new JLabel("In Hand:");
		JLabel seen = new JLabel("Seen:");

		seenRooms.add(inHand);
		boolean hasPerson = false;
		for (Card card : Board.getCurrentBoard().getThePlayer().getHand()) {
			if (card.getType() == CardType.ROOM) {
				JTextField cardName = new JTextField(card.getName());
				cardName.setEditable(false);
				seenRooms.add(cardName);
				hasPerson = true;
			}
		}
		if (!hasPerson) {
			JTextField noneInHand = new JTextField("None");
			noneInHand.setEditable(false);
			seenRooms.add(noneInHand);
		}

		seenRooms.add(seen);
		JTextField noneSeen = new JTextField("None");
		noneSeen.setEditable(false);
		seenRooms.add(noneSeen);
		seenRooms.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));


		return seenRooms;
	}

	// sets up the panel used to display the weapons in the player's hand as well as the seen weapons
	private JPanel createWeapons() {
		seenWeapons = new JPanel();
		seenWeapons.setLayout(new GridLayout(0, 1));
		JLabel inHand = new JLabel("In Hand:");
		JLabel seen = new JLabel("Seen:");

		seenWeapons.add(inHand);
		boolean hasPerson = false;
		for (Card card : Board.getCurrentBoard().getThePlayer().getHand()) {
			if (card.getType() == CardType.WEAPON) {
				JTextField cardName = new JTextField(card.getName());
				cardName.setEditable(false);
				seenWeapons.add(cardName);
				hasPerson = true;
			}
		}
		if (!hasPerson) {
			JTextField noneInHand = new JTextField("None");
			noneInHand.setEditable(false);
			seenWeapons.add(noneInHand);
		}

		seenWeapons.add(seen);
		JTextField noneSeen = new JTextField("None");
		noneSeen.setEditable(false);
		seenWeapons.add(noneSeen);
		seenWeapons.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));


		return seenWeapons;
	}

	public static void main(String[] args) {
		// Create board
		Board board = Board.getInstance();
		// set the file names
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize 
		board.initialize();
		//Set up frame with panel
		KnownCardsPanel panel = new KnownCardsPanel();
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		//Pause to show difference between initial setup and updated setup
		Scanner in = new Scanner(System.in);
		System.out.println("Enter to test if updates work properly");
		in.nextLine();

		//Update to display all cards in other players hands
		Set<Player> playerSet = board.getPlayerSet();
		for(Player p : playerSet) {
			if(!(p instanceof HumanPlayer)) {
				for(Card c :p.getHand()) {
					if(c.getType() == CardType.PERSON) {
						panel.updateSeenPeople(p, c);
					}
					if(c.getType() == CardType.WEAPON) {
						panel.updateSeenWeapon(p, c);
					}
					if(c.getType() == CardType.ROOM) {
						panel.updateSeenRooms(p, c);
					}
				}
			}
		}
		frame.pack();

	}

}
