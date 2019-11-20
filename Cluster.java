public class Cluster {
	
	String sequence;
	Cluster group1 = null;
	Cluster group2 = null;
	Cluster parent = null;
	double height = 0;
	
	public Cluster(String sequence) {
		this.sequence = sequence;
	}
	
	public Cluster(Cluster s1, Cluster s2) {
		group1 = s1;
		group2 = s2;
	}
	
	public Cluster merge(Cluster s1, String name) {
		Cluster c = new Cluster(this, s1);
		c.sequence = name;
		return c;
	}
	
	public void addParent(Cluster c) {
		parent = c;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
}
