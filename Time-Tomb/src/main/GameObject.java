package main;

import java.awt.Graphics;

public abstract class GameObject {

	protected double x, y;
	protected int width, height;
	protected ID id;
	protected double velX, velY;
	protected Handler handler;
	protected Game game;
	
	public GameObject(int x, int y, ID id, Handler handler, Game game){
		
		this.x = x;
		this.y = y;
		this.id = id;
		this.handler = handler;
		this.game = game;
		
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);

	public int getX() {
		return (int)x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int)y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
}
