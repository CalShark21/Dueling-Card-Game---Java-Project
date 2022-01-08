import java.util.*;


public class Player {	
	private ArrayList<Card> cardsInHand;
	private ArrayList<Card> cardsInDeck;
	private PlayerNumber playerNum;
	private int turnCount = 0;
	private int benchIndexCount = 1;
	private boolean winFlag = false;
	
	private Card[] benchArray;
	
	// Reference variable for other player (for attacking and new training card)
	// Also for generally accessing the other player's bench, etc
	private Player otherPlayer;
	
	Scanner keyboard = new Scanner(System.in); 
	

	public Player(List<Card> deck, PlayerNumber playNum){
		
		this.playerNum = playNum;
		cardsInHand = new ArrayList<Card>();
		cardsInDeck = (ArrayList<Card>) deck;		
		benchArray  = new Card[7];
	
	}
	
	public void setOtherPlayer(Player player) { 
		otherPlayer = player;
	}
	
	public void draw(int numCardsToDraw){
		
		if (cardsInDeck.size() <= 0) {
			System.out.println("No more cards in deck!\n");
		}
			
		else {
		
			if (numCardsToDraw > cardsInDeck.size())
				numCardsToDraw = cardsInDeck.size();
			
			else if (numCardsToDraw > 0){
				
				for(int i = 0; i < numCardsToDraw; i++) {
					cardsInHand.add(cardsInDeck.get(0)); // Adds to the end of hand
					cardsInDeck.remove(0); // Remove the top card (assuming 0 is top card)
				}
				
				if (numCardsToDraw == 1)
					System.out.println(this.playerNum + " draws a card! \n");
					
				else
					System.out.println(this.playerNum + " draws " + numCardsToDraw + ".\n");
								
			}
		
		}
		
	}
	

	public void play(int cardInHandChoice) {	//remember 'cardInHandIndex' is 1-indexed	
		Card cardChoice;	
		int cardInHandIndex = (cardInHandChoice - 1);
		
		// Validate users card choice is valid
		if (cardInHandIndex >= cardsInHand.size() || (cardInHandChoice < 1)){
			System.out.println("The position '" + cardInHandChoice + "' doesn't exist in your hand.");
		}
		
		else {
			
			cardChoice = cardsInHand.get((cardInHandIndex));	
	
			// Dueler 
			if ((cardChoice.getCardType()).equals(CardType.DUELER)) {

				// Test if bench is full
				if ( benchArray[1] != null && benchArray[2] != null && benchArray[3] != null && 
						benchArray[4] != null &&  benchArray[5] != null && benchArray[6] != null) {
					cardsInHand.remove(cardInHandIndex);
					System.out.println("Your bench is full: Dueler discarded\n");
				}
				

				// Test if this player's arena position is empty; if it is, card goes to arena and NOT bench)
				// 'Arena' is index 0 of player's bench
				else if (benchArray[0] == null) {
					
				cardsInHand.remove(cardInHandIndex);
				benchArray[0] = cardChoice;
				
				System.out.println("");
				System.out.println(this.playerNum + " moves " + cardChoice.getCardName() + " into the arena.\n");
				
			}
				
				// Add dueler to bench, and remove that card from the deck
				else {
					cardsInHand.remove(cardInHandIndex);
					benchArray[benchIndexCount] = cardChoice;
					benchIndexCount++;
					
					System.out.println("");
					System.out.println(this.playerNum + " moves " + cardChoice.getCardName() + " onto their bench.\n");
				}
	
			}
			
			
			
			// Training Card
			else if ((cardChoice.getCardType()).equals(CardType.TRAINER)) {
				System.out.println("That's a trainer card\n"); //placeholder
				
				
				
				if ((((TrainingCard)cardChoice).getTrainingCardType()) == TrainingCardType.LUCKY_DRAW) {
					// Plays the card for only Lucky Draw
					((TrainingCard)cardChoice).play(this);
					cardsInHand.remove(cardInHandIndex);
				}


				else if ( benchArray[0] == null && benchArray[1] == null && benchArray[2] == null && 
						benchArray[3] == null && benchArray[4] == null && benchArray[5]  == null && benchArray[6]  == null) {
					cardsInHand.remove(cardInHandIndex);
					System.out.println("No cards in bench/arena: Training card discarded\n");
				}
				
				
				else {
					// Plays the card
					((TrainingCard)cardChoice).play(this);
					cardsInHand.remove(cardInHandIndex);
				}
							
			}
			
			
			else
				System.out.println("Card type error");

		}
	}
	
	
	
