
public class TrainingCard extends Card {
	
	private TrainingCardType trainingCardType;
	private Team healthPotionType;
	
	// For Health Potions
	TrainingCard(String cardName, TrainingCardType trainingCardType, Team healthPotionType) {
		setCardType(CardType.TRAINER);
		setCardName(cardName);
		this.trainingCardType = trainingCardType;
		this.healthPotionType = healthPotionType;
	}
	
	// For all other training cards
	TrainingCard(String cardName, TrainingCardType trainingCardType) {
		setCardType(CardType.TRAINER);
		setCardName(cardName);
		this.trainingCardType = trainingCardType;
	}
	
	public TrainingCardType getTrainingCardType() {
		return trainingCardType;
	}
	
	public void setTrainingCardType(TrainingCardType trainingCardType) {
		this.trainingCardType = trainingCardType;
	}
	
	public void play(Player player) { // This method is called from within the Player class
		// Plus Ultra
		Dueler target;
		if (trainingCardType == TrainingCardType.PLUS_ULTRA) {
			// add 20 to the max HP of the target
			target = player.target();

			target.setMaxHP(target.getMaxHP() + 20);
			// add 20 the current HP by setting it to the new max
			target.setCurrentHP(target.getMaxHP());
			System.out.println("Max HP of " + target.getCardName() + " was increased by 20 using Plus Ultra!");
			
		}
		
		// Recover
		if (trainingCardType == TrainingCardType.RECOVER) {
			target = player.target();
			target.setCurrentHP(target.getMaxHP());
			System.out.println(target.getCardName() + " was healed to full health using Recover!");
		}
		
		// Status Advance
		if (trainingCardType == TrainingCardType.STATUS_ADVANCE) {
			// target = dueler to be upgraded
			target = player.target();
			if (target.getExperience() < 3) {
				System.out.println("Your Dueler needs to be on the field for 3 turns or more to use Status Advance!");
			}
			else
				target.statusAdvance();
		}
		
		// Lucky Draw
		if (trainingCardType == TrainingCardType.LUCKY_DRAW) {
			player.draw(3);
		}
		
		// Health Potion
		if (trainingCardType == TrainingCardType.HEALTH_POTION) {
			target = player.target();
			if (healthPotionType == target.getTeam()) {
				target.setCurrentHP(target.getCurrentHP() + 30);
			}
			else {
				target.setCurrentHP(target.getCurrentHP() + 10);
			}
			
			if (target.getCurrentHP() > target.getMaxHP()) {
				target.setCurrentHP(target.getMaxHP());
			}
			System.out.println("HP of " + target.getCardName() + " increased to " + target.getCurrentHP() + " using Health Potion!\n");
		}
		
		// Instant Confrontation
		if (trainingCardType == TrainingCardType.INSTANT_CONFRONTATION) {
			Player otherPlayer = player.getOtherPlayer();
			//target = otherPlayer.target();
			otherPlayer.instantConfrontation();
		}
		
	}
	
}
