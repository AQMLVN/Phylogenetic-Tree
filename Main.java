
public class Main {
	public Main() {}
	
	public static void main(String[] args) {
		Parser parse = new Parser();
		MultipleSeq mulTest = new MultipleSeq();
		mulTest.setConfig(parse.match, parse.mismatch, parse.gap);
		mulTest.createSimilarityMatrix(parse.sequenceList);
		mulTest.alignOrder();
		mulTest.msAlign();
		PhyloTree tree = new PhyloTree(mulTest.alignedSeqs);
		tree.createDistMatrix();
		tree.printDistMatrix();
		tree.createTree();
		//tree.printTree(tree.result);
		System.out.println("---------- FINAL RESULT ----------");
		tree.printNewick(tree.result);
		System.out.println("\n\n---------- REFERENCE ----------");
		for (int i = 0; i < parse.sequenceList.size(); i++) {
			System.out.println(parse.getCharForNumber(i+1) + " ----> " + parse.nameList.get(i).substring(0, parse.nameList.get(i).length()-1));
			System.out.println(parse.sequenceList.get(i) + "\n");
		}
	}
	
}
