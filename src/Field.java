
public class Field {
	private Card[] p1Bench;
	private Card[] p2Bench;
	
	
	public Field(Card[] player1Bench, Card[] player2Bench) {
		p1Bench = player1Bench;
		p2Bench = player2Bench;
	}

	public void printField() {
		
		System.out.println("-------------------------------------");
		System.out.println("\nPlayer 1 Bench: ");
		
		for(int i = 1; i < p1Bench.length; i++){
			if (p1Bench[i] == null) {
				System.out.println(i + ") " + "empty");		
			}
			else {
				System.out.println(i + ") " + (p1Bench[i].getCardName()));		
			}
			
		}
		
		
		System.out.println("\nPlayer 1 Arena:         Player 2 Arena: ");		
		
		if (p1Bench[0] == null || p2Bench[0] == null) {
			
			if (p1Bench[0] == null && p2Bench[0] == null) {
				System.out.print("empty");	
				System.out.print("\t\t\t");
				System.out.print("empty");	
				System.out.println("");	
			}
			else if (p1Bench[0] == null) {		
				System.out.print("empty");	
				System.out.print("\t\t");
				System.out.print((p2Bench[0].getCardName()));	
				System.out.println("");			
			}
			
			else {
				System.out.print((p1Bench[0].getCardName()));	
				System.out.print("\t\t");
				System.out.print("empty");	
				System.out.println("");				
			}
		}
		

		else {
			System.out.print((p1Bench[0].getCardName()));	
			System.out.print("\t\t");
			System.out.print((p2Bench[0].getCardName()));	
			System.out.println("");
		}
		
		System.out.println("\nPlayer 2 Bench: ");
		
		for(int i = 1; i < p2Bench.length; i++){
			if (p2Bench[i] == null) {
				System.out.println(i + ") " + "empty");		
			}
			else {
				System.out.println(i + ") " + (p2Bench[i].getCardName()));		
			}
		}
		
		System.out.println("");
		
		System.out.println("-------------------------------------");
	
	}

}


