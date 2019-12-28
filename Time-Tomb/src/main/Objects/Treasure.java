package main.Objects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import main.Direction;
import main.Game;
import main.GameObject;
import main.Handler;
import main.ID;
import main.Entities.EntityState;
import main.Entities.Player;
import main.Entities.RatKing;
import main.Tiles.Tile;

public abstract class Treasure extends GameObject implements ImageObserver{
	
	int value;
	double xVel, yVel;
	Image sprite;
	Player player;
	Rectangle collisionRect;
	double magSpeed = 1.1, magRange = 64;
	
	public Treasure(int x, int y, Handler handler, Game game, double xVel, double yVel){
		super(x, y, ID.Treasure, handler, game);
		this.xVel = xVel;
		this.yVel = yVel;
		
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Player){
				player = (Player)handler.object.get(i);
				break;
			}
		}
		
	}
	
	public void tick(){
		
		this.x += xVel;
		this.y += yVel;
		this.collisionRect.x = (int)x;
		this.collisionRect.y = (int)y;
		
		if(Math.abs(this.xVel) < 1){
			this.xVel = 0;
		}
		if(Math.abs(this.yVel) < 1){
			this.yVel = 0;
		}
		
		if(xVel > 0)xVel -= 0.5;
		if(xVel < 0)xVel += 0.5;
		if(yVel > 0)yVel -= 0.5;
		if(yVel < 0)yVel += 0.5;
		
		tileCollisions();
		potCollisions();
		playerCollision();
		checkRatCollisions();
		checkSarcophagusCollisions();
		
	}
	
	public void tileCollisions(){
		
		for(int x = 0; x < 12; x++){
			for(int y = 0; y < 12; y++){
				Tile tempTile = game.tiles[x][y];
				if(tempTile.isSolid()){
					if(collisionRect.intersects(tempTile.collisionRect())){
						collideWith(tempTile);
					}
				}
			}
		}
		
	}
	
	private void checkRatCollisions(){
		
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.RatKing){
				RatKing tempRat = (RatKing)handler.object.get(i);
				if(tempRat.getState() != EntityState.Death)ratCollision(tempRat);
			}
		}
	}
		
	public void potCollisions(){
		
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Pot){
				Pot tempPot = (Pot)handler.object.get(i);
				if(tempPot.collisionRect().intersects(collisionRect))collideWith(tempPot);
			}
		}
		
	}
	
	public void checkSarcophagusCollisions(){
		
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Sarcophagus){
				Sarcophagus tempSarc = (Sarcophagus)handler.object.get(i);
				if(tempSarc.collisionRect().intersects(collisionRect))collideWith(tempSarc);
			}
		}
		
	}
	
	public void collideWith(Sarcophagus s){
		
		Direction direction = calculateDirection(s);
		if(direction == Direction.Up && s.isDownSolid() && yVel < 0){
			if(yVel == 0)this.y = s.getY() + s.getHeight();
			else yVel = -yVel;
		}
		if(direction == Direction.Down && s.isUpSolid() && yVel > 0){
			if(yVel == 0)this.y = s.getY() - this.height;
			else yVel = -yVel;
		}
		if(direction == Direction.Left && s.isRightSolid() && xVel < 0){
			if(xVel == 0)this.x = s.getX() + s.getWidth();
			else xVel = -xVel;
		}
		if(direction == Direction.Right && s.isLeftSolid() && xVel > 0){
			if(xVel == 0)this.x = s.getX() - this.width;
			else xVel = -xVel;
		}
		
	}

	public Rectangle collisionRect() {
		return collisionRect;
	}

	public Direction calculateDirection(Tile t){
		
		int trX = (int)this.x + this.width/2;
		int trY = (int)this.y + this.height/2;
		int tiX = t.getX() + t.getWidth()/2;
		int tiY = t.getY() + t.getHeight()/2;
		
		if(!(t.isLeftSolid() || t.isUpSolid() || t.isRightSolid() || t.isDownSolid())){
			if(trX > tiX){
				x = t.getX() + t.getWidth();
				if(xVel < 0) xVel = -xVel;
			}else{
				x = t.getX() - this.width;
				if(xVel > 0) xVel = -xVel;
			}
			if(trY > tiY){
				y = t.getY() + t.getHeight();
				if(yVel < 0) yVel = -yVel;
			}else{
				y = t.getY() - this.height;
				if(yVel > 0) yVel = -yVel;
			}
			return Direction.None;
		}
		
		if(Math.abs(tiX - trX) >= Math.abs(tiY - trY)){
			if(tiX > trX){
				if(!t.isLeftSolid()){
					if(tiY > trY)return Direction.Down;
					else return Direction.Up;
				}
				return Direction.Right;
			}else{
				if(!t.isRightSolid()){
					if(tiY > trY)return Direction.Down;
					else return Direction.Up;
				}
				return Direction.Left;
			}
		}else{
			if(tiY > trY){
				if(!t.isUpSolid()){
					if(tiX > trX)return Direction.Right;
					else return Direction.Left;
				}
				return Direction.Down;
			}else{
				if(!t.isDownSolid()){
					if(tiX > trX)return Direction.Right;
					else return Direction.Left;
				}
				return Direction.Up;
			}
		}
		
	}
	
