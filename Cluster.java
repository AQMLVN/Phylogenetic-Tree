public class Cluster {
	
	String sequence;
	Cluster cluster2;
	double height = 0;
	
	public Cluster(String sequence) {
		this.sequence = sequence;
	}
	
	public void merge(Cluster s) {
		cluster2 = s;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
}
