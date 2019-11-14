public class Cluster {
	
	String sequence;
	Cluster cluster2;
	Cluster cluster3;
	double height = 0;
	double clustHeight = 0;
	
	public Cluster(String sequence) {
		this.sequence = sequence;
	}
	
	public void merge(Cluster s) {
		cluster2 = s;
	}
	
	public void clustMerge(Cluster s) {
		cluster3 = s;
	}
	
	public Boolean checkMerge() {
		if (cluster2 != null) {return false;}
		else {return true;}
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public void setClustHeight(double height) {
		this.clustHeight = height;
	}
	
}
