/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name> Safin Rashid
 * <Student1 EID> srr3288
 * <Student1 5-digit Unique No.> 17155
 * <Student2 Name> Roy Mor
 * <Student2 EID> rm58977
 * <Student2 5-digit Unique No.> 17150
 * Slip days used: <0>
 * Git URL: https://github.com/EE422C/sp-23-assignment-3-sp23-pair-4.git
 * Spring 2023
 */

package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static char[] letters;
	
	public static void main(String[] args) throws Exception { //ASK IF THEY CALL MAIN IN  OFFICE HOURS
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.

		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();

		while(true) {
			ArrayList<String> input = parse(kb);
			if(input == null) break;
			String start = input.get(0);
			String end = input.get(1);

			ArrayList<String> BFS = getWordLadderBFS(start, end);
			//ArrayList<String> DFS = getWordLadderDFS(start, end);

			printLadder(BFS);
			//System.out.println();
			//printLadder(DFS);
		}
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {

		ArrayList<String> parsed = new ArrayList<String>();
		String input = keyboard.next();

		if(input.equals("/quit")) return null;

		parsed.add(input);
		parsed.add(keyboard.next());
		return parsed;
	}
	
	/**
	 * Finds a word ladder between the start and end word with a recursive implementation of DFS
	 * @param start the start word
	 * @param end the end word
	 * @return an ArrayList with the ladder between the start and end word, contains only start and end if there is no word ladder
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		StringParent startP = new StringParent(null, start.toUpperCase());
		StringParent endP = new StringParent(null, end.toUpperCase()); //Initiating StringParents of the start and end word
		ArrayList<String> output = new ArrayList<String>(); 
		
		Set<String> dict = makeDictionary(); //HashSet of all possible words
		
//		HashMap<StringParent, ArrayList<StringParent>> adjacencyList = new HashMap<>();
//		adjacencyList.put(startP, new ArrayList<StringParent>());
		
		HashSet<String> discovered = new HashSet<>(); //Hashset of discovered words
		discovered.add(startP.value);
		
		
		// Returned list should be ordered start to end. Include start and end.
		// If ladder is empty, return list with just start and end.
		// TODO some code

		endP = getDFSRecursive(startP, endP, dict,/* adjacencyList,*/ discovered);
		if(endP==null) { //get DFSRecursive returns null if there is no path
			output.add(start);
			output.add(end);
			return output;
		}
		StringParent outputWord = endP; 
		do { //Cycling from end to start into a list would have a backwards word ladder, thus add to output then traverse output in reverse into ladder
			outputWord = outputWord.parent;
			output.add(outputWord.value.toLowerCase());
		} while(outputWord.parent!=null);
		
		ArrayList<String> ladder = new ArrayList<String>();
		for(int i = output.size() - 1; i >= 0; i--){ //reverse output into ladder format
			ladder.add(output.get(i));
		}
		ladder.add(end);
		return ladder; // replace this line later with real return
	}
	
	/**
	 * The recursive aspect of DFS, finds all adjacent words to start and calls itself on them, if dead end, return null
	 * @param start
	 * @param end
	 * @param dict
	 * @param discovered
	 * @return a StringParent of the end word with parent != null
	 */
	public static StringParent getDFSRecursive(StringParent start, StringParent end, Set<String> dict, /*HashMap<StringParent, ArrayList<StringParent>> adjList,*/ HashSet<String> discovered) {
		char[] currentWord;
		String checkWord;
		StringParent checkW;
		for(int i = 0; i < 5; i++) { //Loop through each letter position of word
			for(char a = 'A'; a <= 'Z'; a++) { //Replace with every other letter
				currentWord = start.value.toCharArray();
				currentWord[i] = a;
				checkWord = String.valueOf(currentWord);
				checkW = new StringParent(start, checkWord);
				if(checkWord.equals(end.value)) { //Return the StringParent of end if found
					return checkW;
				}
				if(!discovered.contains(checkWord)&&dict.contains(checkWord)) { //Else if not in discovered and in dictionary
					discovered.add(checkWord); //Add to discovered and call itself with new word as start
//					adjList.get(start).add(checkW);
					StringParent recurse = getDFSRecursive(checkW, end, dict, /*adjList,*/ discovered);  //Pass the dictionary and discovered in
					if(recurse != null) { //null check
						if(recurse.value.equals(end.value)) { //If end found, keep exiting the recursive chain
							return recurse;
						}
					}
				}
			}
		}
		
		return null; //If dead end, return null
	}
	
	/**
	 * Finds a word ladder between the start word and end with with BFS
	 * @param start the start word
	 * @param end the end word
 	 * @return an ArrayList of the word ladder from start to end, contains only start and end if there is no word ladder
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	StringParent startP = new StringParent(null, start.toUpperCase());
    	StringParent endP = new StringParent(null, end.toUpperCase()); //StringParent verions of start and end, end's parent will be set once it is found
		ArrayList<String> output = new ArrayList<String>();
		Set<String> dict = makeDictionary();
		
		ArrayList<StringParent> queue = new ArrayList<StringParent>();
		queue.add(startP); //Add start to the queue
		HashSet<String> discovered = new HashSet<>();
		int level = 0; //The layer being searched
		
		discovered.add(start.toUpperCase());
		StringParent currentW;
		char[] currentWord;
		char temp;
		String checkWord;
		StringParent checkW;
		findEnd:
		while(queue.size() > 0){

			level = queue.size();

			for(int i = 0; i < level; i++){ //for each word at level
				currentW = queue.get(0);
				currentWord = queue.get(0).value.toCharArray(); //extract from queue
				queue.remove(0);

				for(int j = 0; j < 5; j++){ //for each letter in word
					temp = currentWord[j];
					String word = String.valueOf(currentWord).toLowerCase();
					if(word.equals(end)){
						endP.parent = currentW;
						queue.clear();
						break findEnd;
					}
					for(char a = 'a'; a <= 'z'; a++){ //for each alphabet letter
						currentWord[j] = a;
						checkWord = String.valueOf(currentWord).toUpperCase();
						if(dict.contains(checkWord) && ! discovered.contains(checkWord)) {
							checkW = new StringParent(currentW, checkWord);
							discovered.add(checkWord);
							queue.add(checkW);
							if(checkWord.equals(end)){
								endP.parent = currentW; 
								queue.clear();
								break findEnd;
							}
							break;
						}
					}

					currentWord[j] = temp;
				}
			}
		}
		if(endP.parent==null) {//no word ladder found
			output.add(start);
			output.add(end);
			return output;
		}
		StringParent outputWord = endP;
		do {
			outputWord = outputWord.parent;
			output.add(outputWord.value.toLowerCase());
		} while(outputWord.parent!=null);

		ArrayList<String> ladder = new ArrayList<String>();
		for(int i = output.size() - 1; i >= 0; i--){ //reverse output into ladder format
			ladder.add(output.get(i));
		}

		return ladder; // replace this line later with real return
	}

    
    /**
     * Validates that all words in a word ladder are adjacent 
     * @param ladder the ladder to scan
     * @return true if ladder valid, false otherwise
     */
	public static boolean validLadder(ArrayList<String> ladder){

		char[] first, second;

		for(int i = 1; i < ladder.size(); i++){
			first = ladder.get(i-1).toCharArray();
			second = ladder.get(i).toCharArray();
			int count = 0;
			for(int j = 0; j < 5; j++){
				if(first[j] != second[j]) count++;
			}
			if(count != 1) return false;
		}

		return true;
	}
	
	
	/**
	 * Prints out a ladder
	 * @param ladder the word ladder to print
	 */
	public static void printLadder(ArrayList<String> ladder) {
		
		if(ladder.size() <= 2){ //If ladder contains only start and end
			String start = ladder.get(0);
			String finish = ladder.get(1);
			System.out.println("no word ladder can be found between " + start + " and " + finish + ".");
			return;
		}

		if(! validLadder(ladder)) {//No point printing an invalid ladder
			System.out.println("Not valid ladder!");
			return;
		}

		int N = ladder.size() - 2; //Print valid ladder
		String start = ladder.get(0), finish = ladder.get(ladder.size() - 1);
		System.out.println("a " + N + "-rung word ladder exists between " + start + " and " + finish + ".");
		for(int i = 0; i < ladder.size(); i++){
			System.out.println(ladder.get(i));
		}
	}

	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
