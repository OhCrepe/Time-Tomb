package main.Objects;

public class vPot {

	public int index;
	public double distance, mDistance;
	
	public vPot(int i, double d, double m){
		
		this.index = i;
		this.distance = d;
		this.mDistance = m;
		
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getD() {
		return distance;
	}

	public void setD(double d) {
		this.distance = d;
	}
	
	public void setM(double d){
		this.mDistance = d;
	}
	
	public double getMouseD(){
		return mDistance;
	}

}
