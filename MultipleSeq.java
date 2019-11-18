import java.text.DecimalFormat;
import java.util.ArrayList;

public class MultipleSeq {

	double[][] similarityMatrix;
	ArrayList<Integer> order = new ArrayList<Integer>();
	ArrayList<String> seqs;
	ArrayList<String> alignedSeqs = new ArrayList<String>();
	
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
		SeqAlign sequencePair = null;
		for (int i = 0; i < order.size(); i++) {
			if (i == 0) {
				sequencePair = new SeqAlign(seqs.get(order.get(i)), seqs.get(order.get(i+1)));
				sequencePair.align();
				alignedSeqs.add(sequencePair.resA);
				alignedSeqs.add(sequencePair.resB);
				System.out.println("Sequence " + "0" + " and Sequence " + "1" + ": identity = " + sequencePair.identity + ", gap = " + sequencePair.gap);
				}
			else {
				if (i == order.size()-1) {return;}
				sequencePair.singleAlign(seqs.get(order.get(i+1)));
				alignedSeqs.add(sequencePair.resA);
			}
		}
		//SeqAlign sequencePair = new SeqAlign(seqs.get(i), seqs.get(j));
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
		test.add("CATGCGAGTAGTAG");
		test.add("CATGGTAGTAG");
		test.add("CCTGGAGTACGTAG");
		test.add("CATGAGCGTAG");
		MultipleSeq mulTest = new MultipleSeq();
		mulTest.createSimilarityMatrix(test);
		mulTest.printSimMatrix();
		mulTest.alignOrder();
		System.out.println("\n" + mulTest.order);
		mulTest.msAlign();
		System.out.println(mulTest.alignedSeqs.size());
		System.out.println(mulTest.alignedSeqs);
		for (int i = 0; i < mulTest.alignedSeqs.size(); i++) {
			System.out.println(mulTest.alignedSeqs.get(i));
		}
	}
	
}
