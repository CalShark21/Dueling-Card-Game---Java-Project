import java.util.*;

public class Dueler extends Card {
	private int attack;
	private int maxHP;
	private int currentHP;
	private Team team;
	private int experience;
	private Scanner userInput = new Scanner(System.in);
	
	Dueler(String cardName, int attack, int maxHP, Team team, int exp) {
		setCardType(CardType.DUELER);
		setCardName(cardName);
		this.attack = attack;
		this.maxHP = maxHP;
		this.currentHP = maxHP;
		this.team = team;
		experience = exp;
	}
	
	public boolean attack(Player player) { // this method is called from the Main class when the player types the attack command
		// will subtract "attack" from enemy's dueler in the arena
		// check if there is a dueler in the enemy arena, if not, win
		// check type of enemy card vs own type to determine advantage or not
		// check if there is a dueler in arena and if it is first turn
		boolean success;

		Player attackedPlayer = player.getOtherPlayer();
		if (player.getBench()[0] == null) { // If the attacking player has no cards on their bench
			System.out.println("Unable to attack! You have no cards in your arena!\n");
			success = false;
		}
		else if (experience < 1) { // Checks to see if the dueler has been there for at least a round
			System.out.println("\nUnable to attack! Your dueler must be in the bench for at least 1 turn!");
			success = false;
		}
		else if (attackedPlayer.getBench()[0] == null) { // Checks if there is a dueler in the enemy's arena
			System.out.println("\nCongratulations! " + player.getPlayerNumString() + " wins!\n");
			
			player.setWinFlag(true);
			
			success = true;
		}
		
		// Wolf Claire Special Power
		else if (attackedPlayer.getBench()[0].getCardName().equals("Wolf Claire") && experience < 3){
			System.out.println("Wolf Claire is invisibile and cannot be attacked for two turns after entering the field!");
			success = false;
		}
		// Wolf Xylo Special Power
		else if (player.getArena().getCardName().equals("Wolf Xylo")) {
			boolean completed = false;
			String answer;
			
			if (((Dueler) player.getArena()).getCurrentHP() > 60)
				completed = attackedPlayer.specialPowerRetire();
			
			if (completed == true) {
				((Dueler) player.getArena()).setCurrentHP(((Dueler) player.getArena()).getCurrentHP() - 60);
			}

			System.out.print("Do you want to use Wolf Xylo's original attack? (y/n): ");
			answer = userInput.nextLine();
			
			if (answer.equals("y")) {
				// (Attack) 
				System.out.println();
				System.out.println(player.getPlayerNumString() +   " attacks with " + player.getArena().getCardName() + "!");
				Dueler attackedDueler = (Dueler)attackedPlayer.getArena();
				int advantage = determineAdvantage(attackedDueler);
				int attackDamage = attack + advantage;
				if (attackDamage < 0) { // Makes sure it doesn't do a negative attack
					attackDamage = 0;
				}
				attackedDueler.setCurrentHP(attackedDueler.getCurrentHP() - attackDamage); // attacks
				//System.out.println(attackedDueler.getCurrentHP() + "/" + attackedDueler.getMaxHP());
				System.out.println("");
				System.out.println(attackedDueler.getCardName() + " took " + attackDamage + " damage from " + player.getPlayerNumString() + "!\n");
				attackedPlayer.retire(); // Checks if the attacked dueler should be retired
				success = true;
			}
			
			else {
				success = true;
			}
		
		}
		else {
			// (Attack) 
			System.out.println();
			System.out.println(player.getPlayerNumString() +   " attacks with " + player.getArena().getCardName() + "!");
			Dueler attackedDueler = (Dueler)attackedPlayer.getArena();
			int advantage = determineAdvantage(attackedDueler);
			int attackDamage = attack + advantage;
			if (attackDamage < 0) { // Makes sure it doesn't do a negative attack
				attackDamage = 0;
			}
			attackedDueler.setCurrentHP(attackedDueler.getCurrentHP() - attackDamage); // attacks
			//System.out.println(attackedDueler.getCurrentHP() + "/" + attackedDueler.getMaxHP());
			System.out.println("");
			System.out.println(attackedDueler.getCardName() + " took " + attackDamage + " damage from " + player.getPlayerNumString() + "!\n");
			attackedPlayer.retire(); // Checks if the attacked dueler should be retired
			success = true;
		}
		
		// Wolf Bob special power
		if (success && this.getCardName().equals("Wolf Bob")) {
			System.out.println("Wolf Bob gained 5 attack damage for attacking successfully.");
			this.attack++;
		}
		return success;
	}
	
