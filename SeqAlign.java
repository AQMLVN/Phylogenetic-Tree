public class SeqAlign {
	
	int[][] alignMatrix;
	String[][] traceMatrix;
	String seqA;
	String seqB;
	String resA = "";
	String resB = "";
	double identity;
	int match = 5;
	int mismatch = -1;
	int gap = -2;

	public SeqAlign(String seqA, String seqB) {
		this.seqA = seqA;
		this.seqB = seqB;
		
		// Setting up matrices
		alignMatrix = new int[seqA.length()+1][seqB.length()+1];
		alignMatrix[0][0] = 0;
		traceMatrix = new String[seqA.length()+1][seqB.length()+1];
		for (int i = 1; i <= seqA.length(); i++) {alignMatrix[i][0] = alignMatrix[i-1][0] + gap;}
		for (int j = 1; j <= seqB.length(); j++) {alignMatrix[0][j] = alignMatrix[0][j-1] + gap;}
	}
	
	// Sets the config of alignment
	public void setConfig(int match, int mismatch, int gap) {
		this.match = match;
		this.mismatch = mismatch;
		this.gap = gap;
	}
	
	
	public void align() {
		// Filling out the alignment matrix
		for (int x = 1; x <= seqA.length(); x++) {
			for (int y = 1; y <= seqB.length(); y++) {
				if (seqA.charAt(x - 1) == seqB.charAt(y - 1)) {
					alignMatrix[x][y] = alignMatrix[x-1][y-1] + match;
					traceMatrix[x-1][y-1] = "dia=";
				}
				else {
					int up = alignMatrix[x-1][y] + gap;
					int left = alignMatrix[x][y-1] + gap;
					int diag = alignMatrix[x-1][y-1] + mismatch;
					alignMatrix[x][y] = Math.max(Math.max(up, left), diag);
					if (alignMatrix[x][y] == diag) {traceMatrix[x-1][y-1] = "diag";}
					else if (alignMatrix[x][y] == up) {traceMatrix[x-1][y-1] = "up--";}
					else if (alignMatrix[x][y] == left) {traceMatrix[x-1][y-1] = "left";}
				}
			}
		}
		
		// Tracing back
		int j = seqA.length()-1;
		int k = seqB.length()-1;
		int count = 0;
		
		while (j >= 0 && k >= 0) {
			if (traceMatrix[j][k] == "left") {
				resA = resA + "-";		
				resB = resB + seqB.charAt(k);
				k--;
			}
			else if (traceMatrix[j][k] == "up--") {
				resA = resA + seqA.charAt(j);
				resB = resB + "-";
				j--;
			}
			else if (traceMatrix[j][k] == "diag") {
				resB = resB + seqB.charAt(k);
				resA = resA + seqA.charAt(j);
				j--;
				k--;
			}
			else if (traceMatrix[j][k] == "dia=") {
				resB = resB + seqB.charAt(k);
				resA = resA + seqA.charAt(j);
				j--;
				k--;
				count++;
			}
		}
		
		// If j or k == 0, add gap
		while (j >= 0 && k < 0) {
			resA = resA + seqA.charAt(j);
			resB = resB + "-"; 
			j--;
			}
		while (k >= 0 && j < 0) {
			resB = resB + seqB.charAt(k); 
			resA = resA + "-";
			k--;
			}	
		
		// Flip the result
		resA = reverse(resA);
		resB = reverse(resB);
		
		identity = (double)count/resA.length();
		
	}
	
	// aligns a sequence to another FIXED sequence
	public void singleAlign(String seq) {
		seqB = resB;
		alignMatrix = new int[seq.length()+1][seqB.length()+1];
		alignMatrix[0][0] = 0;
		traceMatrix = new String[seq.length()+1][seqB.length()+1];
		for (int i = 1; i <= seq.length(); i++) {alignMatrix[i][0] = alignMatrix[i-1][0] + gap;}
		for (int y = 1; y <= seqB.length(); y++) {alignMatrix[0][y] = alignMatrix[0][y-1] + gap;}
		// Filling out the alignment matrix
		for (int x = 1; x <= seq.length(); x++) {
			for (int y = 1; y <= seqB.length(); y++) {
				if (seq.charAt(x - 1) == seqB.charAt(y - 1)) {
					alignMatrix[x][y] = alignMatrix[x-1][y-1];
					traceMatrix[x-1][y-1] = "dia=";
				}
				else {
					int left = alignMatrix[x][y-1] + gap;
					int diag = alignMatrix[x-1][y-1] + mismatch;
					alignMatrix[x][y] = Math.max(left, diag);
					if (alignMatrix[x][y] == diag) {traceMatrix[x-1][y-1] = "diag";}
					else if (alignMatrix[x][y] == left) {traceMatrix[x-1][y-1] = "left";}
					else {traceMatrix[x-1][y-1] = "diag";}
				}
			}
		}
		
		// Tracing back
		int j = seq.length()-1;
		int k = seqB.length()-1;
		int count = 0;
		resA = "";
		
		while (j >= 0 && k >= 0) {
			if (traceMatrix[j][k] == "left") {
				resA = resA + "-";		
				k--;
			}
			else if (traceMatrix[j][k] == "up--") {
				resA = resA + seq.charAt(j);
				j--;
			}
			else if (traceMatrix[j][k] == "diag") {
				resA = resA + seq.charAt(j);
				j--;
				k--;
			}
			else if (traceMatrix[j][k] == "dia=") {
				resA = resA + seq.charAt(j);
				j--;
				k--;
				count++;
			}
		}
		
		// If j or k == 0, add gap
		while (j >= 0 && k < 0) {
			resA = resA + seq.charAt(j);
			j--;
			}
		while (k >= 0 && j < 0) {
			k--;
			}	
		
		while (resA.length() < resB.length()) {
			resA = resA + "-";
		}
		// Flip the result
		resA = reverse(resA);
		identity = (double)count/resA.length();
	}
	
	public void printTrace() {
		for (int i = 0; i < traceMatrix.length-1; i++) {
		    for (int j = 0; j < traceMatrix[i].length-1; j++) {
		        System.out.print(traceMatrix[i][j] + " ");
		    }
		    System.out.println();
		}
	}
	
	public void printMatrix() {
		for (int i = 0; i < alignMatrix.length; i++) {
		    for (int j = 0; j < alignMatrix[i].length; j++) {
		    	if (alignMatrix[i][j] < 0) {
		    		System.out.print(alignMatrix[i][j] + " ");
		    	}
		    	else if (alignMatrix[i][j] >= 10) {System.out.print(alignMatrix[i][j] + " ");}
		    	else {System.out.print(" " + alignMatrix[i][j] + " ");}
		        
		    }
		    System.out.println();
		}
	}
	
	public String reverse(String original) {
	    return new StringBuilder(original).reverse().toString();
	}
	
}
