package msa;

import java.util.ArrayList;

public class MultipleSeq {

	
	
	public MultipleSeq() {}
	
	public void mAlign(ArrayList<String> seqs) {
		for (int i = 0; i < seqs.size(); i++) {
			for (int j = 0; j < seqs.size(); j++) {
				if (i != j) {
					SeqAlign sequencePair = new SeqAlign(seqs.get(i), seqs.get(j));
					sequencePair.align();
					System.out.println("Sequence " + i + " and Sequence " + j + ": identity = " + sequencePair.identity + ", gap = " + sequencePair.gap);
				}
			}
		}
	}
	
	public static void main (String[] args) {
		ArrayList<String> test = new ArrayList<String>();
		test.add("GCATGCU");
		test.add("GATTACA");
		test.add("CGTAGCA");
		test.add("CATAGAA");
		MultipleSeq mulTest = new MultipleSeq();
		mulTest.mAlign(test);
	}
	
}
