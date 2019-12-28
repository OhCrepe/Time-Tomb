package main.Entities;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import main.Animation.Animation;
import main.Animation.AnimationState;
import main.Direction;
import main.Game;
import main.GameObject;
import main.Handler;
import main.ID;
import main.Objects.Pot;
import main.Objects.Sarcophagus;
import main.Tiles.Tile;

public abstract class Entity extends GameObject implements ImageObserver{
	
	protected Rectangle collisionRect;
	protected Animation animation;
	protected float transparency;
	protected boolean transparent = false;
	protected EntityState state;
	protected AnimationState animState;
	double kbSpeed, kbX = 0, kbY = 0, xVel, yVel, speed;;
	Player player;
	
	public Entity(int x, int y, ID id, Handler handler, Game game) {
		super(x, y, id, handler, game);
		state = EntityState.Normal;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick(){
		animation.update();
	}

	@Override
	public void render(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		if(transparent)g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		g2.drawImage(animation.getFrame(), (int)(this.x + (this.width - animation.getWidth())/2), 
				(int)(this.y + (this.height - animation.getHeight())/2), animation.getWidth(), animation.getHeight(), this);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	
	public void checkTileCollisions(){
		
		for(int x = 0; x < 12; x++){
			for(int y = 0; y < 12; y++){
				if(game.tiles[x][y] == null)return;
				Tile tempTile = (Tile)game.tiles[x][y];
				if(tempTile.isSolid()){
					if(tempTile.collisionRect().intersects(collisionRect)){
						collideWith(tempTile);
					}
				}
			}
		}
		
	}
	
	public void checkSarcophagusCollisions(){
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Sarcophagus){
				Sarcophagus sarc = (Sarcophagus)handler.object.get(i);
				if(sarc.collisionRect().intersects(collisionRect)){
					collideWith(sarc);
				}
			}
		}
	}
	
	abstract void collideWith(Tile tempTile);
	
	abstract void collideWith(Sarcophagus sarc);
	
	public Direction calculateDirection(Tile tile) {		
		
		int tileX = tile.getX() + (tile.getWidth()/2), tileY = tile.getY() + (tile.getHeight()/2);
		
		if(Math.abs(tileX - (this.x + this.width/2)) >= Math.abs(tileY - (this.y + this.height/2))){
			if(tileX - (this.x + this.width/2) > 0){
				return Direction.Right;
			}else{
				return Direction.Left;
			}
		}else{
			if(tileY - (this.y + this.height/2) > 0){
				return Direction.Down;
			}else{
				return Direction.Up;
			}
		}
		
	}
	
	public Direction calculateDirection(Sarcophagus sarc) {		
		
		int sarcX = sarc.getX() + (sarc.getWidth()/2), sarcY = sarc.getY() + (sarc.getHeight()/2);
		
		if(Math.abs(sarcX - (this.x + this.width/2)) >= Math.abs(sarcY - (this.y + this.height/2))){
			if(sarcX - (this.x + this.width/2) > 0){
				return Direction.Right;
			}else{
				return Direction.Left;
			}
		}else{
			if(sarcY - (this.y + this.height/2) > 0){
				return Direction.Down;
			}else{
				return Direction.Up;
			}
		}
		
	}
	
	public Direction calculateDirection(Pot pot) {		
		
		int potX = pot.collisionRect().x + (pot.collisionRect().width/2), potY = pot.collisionRect().y + (pot.collisionRect().height/2);
		int thisX = this.collisionRect().x + (this.collisionRect().width/2), thisY = this.collisionRect().y + (this.collisionRect().height/2);
		
		if(Math.abs(potX - thisX) >= Math.abs(potY - thisY)){
			if(potX > thisX){
				return Direction.Right;
			}else{
				return Direction.Left;
			}
		}else{
			if(potY > thisY){
				return Direction.Down;
			}else{
				return Direction.Up;
			}
		}
		
	}
	
	public void knockbackTick() {
		
		this.x += kbX;
		this.y += kbY;
		
		if(kbX > 0)kbX -= 0.5;
		if(kbX < 0)kbX += 0.5;
		if(kbY < 0)kbY += 0.5;
		if(kbY > 0)kbY -= 0.5;
		
		if(Math.abs(kbX) <= 0.5)kbX = 0;
		if(Math.abs(kbY) <= 0.5)kbY = 0;
		
		if(kbX == 0 && kbY == 0)state = EntityState.Normal;
		
	}
	
	public void checkPotCollisions(){
		
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Pot){
				Pot p = (Pot)handler.object.get(i);
				if(p.collisionRect().intersects(collisionRect()) && !p.isHeld())collideWith(p);
			}
		}
		
	}
	
	abstract void collideWith(Pot pot);
	
	public void checkPlayerCollision(){
		
		if(player.collisionRect().intersects(collisionRect()))collideWith(player);
		
	}
	
	public EntityState getState(){
		return state;
	}
	
	abstract void collideWith(Player p);
	
	public void setKnockback(Entity e){
		
		int dX = (int)(e.getX() - this.x), dY = (int)(e.getY() - this.y);
		
		double playerDist = Math.sqrt(dX*dX + dY*dY);
		double a = Math.asin(dY/playerDist);
		
		this.kbY = -kbSpeed*Math.sin(a);
		
		if(e.getX() < this.x){
			this.kbX = kbSpeed*Math.cos(a);
		}else{
			this.kbX = -kbSpeed*Math.cos(a);
		}
		
	}
	
	void setKnockback(Pot pot){
		
		int dX = (int)(pot.getX() - this.x), dY = (int)(pot.getY() - this.y);
		
		double potDist = Math.sqrt(dX*dX + dY*dY);
		double a = Math.asin(dY/potDist);
		
		this.kbY = -kbSpeed*Math.sin(a);
		
		if(pot.getX() < this.x){
			this.kbX = kbSpeed*Math.cos(a);
		}else{
			this.kbX = -kbSpeed*Math.cos(a);
		}
		
	}
	
	public void fixCollisionRect(){
		this.collisionRect.x = (int)x;
		this.collisionRect.y = (int)y;
	}
	
	public void setAnim(Animation anim, AnimationState aState){
		this.animation = anim;
		this.animState = aState;
	}
	
	abstract Rectangle collisionRect();

}
