import java.text.DecimalFormat;
import java.util.ArrayList;

public class MultipleSeq {

	double[][] similarityMatrix;
	ArrayList<Integer> order = new ArrayList<Integer>();
	ArrayList<String> seqs;
	ArrayList<String> alignedSeqs;
	int match = 5;
	int mismatch = -2;
	int gap = -1;
	
	public MultipleSeq() {}
	
	// Create similarity matrix for all sequences
	public void createSimilarityMatrix(ArrayList<String> seqs) {
		this.seqs = seqs;
		similarityMatrix = new double[seqs.size()][seqs.size()];
		for (int i = 0; i < seqs.size(); i++) {
			for (int j = 0; j < seqs.size(); j++) {
				if (i != j) {
					SeqAlign sequencePair = new SeqAlign(seqs.get(i), seqs.get(j));
					sequencePair.setConfig(match, mismatch, gap);
					sequencePair.align();
					similarityMatrix[i][j] = sequencePair.identity;
				}
			}
		}
	}
	
	// Sets the config of alignment
	public void setConfig(int match, int mismatch, int gap) {
		this.match = match;
		this.mismatch = mismatch;
		this.gap = gap;
	}
	
	// Multiple Sequence Alignment
	public void msAlign() {
		SeqAlign sequencePair = null;
		alignedSeqs = new ArrayList<String>(order.size());
		for (int i = 0; i < order.size(); i++) {
			alignedSeqs.add("");
		}
		for (int i = 0; i < order.size(); i++) {
			if (i == 0) {
				sequencePair = new SeqAlign(seqs.get(order.get(i)), seqs.get(order.get(i+1)));
				sequencePair.setConfig(match, mismatch, gap);
				sequencePair.align();
				alignedSeqs.set(order.get(i), sequencePair.resA);
				alignedSeqs.set(order.get(i+1), sequencePair.resB);
				}
			else {
				if (i == order.size()-1) {return;}
				sequencePair.singleAlign(seqs.get(order.get(i+1)));
				alignedSeqs.set(order.get(i+1), sequencePair.resA);
			}
		}
	}
	
	// Determines order of progressive alignment based on distance
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
	
	// Helper method for alignOrder()
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
	
	// Prints similarity Matrix
	public void printSimMatrix() {
		DecimalFormat f = new DecimalFormat("##.00"); // round to 2 decimals
		for (int i = 0; i < similarityMatrix.length; i++) {
		    for (int j = 0; j < similarityMatrix[i].length; j++) {
		        System.out.print(f.format(similarityMatrix[i][j]) + " ");
		    }
		    System.out.println();
		}
	}
	
}
