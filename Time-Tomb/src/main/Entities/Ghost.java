package main.Entities;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;

import main.Game;
import main.Handler;
import main.ID;
import main.Animation.AnimationState;
import main.Animation.Ghost.GhostAttackAnim;
import main.Animation.Ghost.GhostStandardAnim;
import main.Audio.SoundPlayer;
import main.Objects.Pot;
import main.Objects.Sarcophagus;
import main.Tiles.Tile;

public class Ghost extends Entity{
	
	public Ghost(int x, int y, ID id, Handler handler, Game game) {
		super(x, y, ID.Ghost, handler, game);
		this.width = 32;
		this.height = 32;
		setAnim(new GhostStandardAnim(game), AnimationState.Normal);
		this.collisionRect = new Rectangle(x, y, 32, 32);
		this.transparency = 0.15f;
		this.transparent = false;
		this.speed = 1.2;
		this.kbSpeed = 10; //knockback speed
		
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Player){
				player = (Player)handler.object.get(i);
			}
		}
		
		this.handler.addObject(new Indicator(x, y, ID.Indicator, handler, game, this, Color.RED));
		SoundPlayer.playSound("Resources/Sounds/Entities/GhostSpawn.wav", 5f);
		
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		return false;
	}

	@Override
	public void tick(){
		super.tick();
		
		if(state == EntityState.Normal)normalTick();
		else if(state == EntityState.Knockback)knockbackTick();
		
		collisionRect.x = (int)x;
		collisionRect.y = (int)y;
		
		this.transparent = false;
		checkTileCollisions();
		checkPotCollisions();
		checkSarcophagusCollisions();
		
	}
	
	private void normalTick(){
		
		//Sets animation to standard if it's not
		if(this.animState != AnimationState.Normal)setAnim(new GhostStandardAnim(game), AnimationState.Normal);
		
		int dX = (int)(player.getX() - this.x), dY = (int)(player.getY() - this.y);
		
		double playerDist = Math.sqrt(dX*dX + dY*dY);
		double a = Math.asin(dY/playerDist);
		
		this.yVel = speed*Math.sin(a);
		
		if(player.getX() < this.x){
			this.xVel = -speed*Math.cos(a);
		}else{
			this.xVel = speed*Math.cos(a);
		}
		
		if(Math.abs(xVel) > speed)xVel = 0;
		if(Math.abs(yVel) > speed)yVel = 0;
		
		this.x += xVel;
		this.y += yVel;
		
		checkPlayerCollision();
		
	}
	
	@Override
	public void collideWith(Tile tempTile) {
		transparent = true;
	}

	@Override
	public void collideWith(Pot pot) {
		transparent = true;
	}

	@Override
	Rectangle collisionRect() {
		return collisionRect;
	}

	@Override
	void collideWith(Player p) {
		setKnockback(p);
		game.timer.penalty(1);
		this.state = EntityState.Knockback;
		setAnim(new GhostAttackAnim(game), AnimationState.Attack);
		p.setKnockback(this);
		p.state = EntityState.Knockback;
	}

	@Override
	void collideWith(Sarcophagus sarc) {
		transparent = true;		
	}

}
