import java.util.ArrayList;

public class PhyloTree {

	double[][] distMatrix;
	ArrayList<String> alignedSeqs;
	ArrayList<Cluster> result = new ArrayList<Cluster>();
	int x = -1;
	int y = -1;

	public PhyloTree(ArrayList<String> alignedSeqs) {
		this.alignedSeqs = alignedSeqs;
	}

	// Creates distance matrix for UPGMA
	public void createDistMatrix() {
		distMatrix = new double[alignedSeqs.size()][alignedSeqs.size()];
		for (int i = 0; i < alignedSeqs.size(); i++) {
			for (int j = 0; j < alignedSeqs.size(); j++) {
				distMatrix[i][j] = computeDist(alignedSeqs.get(i), alignedSeqs.get(j));
			}
		}
	}

	// Prints distance matrix
	public void printDistMatrix() {
		for (int i = 0; i < distMatrix.length; i++) {
			for (int j = 0; j < distMatrix[i].length; j++) {
				System.out.print(distMatrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	// Computes distance between 2 sequences
	public double computeDist(String seq1, String seq2) {
		double count = 0;
		String compare;
		String result;
		if (seq1.length() < seq2.length()) {compare = seq1; result = seq2;}
		else {compare = seq2; result = seq1;}
		for (int i = 0; i < compare.length(); i++) {
			if (seq1.charAt(i) == seq2.charAt(i)) {
				count++;
			}
		}
		return result.length() - count;
	}

	// Create phylogenetic tree using UPGMA
	public void createTree() {
		// Creates a cluster for each sequence being tested
		for (int i = 0; i < distMatrix.length; i++) {
			Cluster c = new Cluster(alignedSeqs.get(i));
			result.add(c);
		}

		while (result.size() > 2) {
			double val = getMinValue(distMatrix);
			
			Cluster temp = result.get(x).merge(result.get(y), "grouping " + result.size());
			temp.setHeight(val/2);
			result.get(x).addParent(temp);
			result.get(y).addParent(temp);
			result.remove(x);
			if (x < y) {result.remove(y-1);}
			else {result.remove(y);}
			result.add(0, temp);
			
			// reconstruct matrix
			double[][] newDist = new double[distMatrix.length-1][distMatrix.length-1];
			int countx = 0;
			int county = 0;
			for (int i = 1; i < distMatrix.length-1; i++, countx++) {
				for (int j = 1; j < distMatrix.length-1; j++, county++) {
					if (i == j) {newDist[i][j] = 0;}
					else {
						if (i == 1) {countx = 0;}
						if (j == 1) {county = 0;}
						while (countx == x || countx == y) {countx++;}
						while (county == y || county == x) {county++;}
						newDist[i][j] = distMatrix[countx][county];
						newDist[j][i] = distMatrix[countx][county];
					}
				}
			}
			
			// new values
			for (int i = 1, j = 0; i < distMatrix.length-1; i++, j++) {
				double value = 0;
				//value = (distMatrix[i][x] + distMatrix[i][y])/2;
				//if (x == 0 || y == 0) {j++;}
				while (j == x || j == y) {j++;}
				value = (distMatrix[j][x] + distMatrix[j][y])/2;
				newDist[0][i] = value;
				newDist[i][0] = value;
			}
			distMatrix = newDist;
			printDistMatrix();
		}
		
		double val = getMinValue(distMatrix);
		Cluster temp = result.get(x).merge(result.get(y), "grouping " + result.size());
		temp.setHeight(val/2);
		result.get(x).addParent(temp);
		result.get(y).addParent(temp);
		result.remove(x);
		if (x < y) {result.remove(y-1);}
		else {result.remove(y);}
		result.add(0, temp);

	}


	public double getMinValue(double[][] numbers) {
		double minValue = 999;
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers.length; j++) {
				if (numbers[i][j] < minValue && numbers[i][j] != 0) {
					minValue = numbers[i][j];
					x = i;
					y = j;
				}
			}
		}
		return minValue ;
	}
	
	public void printTree(ArrayList<Cluster> result) {
		for (int i = 0; i < result.size(); i++) {
			System.out.println("Cluster " + i + ": ");
			System.out.println(
					"height = " + result.get(i).height + "\n" +
					"seq = " + result.get(i).sequence 
					);
			if (result.get(i).group1 != null) {
				System.out.println(result.get(i).group1.sequence + " belongs to " + result.get(i).sequence);
				printTree(result.get(i).group1);
			}
			if (result.get(i).group2 != null) {
				System.out.println(result.get(i).group2.sequence + " belongs to " + result.get(i).sequence);
				printTree(result.get(i).group2);
			}
		}
		
	}
	
	public void printTree(Cluster result) {
		System.out.println("secondary");
		System.out.println(
				"height = " + result.height + "\n" +
				"seq = " + result.sequence 
				);
		if (result.group1 != null) {
			System.out.println(result.group1.sequence + " belongs to " + result.sequence);
			printTree(result.group1);
		}
		if (result.group2 != null) {
			System.out.println(result.group2.sequence + " belongs to " + result.sequence);
			printTree(result.group2);
		}
		
	}
	
	public void printNewick(ArrayList<Cluster> result) {
		int index = 0;
		for (int i = 0; i < result.size(); i++) {
			for (int j = 0; j < alignedSeqs.size(); j++) {
				if (result.get(i).sequence == alignedSeqs.get(j)) {index = j+1;}
			}
			if (result.get(i).height == 0) {System.out.print(getCharForNumber(index)+ ":" + result.get(i).parent.height );}
			else {
				System.out.print(getCharForNumber(index) + "(");
				if (result.get(i).group1 != null) {
					printNewick(result.get(i).group1);
				}
				System.out.print(",");
				if (result.get(i).group2 != null) {
					printNewick(result.get(i).group2);
				}
				System.out.print(")");
			}
			
		}
		
	}
	
	public void printNewick(Cluster result) {
		int index = 0;
		for (int j = 0; j < alignedSeqs.size(); j++) {
			if (result.sequence == alignedSeqs.get(j)) {index = j+1;}
		}
		if (result.height == 0) {System.out.print(getCharForNumber(index)+ ":" + result.parent.height );}
		else {
			System.out.print(getCharForNumber(index) + "(");
			if (result.group1 != null) {
				printNewick(result.group1);
			}
			System.out.print(",");
			if (result.group2 != null) {
				printNewick(result.group2);
			}
			System.out.print(")");
		}
		
		
	}
	
	private String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : "";
	}

	public static void main(String[] args) {
		ArrayList<String> test = new ArrayList<String>();
		test.add("AAAGTTAATGAGTGGTTATCCAGAAGTAGTGACATTTTAGCCTCTGATAACTCCAACGGTAGGAGCCATGAGCAGAGCGCAGAGGTGCCTAGTGCCTTAGAAGATGGGCATCCAGATACCGCAGAGGGAAATTCTAGCGTTTCTGAGAAGACTGAC" + 
				"");
		test.add("AAAGTTAATGAGTGGTTATTCAGAAGTAATGACGTTTTAGCCCCAGATTACTCAAGTGTTAGGAGCCATGAACAGAATGCAGAGGCAACCAATGCTTTAGAATATGGGCATGTAGAGACA---GATGGAAATTCTAGCATTTCTGAAAAGACTGAT");
		test.add("AAAGTTAACGAGTGGTTTTCCAGAGGTGATGACATATTAACTTCTGATGACTCACACGATAGGGGGTCTGAATTAAATGCAGAAGTAGCTGGTGCATTGAAAGTT------TCAAAAGAAGTAGATGAATATTCTAGTTTTTCAGAGAAGATAGAC" + 
				"");
		test.add("AAAGTTAATGAGTGGTTTTCCAGAAGTGATGACATACTAACTTCTGATGACTCACACAATGGGGGGTCTGAATCAAATGCAGAAGTAGTTGGTGCATTGAAAGTT------CCAAATGAAGTAGATGGATATTCTGGTTCTTCAGAGAAGATAGAC" + 
				"");
		test.add("AAAGTTAATGAGTGGTTTTTCAGAAGTGATGGCCTG---------GATGACTTGCATGATAAGGGGTCTGAGTCAAATGCAGAAGTAGCTGGTGCTTTAGAAGTT------CCAGAAGAAGTACATGGATATTCTAGTTCTTCAGAGAAAATAGAC" + 
				"");
		test.add("AAAGTTAATGAGTGGTTTTCTAGAAGCGATGAAATGTTAACTTCTGACGACTCACAGGACAGGAGGTCTGAATCAAATACTGGGGTAGCTGGTGCAGCAGAGGTT------CCAAATGAAGCAGATGGACATTTGGGTTCTTCAGAGAAAATAGAC" + 
				"");
		test.add("AAAGTGAATGAATGGCTTTCCAGAAGTGATGAACTGTTAACTTCTGATGACTCATATGATAAGGGATCTAAATCAAAAACTGAAGTAACTGTAACAACAGAAGTT------CCAAATGCAATAGATAGRTTTTTTGGTTCTTCAGAGAAAATAAAC" + 
				"");
		test.add("AAAGTTAATGAGTGGTTTTCCAGAAGTGATGAACTGTTAGGTTCTGATGACTCACATGATGGGGAGTCTGAATCAAATGCCAAAGTAGCTGATGTATTGGACGTT------CTAAATGAGGTAGATGAATATTCTGGTTCTTCAGAGAAAATAGAC" + 
				"");
		test.add("AAAGTGAATGAGTGGTTTTCCAGAACTGGTGAAATGTTAACTTCTGACAATGCATCTGACAGGAGGCCTGCGTCAAATGCAGAAGCTGCTGTTGTGTTAGAAGTT------TCAAATGAAGTGGATGGATGTTTCAGTTCTTCAAAGAAAATAGAC" + 
				"");
		
		MultipleSeq mulTest = new MultipleSeq();
		mulTest.createSimilarityMatrix(test);
		mulTest.alignOrder();
		mulTest.msAlign();
		PhyloTree tree = new PhyloTree(mulTest.alignedSeqs);
		for (int i = 0; i < test.size(); i++) {
			System.out.println(test.get(i));
		}
		tree.createDistMatrix();
		init in = new init();
		// inserts distance matrix from phylo tool
		tree.distMatrix = in.list;
		tree.printDistMatrix();
		tree.createTree();
		//tree.printTree(tree.result);

		System.out.println("@@@@@@@@@@@@@@ FINAL RESULT @@@@@@@@@@@@@@@@");
		tree.printNewick(tree.result);
		System.out.println(":::::");
		System.out.println(tree.computeDist("AAAGTTAATGAGTGGTTATCCAGAAGTAGTGACATTTTAGCCTCTGATAACTCCAACGGTAGGAGCCATGAGCAGAGCGCAGAGGTGCCTAGTGCCTTAGAAGATGGGCATCCAGATACCGCAGAGGGAAATTCTAGCGTTTCTGAGAAGACTGAC", "AAAGTTAATGAGTGGTTTTCCAGAAGTGATGAACTGTTAGGTTCTGATGACTCACATGATGGGGAGTCTGAATCAAATGCCAAAGTAGCTGATGTATTGGACGTT------CTAAATGAGGTAGATGAATATTCTGGTTCTTCAGAGAAAATAGAC"));
		System.out.println(tree.computeDist("AAAGTTAATGAGTGGTTATCCAGAAGTAGTGACATTTTAGCCTCTGATAACTCCAACGGTAGGAGCCATGAGCAGAGCGCAGAGGTGCCTAGTGCCTTAGAAGATGGGCATCCAGATACCGCAGAGGGAAATTCTAGCGTTTCTGAGAAGACTGAC", "AAAGTGAATGAGTGGTTTTCCAGAACTGGTGAAATGTTAACTTCTGACAATGCATCTGACAGGAGGCCTGCGTCAAATGCAGAAGCTGCTGTTGTGTTAGAAGTT------TCAAATGAAGTGGATGGATGTTTCAGTTCTTCAAAGAAAATAGAC"));
	}

}