public Direction calculateDirection(Sarcophagus s){
		
		int srX = (int)this.x + this.width/2;
		int srY = (int)this.y + this.height/2;
		int siX = s.getX() + s.getWidth()/2;
		int siY = s.getY() + s.getHeight()/2;
		
		if(!(s.isLeftSolid() || s.isUpSolid() || s.isRightSolid() || s.isDownSolid())){
			if(srX > siX){
				x = s.getX() + s.getWidth();
				if(xVel < 0) xVel = -xVel;
			}else{
				x = s.getX() - this.width;
				if(xVel > 0) xVel = -xVel;
			}
			if(srY > siY){
				y = s.getY() + s.getHeight();
				if(yVel < 0) yVel = -yVel;
			}else{
				y = s.getY() - this.height;
				if(yVel > 0) yVel = -yVel;
			}
			return Direction.None;
		}
		
		if(Math.abs(siX - srX) >= Math.abs(siY - srY)){
			if(siX > srX){
				if(!s.isLeftSolid()){
					if(siY > srY)return Direction.Down;
					else return Direction.Up;
				}
				return Direction.Right;
			}else{
				if(!s.isRightSolid()){
					if(siY > srY)return Direction.Down;
					else return Direction.Up;
				}
				return Direction.Left;
			}
		}else{
			if(siY > srY){
				if(!s.isUpSolid()){
					if(siX > srX)return Direction.Right;
					else return Direction.Left;
				}
				return Direction.Down;
			}else{
				if(!s.isDownSolid()){
					if(siX > srX)return Direction.Right;
					else return Direction.Left;
				}
				return Direction.Up;
			}
		}
		
	}
	
	public Direction calculateDirection(Pot p){
		
		int trX = (int)this.x + this.width/2;
		int trY = (int)this.y + this.height/2;
		int poX = p.getX() + p.getWidth()/2;
		int poY = p.getY() + p.getHeight()/2;
		
		if(!(p.isLeftSolid() || p.isUpSolid() || p.isRightSolid() || p.isDownSolid())){
			if(trX > poX){
				x = p.getX() + p.getWidth();
				if(xVel < 0) xVel = -xVel;
			}else{
				x = p.getX() - this.width;
				if(xVel > 0) xVel = -xVel;
			}
			if(trY > poY){
				y = p.getY() + p.getHeight();
				if(yVel < 0) yVel = -yVel;
			}else{
				y = p.getY() - this.height;
				if(yVel > 0) yVel = -yVel;
			}
			return Direction.None;
		}
		
		if(Math.abs(poX - trX) >= Math.abs(poY - trY)){
			if(poX > trX){
				if(!p.isLeftSolid()){
					if(poY > trY)return Direction.Down;
					else return Direction.Up;
				}
				return Direction.Right;
			}else{
				if(!p.isRightSolid()){
					if(poY > trY)return Direction.Down;
					else return Direction.Up;
				}
				return Direction.Left;
			}
		}else{
			if(poY > trY){
				if(!p.isUpSolid()){
					if(poX > trX)return Direction.Right;
					else return Direction.Left;
				}
				return Direction.Down;
			}else{
				if(!p.isDownSolid()){
					if(poX > trX)return Direction.Right;
					else return Direction.Left;
				}
				return Direction.Up;
			}
		}
		
	}
	
	public void collideWith(Tile t){
		
		Direction direction = calculateDirection(t);
		if(direction == Direction.Up && t.isDownSolid() && yVel < 0){
			if(yVel == 0)this.y = t.getY() + t.getHeight();
			else yVel = -yVel;
		}
		if(direction == Direction.Down && t.isUpSolid() && yVel > 0){
			if(yVel == 0)this.y = t.getY() - this.height;
			else yVel = -yVel;
		}
		if(direction == Direction.Left && t.isRightSolid() && xVel < 0){
			if(xVel == 0)this.x = t.getX() + t.getWidth();
			else xVel = -xVel;
		}
		if(direction == Direction.Right && t.isLeftSolid() && xVel > 0){
			if(xVel == 0)this.x = t.getX() - this.width;
			else xVel = -xVel;
		}
		
	}
	
	public void collideWith(Pot p){
		
		if(p.getxVel() == 0 && p.getyVel() == 0 && !p.isHeld()){
			Direction direction = calculateDirection(p);
			if(direction == Direction.Up && p.isDownSolid() && yVel < 0){
				if(yVel == 0)this.y = p.getY() + p.getHeight();
				else yVel = -yVel;
			}
			if(direction == Direction.Down && p.isUpSolid() && yVel > 0){
				if(yVel == 0)this.y = p.getY() - this.height;
				else yVel = -yVel;
			}
			if(direction == Direction.Left && p.isRightSolid() && xVel < 0){
				if(xVel == 0)this.x = p.getX() + p.getWidth();
				else xVel = -xVel;
			}
			if(direction == Direction.Right && p.isLeftSolid() && xVel > 0){
				if(xVel == 0)this.x = p.getX() - this.width;
				else xVel = -xVel;
			}
		}
		
	}
	
	public void playerCollision(){
		if(player.collisionRect().intersects(collisionRect)){
			handler.removeObject(this);
			game.money += this.value;
			game.treasureHandler.increaseTreasure(toString());
			playCollectNoise();
		}else{
			int pX = player.getX() + player.getWidth()/2;
			int pY = player.getY() + player.getHeight()/2;
			int tX = (int)this.x + this.width/2;
			int tY = (int)this.y + this.height/2;
			int dX = pX - tX;
			int dY = pY - tY;
			double dist = Math.sqrt(dX*dX +dY*dY);
			if(dist <= magRange){
				double a = Math.asin(dY/dist);
				double dXv = magSpeed*Math.cos(a);
				double dXy = magSpeed*Math.sin(a);
				if(dX > 0)xVel += dXv;
				else xVel -= dXv;
				yVel += dXy;
			}
		}
	}
	
	public void ratCollision(RatKing rat){
		if(rat.collisionRect().intersects(collisionRect)){
			handler.removeObject(this);
			rat.collect(this);
			//playRatCollectNoise();
		}else{
			int pX = player.getX() + player.getWidth()/2;
			int pY = player.getY() + player.getHeight()/2;
			int tX = (int)this.x + this.width/2;
			int tY = (int)this.y + this.height/2;
			int dX = pX - tX;
			int dY = pY - tY;
			double dist = Math.sqrt(dX*dX +dY*dY);
			if(dist <= magRange*2/3){
				double a = Math.asin(dY/dist);
				double dXv = magSpeed*Math.cos(a);
				double dXy = magSpeed*Math.sin(a);
				if(dX > 0)xVel += dXv;
				else xVel -= dXv;
				yVel += dXy;
			}
		}
	}
	
	public int treasureValue(){
		return value;
	}
	
	public void render(Graphics g){
		g.drawImage(sprite, (int)x, (int)y, width, height, this);
	}
	
	public void setVelX(double vel){
		this.xVel = vel;
	}
	
	public void setVelY(double vel){
		this.yVel = vel;
	}
	
	public abstract void playCollectNoise();
	public abstract String toString();
	
}
