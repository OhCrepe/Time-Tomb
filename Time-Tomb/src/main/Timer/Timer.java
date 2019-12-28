package main.Timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import main.Game;
import main.Animation.Animation;
import main.Animation.HUD.TimerAnimation;
import main.Animation.HUD.TimerLowAnimation;

public class Timer implements ImageObserver {

	int initialTime, maxTime, timeLeft, timePenalty = 60, x, displayY;
	Animation anim;
	Game game;
	Font timeFont;
	
	public Timer(double maxTime, Game game){
		this.maxTime = (int)(maxTime * 60);
		this.initialTime = this.maxTime;
		this.timeLeft = this.maxTime;
		this.game = game;
		this.displayY = game.HEIGHT - 50;
		this.x = game.WIDTH/2 - 405;
		anim = new TimerAnimation(game);
		timeFont = importFont();
	}
	
	public void penalty(double penalty){
		this.maxTime -= (int)(penalty*timePenalty);
	}
	
	public void tick(){
		if(this.timeLeft <= 0)game.endGame();
		anim.update();
		timeLeft--;
		if(timeLeft == 59){
			this.anim = new TimerLowAnimation(game);
		}
	}
	
	public void render(Graphics g){
		g.drawImage(anim.getFrame(), 8, 32, 112, 112, this);
		if(timeFont != null){
			g.setFont(timeFont);
			g.setColor(new Color(158, 106, 54));
			g.drawString((int)(Math.ceil(timeLeft/60)) + "/", 120 - g.getFontMetrics().stringWidth(timeLeft/60 + "/"), 208);
			g.drawString("" + maxTime/60, 112 - g.getFontMetrics().stringWidth("" + maxTime/60), 272);
		}
	}
	
	public void resetTime(){
		this.timeLeft = maxTime;
		this.anim = new TimerAnimation(game);
	}
	
	public Font importFont(){
		
		File fontFile = new File("Resources/Fonts/VINERITC.TTF");
		Font font = null;
	    try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return font.deriveFont(Font.PLAIN, 64);
		
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