	public int determineAdvantage(Dueler opponent) {
		int advantage;
		
		if ((this.team == Team.C && opponent.getTeam() == Team.B) || (this.team == Team.B && opponent.getTeam() == Team.X) ||
			(this.team == Team.X && opponent.getTeam() == Team.A) || (this.team == Team.A && opponent.getTeam() == Team.C)) {
			advantage = 20;
		}
		else if ((this.team == Team.B && opponent.getTeam() == Team.C) || (this.team == Team.X && opponent.getTeam() == Team.B) ||
				 (this.team == Team.A && opponent.getTeam() == Team.X) || (this.team == Team.C && opponent.getTeam() == Team.A)) {
			advantage = -20;
		}
		else {
			advantage = 0;
		}
		
		return advantage;
	}
	
	public void statusAdvance() { // Checks name to level up
		// Claire
		if (getCardName().equals("Puppy Claire")) {
			setCardName("Dog Claire");
			attack = 30;
			int oldMax = maxHP;
			maxHP = 100;
			currentHP += (maxHP - oldMax);
		} else if (getCardName().equals("Dog Claire")) {
			setCardName("Wolf Claire");
			attack = 60;
			int oldMax = maxHP;
			maxHP = 120;
			currentHP += (maxHP - oldMax);
			this.resetExperience(); // For invisibility special power
		}
		// Bob
		else if (getCardName().equals("Puppy Bob")) {
			setCardName("Dog Bob");
			attack = 30;
			int oldMax = maxHP;
			maxHP = 100;
			currentHP += (maxHP - oldMax);
		} else if (getCardName().equals("Dog Bob")) {
			setCardName("Wolf Bob");
			attack = 50;
			int oldMax = maxHP;
			maxHP = 140;
			currentHP += (maxHP - oldMax);
		}
		// Ash
		else if (getCardName().equals("Puppy Ash")) {
			setCardName("Dog Ash");
			attack = 30;
			int oldMax = maxHP;
			maxHP = 90;
			currentHP += (maxHP - oldMax);
		} else if (getCardName().equals("Dog Ash")) {
			setCardName("Wolf Ash");
			attack = 60;
			int oldMax = maxHP;
			maxHP = 120;
			currentHP += (maxHP - oldMax);
		}
		// Xylo
		else if (getCardName().equals("Puppy Xylo")) {
			setCardName("Dog Xylo");
			attack = 40;
			int oldMax = maxHP;
			maxHP = 120;
			currentHP += (maxHP - oldMax);
		} else if (getCardName().equals("Dog Xylo")) {
			setCardName("Wolf Xylo");
			attack = 50;
			int oldMax = maxHP;
			maxHP = 160;
			currentHP += (maxHP - oldMax);
		}
		// Carlos
		else if (getCardName().equals("Kitten Carlos")) {
			setCardName("Cat Carlos");
			attack = 50;
			int oldMax = maxHP;
			maxHP = 150;
			currentHP += (maxHP - oldMax);
		}
		// Bea
		else if (getCardName().equals("Kitten Bea")) {
			setCardName("Cat Bea");
			attack = 30;
			int oldMax = maxHP;
			maxHP = 70;
			currentHP += (maxHP - oldMax);
		}
		// Alex
		else if (getCardName().equals("Kitten Alex")) {
			setCardName("Cat Alex");
			attack = 40;
			int oldMax = maxHP;
			maxHP = 100;
			currentHP += (maxHP - oldMax);
		}
		// Xin
		else if (getCardName().equals("Kitten Xin")) {
			setCardName("Cat Xin");
			attack = 50;
			int oldMax = maxHP;
			maxHP = 100;
			currentHP += (maxHP - oldMax);
		}
		// Max status duelers
		else {
			System.out.println("This dueler is already at the highest status: Training card discarded.\n");
		}
	}

	
	
	public int getCurrentHP() {
		return this.currentHP;
	}
	
	public void setCurrentHP(int hp) {
		this.currentHP = hp;
	}
	
	public int getMaxHP() {
		return this.maxHP;
	}
	
	public void setMaxHP(int hp) {
		this.maxHP = hp;
	}
	
	public int getAttack() {
		return this.attack;
	}
	
	public void setAttack(int attack) {
		this.attack = attack;
	}
	
	public Team getTeam() {
		return this.team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public void checkHealth() {
		if (currentHP > maxHP) {
			currentHP = maxHP;
		}
	}
	
	public void incrementExperience() {
		experience++;
	}
	
	public int getExperience() {
		return experience;
	}
	
	public void resetExperience(){
		experience = 1;
	}
	
}
