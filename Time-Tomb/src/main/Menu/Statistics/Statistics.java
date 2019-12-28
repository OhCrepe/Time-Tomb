package main.Menu.Statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Statistics {

	public static long treasureCollected;
	public static int coinsCollected, sapphiresCollected, emeraldsCollected, rubiesCollected, skeleHeadsCollected, trappedGhostsCollected,
				ratCrownsCollected, skeletonsDefeated, ghostsTrapped, ratKingsDefeated, potsSmashed, roomsCompleted, gamesPlayed;
	
	static String filePath = "Resources/SaveData/stats.txt";
	
	//The file should store the statistics in the same order their respective variables are declared above
	public static void loadStats(){
		try {
			
			Scanner in = new Scanner(new File(filePath));
			treasureCollected = in.nextLong();
			coinsCollected = in.nextInt();
			sapphiresCollected = in.nextInt();
			emeraldsCollected = in.nextInt();
			rubiesCollected = in.nextInt();
			skeleHeadsCollected = in.nextInt();
			trappedGhostsCollected = in.nextInt();
			ratCrownsCollected = in.nextInt();
			skeletonsDefeated = in.nextInt();
			ghostsTrapped = in.nextInt();
			ratKingsDefeated = in.nextInt();
			potsSmashed = in.nextInt();
			roomsCompleted = in.nextInt();
			gamesPlayed = in.nextInt();
			in.close();
			
		} catch (FileNotFoundException | NoSuchElementException e) {
			createStats();
		}
	}
	
	public static void saveStats(){
		
		try {
			PrintWriter writer = new PrintWriter(filePath, "UTF-8");
			writer.println(treasureCollected);
			writer.println(coinsCollected);
			writer.println(sapphiresCollected);
			writer.println(emeraldsCollected);
			writer.println(rubiesCollected);
			writer.println(skeleHeadsCollected);
			writer.println(trappedGhostsCollected);
			writer.println(ratCrownsCollected);
			writer.println(skeletonsDefeated);
			writer.println(ghostsTrapped);
			writer.println(ratKingsDefeated);
			writer.println(potsSmashed);
			writer.println(roomsCompleted);
			writer.println(gamesPlayed);
			writer.close();
		} catch (FileNotFoundException e) {
			createStats();
			saveStats();
		} catch (UnsupportedEncodingException e) {
			//Eh
		}
		
	}
	
	public static void createStats(){
		
		try{
			
			//Creates a new stats file and loads it into the game
			PrintWriter writer = new PrintWriter(filePath, "UTF-8");
			for(int i = 0; i < 14; i++)writer.println(0);
			writer.close();
			loadStats();
			
		}catch(IOException e){
			System.out.println("Something went wrong here that I can't quite explain");
		}
		
	}
	
}
