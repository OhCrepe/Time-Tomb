package main.Objects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import main.Game;
import main.GameObject;
import main.Handler;
import main.ID;
import main.Entities.Player;
import main.Entities.RatKing;
import main.Entities.Skeleton;
import main.Tiles.Tile;

public class Pot extends GameObject implements ImageObserver{

	Image sprite;
	private Rectangle collisionRect;
	boolean upSolid, leftSolid, rightSolid, downSolid;
	boolean pickedUp = false;
	Player player;
	int maxThrow = 25, throwTime = 0;
	int minVal = 100, maxVal = 1500;
	double speed = 10;
	private double xVel;
	private double yVel;
	double sizeMod = 0;
	
	public Pot(int x, int y, Handler handler, Game game, Image sprite){
		super(x, y, ID.Pot, handler, game);
		this.sprite = sprite;
		this.width = 32;
		this.height = 32;
		this.collisionRect = new Rectangle(x, y, width, height);
		this.upSolid = false;
		this.rightSolid = false;
		this.leftSolid = false;
		this.downSolid = false;
	}

	public boolean isUpSolid() {
		return upSolid;
	}
	
	public boolean isHeld(){
		return pickedUp;
	}

	public void setUpSolid(boolean upSolid) {
		this.upSolid = upSolid;
	}

	public boolean isLeftSolid() {
		return leftSolid;
	}

	public void setLeftSolid(boolean leftSolid) {
		this.leftSolid = leftSolid;
	}

	public boolean isRightSolid() {
		return rightSolid;
	}

	public void setRightSolid(boolean rightSolid) {
		this.rightSolid = rightSolid;
	}

	public boolean isDownSolid() {
		return downSolid;
	}

	public void setDownSolid(boolean downSolid) {
		this.downSolid = downSolid;
	}

	@Override
	public void tick() {

		if(pickedUp){
			this.x = player.getX();
			this.y = player.getY() - this.height + 24;
		}
		this.x += getxVel();
		this.y += getyVel();
		
		
		if(getxVel() != 0 || getyVel() != 0){
			flightTick();
			this.collisionRect.x = (int)x + 1;
			this.collisionRect.y = (int)y + 1;
		}else{
			this.collisionRect.x = (int)x;
			this.collisionRect.y = (int)y;
		}
		
	}
	
	public void flightTick(){
		
		//Sorts out size change during flight
		throwTime += 1;
		int a = Math.abs(throwTime - maxThrow/2);
		sizeMod = (-((double)4/(maxThrow*maxThrow))*(a*a)+1)/4;
		if(throwTime >= maxThrow)smash();
		
		if(throwTime >= 1){
			//Collisions with other objects
			for(int i = 0; i < handler.object.size(); i++){
				
				//Hitting pots
				if(handler.object.get(i).getId() == ID.Pot){
					Pot tempPot = (Pot)handler.object.get(i);
					if(tempPot == this)continue;
					if(collisionRect.intersects(tempPot.collisionRect())){
						tempPot.smash();
						this.smash();
					}
				}
				
				//Hitting skeletons
				if(handler.object.get(i).getId() == ID.Skeleton){
					Skeleton tempSkeleton = (Skeleton)handler.object.get(i);
					if(collisionRect.intersects(tempSkeleton.collisionRect())){
						tempSkeleton.getHit(this);
						this.smash();
					}
				}
				
				//Hitting rats
				if(handler.object.get(i).getId() == ID.RatKing){
					RatKing tempRat = (RatKing)handler.object.get(i);
					if(collisionRect.intersects(tempRat.collisionRect())){
						tempRat.getHit(this);
						this.smash();
					}
				}
				
				//Hitting Sarcophagus
				if(handler.object.get(i).getId() == ID.Sarcophagus){
					Sarcophagus tempSarc = (Sarcophagus)handler.object.get(i);
					if(collisionRect.intersects(tempSarc.collisionRect())){
						tempSarc.open();
						this.smash();
					}
				}
				
			}
			
			//Collisions with tiles
			for(int tX = 0; tX < 12; tX++){
				for(int tY = 0; tY < 12; tY++){
					Tile t = game.tiles[tX][tY];
					if(t.isSolid()){
						if(t.collisionRect().intersects(collisionRect)){
							smash();
						}
					}
				}
			}
		}
		
	}
	
