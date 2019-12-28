package main.Menu.Statistics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import main.Game;
import main.Menu.BackButton;
import main.Menu.Menu;

public class StatisticsMenu extends Menu{

	Font font = new Font("Verdana", 0, 22);
	FontMetrics metrics;
	
	public StatisticsMenu(Game game) {
		super(game);
		buttons.clear();
		buttons.add(new BackButton(game, 16, game.HEIGHT-80, game.img_backButton));
	}
	
	@Override
	public void render(Graphics g){
		super.render(g);
		g.drawImage(game.img_statisticsButton, 152, 64, this);
		g.setFont(font);
		g.setColor(Color.WHITE);
		metrics = g.getFontMetrics();
		g.drawString("Treasure Collected: £" + Statistics.treasureCollected, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Treasure Collected: "), 210);
		g.drawString("Coins Collected: " + Statistics.coinsCollected, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Coins Collected: "), 240);
		g.drawString("Sapphires Collected: " + Statistics.sapphiresCollected, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Sapphires Collected: "), 270);
		g.drawString("Emeralds Collected: " + Statistics.emeraldsCollected, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Emeralds Collected: "), 300);
		g.drawString("Rubies Collected: " + Statistics.rubiesCollected, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Rubies Collected: "), 330);
		g.drawString("Skeleton Heads Collected: " + Statistics.skeleHeadsCollected, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Skeleton Heads Collected: "), 360);
		g.drawString("Trapped Ghosts Collected: " + Statistics.trappedGhostsCollected, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Trapped Ghosts Collected: "), 390);
		g.drawString("Rat Kings Crowns Collected: " + Statistics.ratCrownsCollected, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Rat Kings Crowns Collected: "), 420);
		g.drawString("Skeletons Defeated: " + Statistics.skeletonsDefeated, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Skeletons Defeated: "), 450);
		g.drawString("Ghosts Trapped: " + Statistics.ghostsTrapped, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Ghosts Trapped: "), 480);
		g.drawString("Rat Kings Defeated: " + Statistics.ratKingsDefeated, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Rat Kings Defeated: "), 510);
		g.drawString("Pots Smashed: " + Statistics.potsSmashed, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Pots Smashed: "), 540);
		g.drawString("Rooms Completed: " + Statistics.roomsCompleted, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Rooms Completed: "), 570);
		g.drawString("Games played: " + Statistics.gamesPlayed, 
				game.WIDTH/2 + 50 - metrics.stringWidth("Games Played: "), 600);
	}

}
