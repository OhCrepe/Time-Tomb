package main.Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import main.Direction;
import main.Game;
import main.Handler;
import main.ID;
import main.Animation.AnimationState;
import main.Animation.Skeleton.SkeletonAttackAnim;
import main.Animation.Skeleton.SkeletonDamageAnim;
import main.Animation.Skeleton.SkeletonDeathAnim;
import main.Animation.Skeleton.SkeletonStandardAnim;
import main.Audio.SoundPlayer;
import main.Objects.Pot;
import main.Objects.Sarcophagus;
import main.Objects.SkeletonHead;
import main.Pathfinder.Pathfinder;
import main.Tiles.Tile;

public class Skeleton extends Entity{

	int health, slotTimer, slotTimeLimit = 18, deathTimer = 17;
	Pathfinder pathFinder;
	
	public Skeleton(int x, int y, ID id, Handler handler, Game game) {
		
		super(x, y, ID.Skeleton, handler, game);
		this.width = 32;
		this.height = 32;
		this.animation = new SkeletonStandardAnim(game);
		this.collisionRect = new Rectangle(x, y, 32, 32);
		this.transparency = 0f;
		this.transparent = false;
		this.health = 3;
		this.speed = 2;
		this.slotTimer = slotTimeLimit;
		this.kbSpeed = 10; //knockback speed
		this.pathFinder = new Pathfinder(game);
		
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Player){
				player = (Player)handler.object.get(i);
			}
		}
		
		this.handler.addObject(new Indicator(x, y, ID.Indicator, handler, game, this, Color.RED));
		//SoundPlayer.playSound("Resources/Sounds/Entities/SkeletonSpawn.wav", 5f);
		
	}

	@Override
	public void tick(){
		super.tick();
		
		if(state == EntityState.Normal)normalTick();
		else if(state == EntityState.Knockback)knockbackTick();
		else if(state == EntityState.Recovering)recoverTick();
		else if(state == EntityState.Death)deathTick();
		
		collisionRect.x = (int)x;
		collisionRect.y = (int)y;

		checkTileCollisions();
		checkPotCollisions();
		
	}

	private void normalTick(){
		
		if(this.animState != AnimationState.Normal)setAnim(new SkeletonStandardAnim(game), AnimationState.Normal);
		
		if(slotted()){
			slotTimer = slotTimeLimit;
			System.out.println("Slotted");
			this.xVel = 0;
			this.yVel = 0;
			Direction direction = pathFinder.findNextStep(this, player);
			if(direction == Direction.Up){
				System.out.println("MOVE UP");
				this.yVel = -speed;
			}
			if(direction == Direction.Left){
				System.out.println("MOVE LEFT");
				this.xVel = -speed;
			}
			if(direction == Direction.Right){
				System.out.println("MOVE RIGHT");
				this.xVel = speed;
			}
			if(direction == Direction.Down){
				System.out.println("MOVE DOWN");
				this.yVel = speed;
			}
		}else if(slotTimer == 0){
			this.state = EntityState.Recovering;
			this.xVel = 0;
			this.yVel = 0;
		}
		
		slotTimer--;
		
		this.x += xVel;
		this.y += yVel;
		
		checkPlayerCollision();
		checkSarcophagusCollisions();
		
	}
	
	//Checks to see if locked onto tile grid
	private boolean slotted() {
		if((this.x - game.offSet)%32 == 0 && this.y%32 == 0)return true;
		return false;
	}
	
	private void deathTick(){
		deathTimer--;
		if(deathTimer <= 0)handler.removeObject(this);
	}
	

	@Override
	void collideWith(Sarcophagus sarc) {
		
		Direction direction = calculateDirection(sarc);
		if(direction == Direction.Down){
			if(sarc.isUpSolid())this.y = sarc.getY() - this.height;
		}else if(direction == Direction.Right){
			if(sarc.isLeftSolid())this.x = sarc.getX() - this.width;
		}else if(direction == Direction.Left){
			if(sarc.isRightSolid())this.x = sarc.getX() + sarc.getWidth();
		}else if(direction == Direction.Up){
			if(sarc.isDownSolid())this.y = sarc.getY() + sarc.getHeight();
		}
		fixCollisionRect();
		
	}
	
	private void recoverTick() {
		
		//Select point to align with
		int targetX, targetY;
		targetX = ((int)((this.x-game.offSet)/32))*32 + game.offSet;
		targetY = ((int)(this.y/32))*32;
		
		if((targetX-game.offSet-this.x)%32 > 16)targetX += 32;
		if((targetY-this.y)%32 > 16)targetY += 32;
		
		//Begin moving towards that point
		int dX = (int)(targetX - this.x), dY = (int)(targetY - this.y);
		
		double dist = Math.sqrt(dX*dX + dY*dY);
		
		//If close enough snap onto the point
		if(dist <= speed){
			this.x = targetX;
			this.y = targetY;
			state = EntityState.Normal;
			return;
		}
		
		double a = Math.asin(dY/dist);
		
		this.yVel = speed*Math.sin(a);
		
		if(targetX < this.x){
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

	/*
	private void tempNormalTick() {
		
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
	*/

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void collideWith(Tile tile) {

		Direction direction = calculateDirection(tile);	
		if(direction == Direction.Down){
			if(tile.isUpSolid())this.y = tile.getY() - this.height;
		}else if(direction == Direction.Right){
			if(tile.isLeftSolid())this.x = tile.getX() - this.width;
		}else if(direction == Direction.Left){
			if(tile.isRightSolid())this.x = tile.getX() + tile.getWidth();
		}else if(direction == Direction.Up){
			if(tile.isDownSolid())this.y = tile.getY() + tile.getHeight();
		}
		fixCollisionRect();
		
	}

	@Override
	void collideWith(Pot pot) {
		
		Direction direction = calculateDirection(pot);
		if(direction == Direction.Down){
			if(pot.isUpSolid())this.y = pot.getY() - this.height;
		}else if(direction == Direction.Right){
			if(pot.isLeftSolid())this.x = pot.getX() - this.width;
		}else if(direction == Direction.Left){
			if(pot.isRightSolid())this.x = pot.getX() + pot.getWidth();
		}else if(direction == Direction.Up){
			if(pot.isDownSolid())this.y = pot.getY() + pot.getHeight();
		}
		fixCollisionRect();
		
	}
	
	public void getHit(Pot pot){
		health--;
		if(health <= 0){
			die(pot);
			return;
		}
		setKnockback(pot);
		setAnim(new SkeletonDamageAnim(game), AnimationState.Damaged);
		this.state = EntityState.Knockback;
	}
	
	@Override
	void collideWith(Player p) {
		setKnockback(p);
		game.timer.penalty(1);
		this.state = EntityState.Knockback;
		setAnim(new SkeletonAttackAnim(game), AnimationState.Attack);
		p.setKnockback(this);
		p.state = EntityState.Knockback;
	}
	
	void die(Pot pot){		
		
		/*
		double headXVel, headYVel;
		int dX = (int)(pot.getX() - this.x), dY = (int)(pot.getY() - this.y);
		
		double potDist = Math.sqrt(dX*dX + dY*dY);
		double a = Math.asin(dY/potDist);	
		headYVel = speed*Math.sin(a);
		
		if(pot.getX() < this.x){
			headXVel = -speed*Math.cos(a);
		}else{
			headXVel = speed*Math.cos(a);
		}
		
		if(Math.abs(headXVel) > speed)headXVel = 0;
		if(Math.abs(headYVel) > speed)headYVel = 0;
		
		*/
		
		handler.addObject(new SkeletonHead((int)this.x + 9, (int)this.y, handler, game, pot.getxVel(), pot.getyVel()));
		game.treasureHandler.skeleKill();
		this.state = EntityState.Death;
		setAnim(new SkeletonDeathAnim(game), AnimationState.Death);
		
	}

	@Override
	public Rectangle collisionRect() {
		return collisionRect;
	}
	
	public void render(Graphics g){
		super.render(g);
	}

}