	public void setPickedUp(boolean p){
		pickedUp = p;
	}
	
	public void setPlayer(Player p){
		player = p;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(sprite, (int)(x - width*sizeMod/2), (int)(y - width*sizeMod/2), (int)(width*(1+sizeMod)), (int)(height*(1+sizeMod)), this);
		
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Rectangle collisionRect(){
		return collisionRect;
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void bePicked(Player p){
		
		//Fixes pot collisions
		
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Pot){
				Pot pot = (Pot)handler.object.get(i);

				if(pot.getX() == x && y + 32 == pot.getY())pot.setUpSolid(true);
				if(pot.getX() == x && y - 32 == pot.getY())pot.setDownSolid(true);
				if(x + 32 == pot.getX() && pot.getY() == y)pot.setLeftSolid(true);
				if(x - 32 == pot.getX() && pot.getY() == y)pot.setRightSolid(true);
					
			}
		}

		game.solids[(int)(this.x-game.offSet)/32][(int)this.y/32] = false;
		pickedUp = true;
		player = p;
		game.pickUp = true;
		player.setPot(true);
		this.sprite = game.img_potHeld;
		this.upSolid = false;
		this.rightSolid = false;
		this.leftSolid = false;
		this.downSolid = false;
		game.heldPot = this;
		
	}
	
	public void beThrown(boolean left, double angle){
		
		this.x = player.getX();
		this.y = player.getY();
		this.setyVel(speed*Math.sin(angle));
		this.setxVel(speed*Math.cos(angle));
		
		this.collisionRect.width = 30;
		this.collisionRect.height = 30;
		this.collisionRect.x = (int)x+1;
		this.collisionRect.y = (int)y+1;
		if(left){
			this.setxVel(-this.getxVel());
		}
		this.pickedUp = false;
		player.setPot(false);
		this.sprite = game.img_potRegular;
		game.pickUp = false;
		
	}
	
	public void smash(){
		
		//Fixes pot collisions
		game.solids[(int)(this.x-game.offSet)/32][(int)this.y/32] = false;
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Pot){
				Pot pot = (Pot)handler.object.get(i);

				if(pot.getX() == x && y + 32 == pot.getY())pot.setUpSolid(true);
				if(pot.getX() == x && y - 32 == pot.getY())pot.setDownSolid(true);
				if(x + 32 == pot.getX() && pot.getY() == y)pot.setLeftSolid(true);
				if(x - 32 == pot.getX() && pot.getY() == y)pot.setRightSolid(true);
							
			}
		}
		
		int value = ((int)(Math.random()*(maxVal/10-minVal/10))*10)+minVal;
		while(value > 0){
			
			if(value > 1250){
				
				int oX = (int)(this.x + (Math.random()*(width-12))),
						oY =  (int)(this.y + (Math.random()*(height - 12)));
				handler.addObject(new Ruby(oX, oY, handler, game, this.getxVel(), this.getyVel()));
				value -= 1000;
			}
			
			if(value >= 750){

				int oX = (int)(this.x + (Math.random()*(width-8))),
						oY =  (int)(this.y + (Math.random()*(height - 8)));
				handler.addObject(new Emerald(oX, oY, handler, game, this.getxVel(), this.getyVel()));
				value -= 500;
				
			}
			
			while(value >= 250){

				int oX = (int)(this.x + (Math.random()*(width-8))),
						oY =  (int)(this.y + (Math.random()*(height - 8)));
				handler.addObject(new Sapphire(oX, oY, handler, game, this.getxVel(), this.getyVel()));
				value -= 250;
				
			}
			
			while(value >= 10){
				
				int oX = (int)(this.x + (Math.random()*(width-8))),
						oY =  (int)(this.y + (Math.random()*(height - 8)));
				handler.addObject(new Coin(oX, oY, handler, game, 
						this.getxVel() + ((int)(Math.random()*5)-2), this.getyVel() + ((int)(Math.random()*5)-2)));
				value -= 10;
			}
			
		}
		
		game.treasureHandler.potSmashed();
		handler.removeObject(this);
	}

	public double getxVel() {
		return xVel;
	}

	public void setxVel(double xVel) {
		this.xVel = xVel;
	}

	public double getyVel() {
		return yVel;
	}

	public void setyVel(double yVel) {
		this.yVel = yVel;
	}
	
	public String toString(){
		return "Pot";
	}
	
}
