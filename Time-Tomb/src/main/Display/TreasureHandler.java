package main.Display;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import main.Game;

public class TreasureHandler implements ImageObserver {

	int offSet;
	Game game;
	Font font = new Font("Verdana", 0, 12);
	String[] treasureTypes = new String[]{"Coin", "Sapphire", "Emerald", "Ruby", "SkeletonHead", "RatCrown", "TrappedGhost"};
	BufferedImage[] treasureSprites;
	int[] treasureAmounts = new int[]{0, 0, 0, 0, 0, 0, 0};
	public static int[] treasureValues = new int[]{10, 250, 500, 1000, 5000, 10000, 20000};
	int skeleKills, ghostKills, ratKills, roomsBeat, potsSmashed;
	
	public TreasureHandler(Game game){
		
		this.game = game;
		offSet = game.WIDTH - game.offSet + 8;
		treasureSprites = new BufferedImage[]{game.img_coin, game.img_sapphire, game.img_emerald, game.img_ruby, game.img_skeleHead, game.img_ratKingCrown};
		
	}
	
	public void render(Graphics g){
		
		g.setFont(font);
		int yPos = 150;
		for(int i = 0; i < treasureAmounts.length; i++){
			if(treasureAmounts[i] > 0){
				g.drawImage(treasureSprites[i], offSet + (16-treasureSprites[i].getWidth())/2, yPos, this);
				g.drawString(" - " + numberFormat(treasureAmounts[i]), offSet + 16, yPos + 8);
				yPos += 16;
				g.drawString("- £" + numberFormat(treasureAmounts[i]*treasureValues[i]), offSet + 32, yPos + 8);
				yPos += 16;
			}
		}
		g.drawString("Total Value:", offSet, yPos+8);
		g.drawString("- £" + numberFormat(game.money), offSet + 16, yPos + 24);
		
	}
	
	public int getTreasureValue(int type){
		return treasureValues[type];
	}
	
	public int getTreasureAmount(int type){
		return treasureAmounts[type];
	}

	public void increaseTreasure(String type){
		
		for(int i = 0; i < treasureTypes.length; i++){
			if(type.equals(treasureTypes[i])){
				treasureAmounts[i]++;
				break;
			}
		}
		
	}
	
	String numberFormat(int i){
		String s = ""+i;
		if(s.length() <= 3)return s;
		else return numberFormat(s.substring(0, s.length()-3))+","+s.substring(s.length()-3, s.length());
	}
	
	String numberFormat(String i){
		String s = ""+i;
		if(s.length() <= 3)return s;
		else return numberFormat(s.substring(0, s.length()-3))+","+s.substring(s.length()-3, s.length());
	}
	
	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int totalValue(){
		int total = 0;
		for(int i = 0; i < 7; i++){
			total += (treasureAmounts[i]*treasureValues[i]);
		}
		return total;
		
	}
	
	public void skeleKill(){
		skeleKills++;
	}
	
	public int skeleKilled(){
		return skeleKills;
	}
	
	public void ghostKill(){
		ghostKills++;
	}
	
	public int ghostKilled(){
		return ghostKills;
	}
	
	public void ratKill(){
		ratKills++;
	}
	
	public int ratKilled(){
		return ratKills;
	}
	
	public void roomBeat(){
		roomsBeat++;
	}
	
	public int roomsBeaten(){
		return roomsBeat;
	}
	
	public void potSmashed(){
		potsSmashed++;
	}
	
	public int potsSmashed(){
		return potsSmashed;
	}
	
}
