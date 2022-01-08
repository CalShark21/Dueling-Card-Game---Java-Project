import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

	public static void main(String[] args) throws IOException {

		boolean winCondition = false;
		
		String[] deckArrayP1, deckArrayP2;

		ArrayList<Card> deckP1 = new ArrayList<Card>();
		ArrayList<Card> deckP2 = new ArrayList<Card>();
		
		Scanner userInput = new Scanner(System.in);
				
		// Get filenames for players decks and verify file exists		
		System.out.print("Enter the filename of Player 1's deck:  ");		
		String fileNameP1 = userInput.nextLine();
		File   deckFileP1 = new File(fileNameP1);
				
		while (!deckFileP1.exists()) { 
			System.out.println("Deck not found");
			System.out.println("Enter the filename of Player 1's deck:  ");		
			fileNameP1 = userInput.nextLine();
			deckFileP1 = new File(fileNameP1);
		}
		
		System.out.print("Enter the filename of Player 2's deck:  ");		
		String fileNameP2 = userInput.nextLine();
		File   deckFileP2 = new File(fileNameP2);
		
		while (!deckFileP2.exists()) {
			System.out.println("Deck not found");
			System.out.println("Enter the filename of Player 2's deck:  ");		
			fileNameP2 = userInput.nextLine();
			deckFileP2 = new File(fileNameP2);
		}
		
				
		
		// Call method for reading file, creates an array of Strings
		deckArrayP1 = getDeckFile(fileNameP1);		
		deckArrayP2 = getDeckFile(fileNameP2);
		
		// Create deck to be used in program
		// Converts array of strings into ArrayList of Card objects
		deckP1 = createDeck(deckArrayP1);
		deckP2 = createDeck(deckArrayP2);
		
		// Create the two Player objects, pass deck to constructor
		Player player1 = new Player(deckP1, PlayerNumber.PLAYER_ONE);
		Player player2 = new Player(deckP2, PlayerNumber.PLAYER_TWO);
		
		// Add opposing player to each Player object
		player1.setOtherPlayer(player2);
		player2.setOtherPlayer(player1);
		
		// Create the Field - Passes both players benches as arguments
		Field field = new Field(player1.getBench(), player2.getBench());
		
		// Start game 

		int i = 0;

		
		while (!winCondition)
		{
			while (i % 2 == 0 && winCondition == false) {
				// Player One's turn
				gamePlay(player1, player2, field);
				winCondition = player1.getWinFlag();
				i++;
				
				while (i % 2 == 1 && winCondition == false) {
					// Player Two's turn
					gamePlay(player2, player1, field);
					winCondition = player2.getWinFlag();
					i++;
					
				}
								
			}
			
		}

	}

	
	
	/**
	 * gamePlay is the primary method in the program wherein each player's turn takes place.
	 * A loop iterates through each turn and prompts the user for input. The loop ends when 
	 * a particular command has been made ('pass' or a valid 'attack') and the next player's 
	 * turn begins.
	 * @param att The attacking player (Player object)
	 * @param opp The opposing player (Player object)
	 * @param f The Field (Field object)
	 */
	public static void gamePlay(Player att, Player opp, Field f) {		
		Field  field = f;
		Player attacker = att;
		// Player opponent = opp;
		
		int cardChoice;
		boolean continueTurn = true;
		boolean winCondition = false;

		Scanner userInput = new Scanner(System.in);

		// String commands for user input
		String printField, printHand, attack, pass;		
		printField = "print field";
		printHand  = "print hand";
		attack     = "attack";
		pass       = "pass";
				
		// Call method to update player's Duelers experience each turn
		experienceUpdate(attacker.getBench());
		
		// Banner for announcing attacker's turn
		System.out.println("------------------" + attacker.getPlayerNum() + "'s turn! " + "------------------");
		
		// Check if this is the first round - if so draw 6 cards
		if (attacker.getTurnCount() == 0) {
			System.out.println("");
			attacker.draw(6);
		}
		
		// Draw card at beginning of turn
		else
			attacker.draw(1);
		
		
		// Initiate input 
		System.out.print("Enter a command: ");
		String command = userInput.nextLine();
		String[] commandSplit = command.split(" ");
		
		
		// Main loop: Outputs prompts, takes and validates user input, and calls respective methods
		while(continueTurn == true) {

			
			// Print Field
			if (command.toLowerCase().equals(printField)) {
				
				field.printField();
				
				// Begin next loop iteration
				System.out.print("Enter a command: ");
				command = userInput.nextLine();
				commandSplit = command.split(" ");				
				continueTurn = true;
			}
			
			// Print Hand
			else if (command.toLowerCase().equals(printHand)) {
				attacker.printHand();
				
				// Begin next loop iteration
				System.out.print("\nEnter a command: ");
				command = userInput.nextLine();
				commandSplit = command.split(" ");			
				continueTurn = true;
			}
			
			// Attack
			else if (command.toLowerCase().equals(attack)) {			
				if (attacker.getBench()[0] != null) { // checks if there is a dueler in the attacker's arena
					Card arena = attacker.getArena();

					continueTurn = !((Dueler) arena).attack(attacker); // continues if attack is unsuccessful
					
					winCondition = attacker.getWinFlag();
					
					if (winCondition)
					{
						System.out.println("End of game!");
					}
					
					else if (continueTurn) { // Prevents infinite loop
						System.out.print("Enter a command: ");
						command = userInput.nextLine();
						commandSplit = command.split(" ");
					}
				}
				else { // if there is no dueler in the attacker's arena
					System.out.println("Unable to attack! You have no cards in your arena!\n");
					
					// Prevents infinite loop
					System.out.print("\nEnter a command: ");
					command = userInput.nextLine();
					commandSplit = command.split(" ");
					
					continueTurn = true;
				}
			}
			
			// Pass
			else if (command.toLowerCase().equals(pass)) {
				System.out.println(attacker.getPlayerNumString() + " passes.\n");
				continueTurn = false;
			}
			
			// Switch
			else if (commandSplit[0].toLowerCase().equals("switch")) {
				
				// Validate switch command for extra words or lacking a number
				if (commandSplit.length > 2 || commandSplit.length == 1 ) {
					System.out.println(command + " is an invalid command, enter one of the following commands: ");
					System.out.println("'print field', 'print hand', 'attack', 'switch[card]', 'play[card]', 'pass'");
					
					System.out.print("\nEnter a command: ");
					command = userInput.nextLine();
					commandSplit = command.split(" ");
					continueTurn = true;			
				}
				
				
				else {   // Carry-out switch
								
					boolean switchSuccess = false;				
					
					try {
						// Call switchCard method (returns a boolean)
						switchSuccess = attacker.switchCards(Integer.parseInt(commandSplit[1]));
					}
					catch(NumberFormatException | NullPointerException e) {
						System.out.println("Invalid command");
					}
					
					// Check if chosen card is out of bounds for bench
					if (switchSuccess == false) {
						
						System.out.println("\nThat bench position doesn't exist! Enter a command: ");
						System.out.println("'print field', 'print hand', 'attack', 'switch[card]', 'play[card]', 'pass'");
						System.out.print("\nEnter a command: ");
						command = userInput.nextLine();
						commandSplit = command.split(" ");
						continueTurn = true;	
							
					}
					
					// Begin next loop iteration
					System.out.print("\nEnter a command: ");
					command = userInput.nextLine();
					commandSplit = command.split(" ");					
					continueTurn = true;
				
				}
			}
			
			// Play
			else if (commandSplit[0].toLowerCase().equals("play")) {
				
				// Validates play command is in correct format 'play[number]'
				if (commandSplit.length < 2 || commandSplit.length > 2) {
					
					System.out.println(command + " is an invalid command, enter one of the following commands: ");
					System.out.println("'print field', 'print hand', 'attack', 'switch[card]', 'play[card]', 'pass'");
					System.out.print("\nEnter a command: ");
					command = userInput.nextLine();
					commandSplit = command.split(" ");
					continueTurn = true;
					
				}
				
				else {

					try {
						cardChoice = Integer.parseInt(commandSplit[1]);
						attacker.play(cardChoice);
					}					
					catch(NumberFormatException e) {
						System.out.println("Invalid command");
					}
					
					// Begin next loop iteration
					System.out.print("\nEnter a command: ");
					command = userInput.nextLine();
					commandSplit = command.split(" ");
					continueTurn = true;
					
				}
			}
			
			// Wrong input edge case
			else {
				System.out.println(command + " is an invalid command, enter one of the following commands: ");
				System.out.println("'print field', 'print hand', 'attack', 'switch[card]', 'play[card]', 'pass'");
				
				// Begin next loop iteration
				System.out.print("\nEnter a command: ");
				command = userInput.nextLine();
				commandSplit = command.split(" ");
				continueTurn = true;
				
			}
		
		}
		
		// Increment attacker's turn count
		attacker.setTurnCount();
		
		//userInput.close();
	}
	

	/**
	 * createDeck method reads array of card names, as Strings, converts them to
	 * Card objects, and creates the player's deck.
	 * @param deckArray A String array containing names of cards
	 * @return An ArrayList of Card objects
	 */
	public static ArrayList<Card> createDeck (String[] deckArray){
	
		ArrayList<Card> deck = new ArrayList<Card>();
		
		String aList, bList, cList, xList; 
		aList = "A-List";
		bList = "B-List";
		cList = "C-List";
		xList = "X-List";

		
		for (int i = 0; i < deckArray.length; i++)
		{
			
			String[] cardNameSplit = deckArray[i].split(" ");
			
			// Duelers
			if (deckArray[i].equals("Puppy Claire")) {
				deck.add(new Dueler("Puppy Claire", 20, 60, Team.C, 0));			
			}
			else if (deckArray[i].equals("Kitten Carlos")) {
				deck.add(new Dueler("Kitten Carlos", 0, 100, Team.C, 0));
			}
			else if (deckArray[i].equals("Puppy Bob")) {
				deck.add(new Dueler("Puppy Bob", 20, 60, Team.B, 0));			
			}
			else if (deckArray[i].equals("Kitten Bea")) {
				deck.add(new Dueler("Kitten Bea", 30, 20, Team.B, 0));
			}
			else if (deckArray[i].equals("Puppy Ash")) {
				deck.add(new Dueler("Puppy Ash", 20, 60, Team.A, 0));
			}
			else if (deckArray[i].equals("Kitten Alex")) {
				deck.add(new Dueler("Kitten Alex", 20, 80, Team.A, 0));				
			}
			else if (deckArray[i].equals("Puppy Xylo")) {
				deck.add(new Dueler("Puppy Xylo", 20, 80, Team.X, 0));				
			}
			else if (deckArray[i].equals("Kitten Xin")) {
				deck.add(new Dueler("Kitten Xin", 30, 60, Team.X, 0));				
			}
			
			// Training Cards
			else if (deckArray[i].equals("Recover")) {
				deck.add(new TrainingCard("Recover", TrainingCardType.RECOVER));
			}
			else if (deckArray[i].equals("Plus Ultra")) {
				deck.add(new TrainingCard("Plus Ultra", TrainingCardType.PLUS_ULTRA));
			}
			else if (deckArray[i].equals("Lucky Draw")) { 
				deck.add(new TrainingCard("Lucky Draw", TrainingCardType.LUCKY_DRAW));
			}
			else if (deckArray[i].equals("Status Advance")) {
				deck.add(new TrainingCard("Status Advance", TrainingCardType.STATUS_ADVANCE));
			}
			else if (deckArray[i].equals("Instant Confrontation")) {
				deck.add(new TrainingCard("Instant Confrontation", TrainingCardType.INSTANT_CONFRONTATION));
			}
			
			
			// Health Potions 
			else if (deckArray[i].equals("A-List Health Potion") || deckArray[i].equals("B-List Health Potion") 
				  || deckArray[i].equals("C-List Health Potion") || deckArray[i].equals("X-List Health Potion")) {
				
				cardNameSplit = deckArray[i].split(" ");
				
				if (cardNameSplit[0].equals(aList))
					deck.add(new TrainingCard("A-List Health Potion", TrainingCardType.HEALTH_POTION, Team.A));
					
				else if (cardNameSplit[0].equals(bList))
					deck.add(new TrainingCard("B-List Health Potion", TrainingCardType.HEALTH_POTION, Team.B));
					
				else if (cardNameSplit[0].equals(cList))
					deck.add(new TrainingCard("C-List Health Potion", TrainingCardType.HEALTH_POTION, Team.C));
					
				else if (cardNameSplit[0].equals(xList))
					deck.add(new TrainingCard("X-List Health Potion", TrainingCardType.HEALTH_POTION, Team.X));
					
			}

			
			else {
				//System.out.println(deckArray[i]);
				System.out.println("Error reading array");
				System.out.println(deckArray[i]);		
			}
			
		}
				
		return deck;
		
	}
	
	
	/**
	 * The getDeckFile method uses the DuelingIO.java to read an array of Strings from
	 * a text file.
	 * @param filename Name of a .txt file containing card names
	 * @return An array of strings containing card names
	 * @throws IOException
	 */
	public static String[] getDeckFile (String filename) throws IOException
	{
		String[] playerDeck = DuelingIO.getDeck(filename);
		
		return playerDeck;
	}
	
	
	/**
	 * The experienceUpdate method iterates through a player's bench and increments
	 * the Dueler's experience
	 * @param bench The player's bench
	 */
	public static void experienceUpdate(Card[] bench) {
		
		Card[] attackersBench = bench;
		for(int i = 0; i < attackersBench.length; i++){
			if (attackersBench[i] != null )
				((Dueler) attackersBench[i]).incrementExperience();
		}
			
	}
	
	public static void checkSpecialPower(Player player) {
		player.ashSpecialPower();
	}

}