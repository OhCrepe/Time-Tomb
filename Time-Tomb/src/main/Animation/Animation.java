package main.Animation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import main.Game;

public abstract class Animation {

	protected LinkedList<BufferedImage> imgList = new LinkedList<BufferedImage>();
	protected double fps, frameCounter;
	protected int frame, width, height;
	protected Game game;
	
	public Animation(Game game){
		this.game = game;
		this.frame = 0;
		this.frameCounter = 0;
		loadImages();
	}
	
	public abstract void loadImages();
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void update(){
		frameCounter++;
		if(frameCounter >= 60/fps){
			frame++;
			frameCounter -= 60/fps;
		}
		if(frame >= imgList.size())frame = 0;
	}
	
	public Image getFrame(){
		return imgList.get(frame);
	}
	
}
