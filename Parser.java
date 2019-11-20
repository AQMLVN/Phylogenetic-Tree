import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
	int count = 1;
	ArrayList<String> sequenceList = new ArrayList<String>();
	ArrayList<String> nameList = new ArrayList<String>();
	int match, mismatch, gap;
	
	public Parser() {
		Scanner scanner = new Scanner(new InputStreamReader(System.in));
		System.out.println("Please enter match score (default = 5): ");
		match = scanner.nextInt();
		System.out.println("Please enter mismatch score (default = -2): ");
		mismatch = scanner.nextInt();
		System.out.println("Please enter gap score (default = -1): ");
		gap = scanner.nextInt();
		scanner = new Scanner(new InputStreamReader(System.in));
		System.out.println("Please enter the input sequences: ");
		String input = null;
		String seq = "";
		//System.out.println(input.charAt(0));
		while (((input = scanner.nextLine())) != null) {
			if (input.equals("stop")) {
				break;
			}
			if (!(input.isEmpty())) {
				if (input.charAt(0) == ">".charAt(0)) {
					System.out.println(input + "-->  " + getCharForNumber(count));
					nameList.add(input);
					count++;
				}
				else {
					seq = seq + input;
				}
			}
			else {
				sequenceList.add(seq);
				seq = "";
			}
		}
		scanner.close();
	}
	
	public String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : "";
	}
}