	public boolean switchCards(int cardChoice) {
		boolean switchSuccess = false;	
		int cardToSwitch = (cardChoice);

		if (cardChoice < 7)
		{

			
			if (this.benchArray[cardToSwitch] == null) {
				
				Card tempArenaCard = this.benchArray[0];
				benchArray[cardToSwitch] = tempArenaCard;
				
				System.out.println("");
				System.out.println(this.playerNum + " moves " + (this.benchArray[0].getCardName()) + " to the bench");
				
				benchArray[0] = null;
			}
			
			else {	
				Card tempArenaCard = this.benchArray[0];
				benchArray[0] = benchArray[cardToSwitch];
				benchArray[cardToSwitch] = tempArenaCard;
				
				System.out.println("");
				System.out.println(this.playerNum + " switches " + (this.benchArray[cardToSwitch].getCardName()) + " with " + (this.benchArray[0]).getCardName()  );

			}
			
			switchSuccess = true;
			
		}
		
		else 
			switchSuccess = false;

		return switchSuccess;
		
	}
	
	
	public void instantConfrontation() { 
		
		if ( benchArray[1] == null && benchArray[2] == null && benchArray[3] == null && 
				benchArray[4] == null && benchArray[5] == null && benchArray[6] == null) {
			System.out.println("No duelers in enemy bench! Training card discarded.\n");
		}

		else {
			this.getBench();
			System.out.print("Which dueler to move to arena? ");
			int cardChoice = keyboard.nextInt();
			
			// !! This is a work-around, actually needs to check if there is only one Dueler on bench
			if (this.benchArray[cardChoice] == null) {
				System.out.println("There is only one dueler in the enemy bench! Training card discarded.\n");
			}
			
			else {
				Card tempArenaCard = this.benchArray[0];
				benchArray[0] = benchArray[cardChoice];
				benchArray[cardChoice] = tempArenaCard;
			}
		
			System.out.println("Successfully switched duelers.\n");
		}
	}
	
	
	public void retire() {
		Dueler arenaDueler = ((Dueler)benchArray[0]);
		if (arenaDueler.getCurrentHP() <= 0) {
			System.out.println(arenaDueler.getCardName() + "has retired!\n");
			benchArray[0] = null;			
		}
	}
	
	public boolean specialPowerRetire() {
		boolean inputValid = false;
		boolean completed = false;
		
		int cardChoice = 0;
	
		while (inputValid == false || cardChoice <= 0) {
			try {
				System.out.print("Choose an enemy Dueler to retire: ");
				cardChoice = keyboard.nextInt();
				inputValid = true;
			}
			catch (InputMismatchException e)
			{
				System.out.println("Please enter a number corresponding to the enemy's Dueler: ");
				keyboard.next();
				inputValid = false;
			}
		}

		if (benchArray[cardChoice].getCardType() == CardType.DUELER) {
			System.out.println("Wolf Xylo forces " + benchArray[cardChoice].getCardName() +" to retire!");		
			benchArray[cardChoice] = null;
			completed = true;
		}
		
		else {
			System.out.println("That's not a Dueler! Attack unsuccessful.");	
			completed = false;
		}
		
		
		return completed;
		
	}
	
	public void ashSpecialPower() {
		if (benchArray[0] != null && benchArray[0].getCardName().equals("Wolf Ash")) {
			for (int i = 1; i < benchArray.length; i++) {
				if (benchArray[i] != null) {
					if (benchArray[i].getCardType() == CardType.DUELER) {
						((Dueler) benchArray[i]).setCurrentHP(((Dueler) benchArray[i]).getCurrentHP() + 5);
						if (((Dueler) benchArray[i]).getCurrentHP() > ((Dueler) benchArray[i]).getMaxHP()) {
							((Dueler) benchArray[i]).setCurrentHP(((Dueler) benchArray[i]).getMaxHP());
						}
					}
				}
			}
			System.out.println("All duelers on " + this.getPlayerNumString() + "'s bench have been awarded 5 HP from Wolf Ash in the arena.");
		}
	}
	
	public Dueler target() {
		// Training card target
		this.getBench();
		System.out.print("Which dueler to apply effect on? ");
		int cardChoice = keyboard.nextInt();
		int cardChoiceIndex = cardChoice - 1;
		
		return (Dueler)benchArray[cardChoiceIndex];
	}
	
	
	public void printHand() {
		System.out.println("\n" + this.playerNum + "'s current hand: ");
		for(int i = 0; i < cardsInHand.size(); i++){
			System.out.println((i + 1) + ") " +  (cardsInHand.get(i)).getCardName());			
		}
		System.out.println("");
		
		System.out.println("Number of cards left in deck: " + cardsInDeck.size());
	}
	
	
	public PlayerNumber getPlayerNum() {
		return this.playerNum;
	}
	
	public String getPlayerNumString() {
		if (this.playerNum == PlayerNumber.PLAYER_ONE) {
			return "Player One";
		}
		
		else if(this.playerNum == PlayerNumber.PLAYER_TWO) {
			return "Player Two";
		}
			
		else
			return "Invalid Player Number";
			
	}
	

	public ArrayList<Card> getDeck() { // Returns the deck as a reference to the ArrayList
		return cardsInDeck;
	}
	
	public Player getOtherPlayer() { // Returns the other player object
		return otherPlayer;
	}
	
	public Card[] getBench() {
		return benchArray;
	}
	
	public Card getArena() {
		return this.benchArray[0];
	}
		
	public void setTurnCount() {
		turnCount++;
	}
	
	public int getTurnCount() {
		return turnCount;
	}
	
	
	public void setWinFlag(boolean b) {
		winFlag = b;
	}
	
	public boolean getWinFlag() {
		return winFlag;
	}
	

}

