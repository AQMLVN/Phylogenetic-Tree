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

	public void createDistMatrix() {
		distMatrix = new double[alignedSeqs.size()][alignedSeqs.size()];
		for (int i = 0; i < alignedSeqs.size(); i++) {
			for (int j = 0; j < alignedSeqs.size(); j++) {
				distMatrix[i][j] = computeDist(alignedSeqs.get(i), alignedSeqs.get(j));
			}
		}
	}

	public void printDistMatrix() {
		for (int i = 0; i < distMatrix.length; i++) {
			for (int j = 0; j < distMatrix[i].length; j++) {
				System.out.print(distMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public int computeDist(String seq1, String seq2) {
		int count = 0;
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

	public void recomputeDistMatrix(int i, int j) {

	}

	public void createTree() {
		for (int i = 0; i < distMatrix.length; i++) {
			Cluster c = new Cluster(alignedSeqs.get(i));
			result.add(c);
		}
		for (int i = 0; i < distMatrix.length; i++) {
			System.out.println(i);
			System.out.println(result.get(i).sequence);
		}

		while (result.size() > 2) {
			System.out.println("LOOP!!!");
			double val = getMinValue(distMatrix);
			
			System.out.println("get x = " + result.get(x).sequence + " " + x);
			System.out.println("get y = " + result.get(y).sequence + " " + y);
			
			Cluster temp = result.get(x).merge(result.get(y), "grouping " + result.size());
			temp.setHeight(val/2);
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
					//countx = i;
					//county = j;
					if (i == j) {newDist[i][j] = 0;}
					else {
						if (i == 1) {countx = 0;}
						if (j == 1) {county = 0;}
						//if (countx >= distMatrix.length) {countx = 0;}
						//if (county >= distMatrix.length) {county = 0;}
						while (countx == x || countx == y) {countx++;}
						while (county == y || county == x) {county++;}
						

						
						newDist[i][j] = distMatrix[countx][county];
						newDist[j][i] = distMatrix[countx][county];
						

						}
					
				}
			}
			
			// new value
			System.out.println("x = " + x);
			System.out.println("y = " + y);
			System.out.println(distMatrix.length-1);
			for (int i = 0; i < distMatrix.length-1; i++) {
				double value = 0;
				if (i < y) {value = (distMatrix[i][x] + distMatrix[i][y])/2;}
				if (i >= y) {value = (distMatrix[i+1][x] + distMatrix[i+1][y])/2;}
				if (i == x) {value = 0;}
				newDist[x][i] = value;
				newDist[i][x] = value;
			}
			distMatrix = newDist;
			printDistMatrix();
		}
		//double val = getMinValue(distMatrix);
		System.out.println("x = " + x + "and y = " + y);
		
		double val = getMinValue(distMatrix);
		Cluster temp = result.get(x).merge(result.get(y), "grouping " + result.size());
		temp.setHeight(val/2);
		result.remove(x);
		if (x < y) {result.remove(y-1);}
		else {result.remove(y);}
		result.add(0, temp);
		
		System.out.println("final val = " + val);
		System.out.println("result - " + result);

	}


	public double getMinValue(double[][] numbers) {
		double minValue = 999;
		System.out.println("min value - matrix size = ");
		System.out.println(numbers.length-1);
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
	
	public void printNewick(Cluster result) {
		int index = 0;
		for (int j = 0; j < alignedSeqs.size(); j++) {
			if (result.sequence == alignedSeqs.get(j)) {index = j+1;}
		}
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
	
	private String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : "";
	}

	public static void main(String[] args) {
		ArrayList<String> test = new ArrayList<String>();
		test.add("TCAGGATGAAC");
		test.add("ATCACGATGAACC");
		test.add("ATCAGGAATGAATCC");
		test.add("TCACGATTGAATCGC");
		for (int i = 0; i < test.size(); i++) {
			System.out.println(test.get(i));
		}
		MultipleSeq mulTest = new MultipleSeq();
		mulTest.createSimilarityMatrix(test);
		mulTest.alignOrder();
		mulTest.msAlign();
		System.out.println("\n########################################################################\n");
		System.out.println(mulTest.alignedSeqs.size());
		System.out.println(mulTest.alignedSeqs);
		System.out.println("____________________________");
		for (int i = 0; i < mulTest.alignedSeqs.size(); i++) {
			System.out.println(mulTest.alignedSeqs.get(i));
			System.out.println();
		}
		System.out.println("\n########################################################################\n");
		PhyloTree tree = new PhyloTree(mulTest.alignedSeqs);
		tree.createDistMatrix();
		tree.printDistMatrix();
		tree.createTree();
		tree.printTree(tree.result);
		System.out.println("@@@@@@@@@@@@@@ FINAL RESULT @@@@@@@@@@@@@@@@");
		tree.printNewick(tree.result);
	}

}
