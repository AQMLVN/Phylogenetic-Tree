package msa;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MultipleSeq {

	double[][] similarityMatrix;
	ArrayList<Integer> order = new ArrayList<Integer>();
	ArrayList<String> seqs;
	ArrayList<String> alignedSeqs;
	
	public MultipleSeq() {}
	
	public void createSimilarityMatrix(ArrayList<String> seqs) {
		this.seqs = seqs;
		similarityMatrix = new double[seqs.size()][seqs.size()];
		for (int i = 0; i < seqs.size(); i++) {
			for (int j = 0; j < seqs.size(); j++) {
				if (i != j) {
					SeqAlign sequencePair = new SeqAlign(seqs.get(i), seqs.get(j));
					sequencePair.align();
					System.out.println("Sequence " + i + " and Sequence " + j + ": identity = " + sequencePair.identity + ", gap = " + sequencePair.gap);
					similarityMatrix[i][j] = sequencePair.identity;
				}
			}
		}
	}
	
	public void msAlign() {
		
	}
	
	public void alignOrder() {
		int x = -1;
		int y = -1;
		double temp = 0;
		for (int i = 0; i < seqs.size(); i++) {
			for (int j = 0; j < seqs.size(); j++) {
				if (temp < similarityMatrix[i][j]) {
					temp = similarityMatrix[i][j];
					x = i; y = j;
				}
			}
		}
		if (x != -1) {order.add(x);}
		if (y != -1) {order.add(y);}
		alignOrder(temp);
		
	}
	
	public void alignOrder(double max) {
		int x = -1;
		int y = -1;
		double temp = 0;
		for (int i = 0; i < seqs.size(); i++) {
			for (int j = 0; j < seqs.size(); j++) {
				if (temp < similarityMatrix[i][j] && similarityMatrix[i][j] < max) {
					temp = similarityMatrix[i][j];
					x = i; y = j;
				}
			}
		}
		if (x == y) {return;}
		if (x != -1) {if (!(order.contains(x))) {order.add(x);}}
		if (y != -1) {if (!(order.contains(y))) {order.add(y);}}
		alignOrder(temp);
		
	}
	
	public void printSimMatrix() {
		DecimalFormat f = new DecimalFormat("##.00"); // round to 2 decimals
		for (int i = 0; i < similarityMatrix.length; i++) {
		    for (int j = 0; j < similarityMatrix[i].length; j++) {
		        System.out.print(f.format(similarityMatrix[i][j]) + " ");
		    }
		    System.out.println();
		}
	}
	
	public static void main (String[] args) {
		ArrayList<String> test = new ArrayList<String>();
		test.add("GCATGCU");
		test.add("GATTACA");
		test.add("CGTAGCA");
		test.add("CATAGAA");
		MultipleSeq mulTest = new MultipleSeq();
		mulTest.createSimilarityMatrix(test);
		mulTest.printSimMatrix();
		mulTest.alignOrder();
		System.out.println("\n" + mulTest.order);
	}
	
}
