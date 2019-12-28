package main.LevelGenerator;

public class vExit {

	int x, y, tileCode;
	
	public vExit(int x, int y, int t){
		this.x = x;
		this.y = y;
		this.tileCode = t;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getTileCode(){
		return tileCode;
	}
	
}
