import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;

public class DuelingIO {

    BufferedReader _reader;
    
    // Constructor 1 which allows the user to specify a specific file to read from
    public DuelingIO(String fileName) {
        try {
			_reader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("input file not found!");
		}
    }

    //default constructor which uses System.in
    public DuelingIO() {
    	_reader = new BufferedReader(new InputStreamReader(System.in));
    }
    
    
    // Output: the next line read from the reader.
    public String getNextInput() throws IOException {
        return _reader.readLine();
    }
    
    // Output: An array of Strings where the first element is the first line
    // of the input file.  The array is shuffled when the word "shuffle" is found.
    public static String[] getDeck(String fileName) throws IOException {
        BufferedReader fReader = new BufferedReader(new FileReader(fileName));
        LinkedList<String> toReturn = new LinkedList<String>();
        
        String s = fReader.readLine();
        while (s != null) {
            // Shuffles the deck so far when the word "shuffle is found"
            if (s.equals("shuffle")) {
                Collections.shuffle(toReturn);
            } else {
              toReturn.addLast(s); 
            }
            s = fReader.readLine();
        }
        fReader.close();
        return toReturn.toArray(new String[(toReturn.size())]);
    }
}