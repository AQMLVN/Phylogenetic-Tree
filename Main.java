
public class Main {
	public Main() {}
	
	public static void main(String[] args) {
		Parser parse = new Parser();
		for (int i = 0; i < parse.sequenceList.size(); i++) {
			System.out.println(parse.sequenceList.get(i));
		}
	}
	
}
