package main.Display;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import main.Game;

public class Window extends Canvas{
	private static final long serialVersionUID = -4810618286807932601L;	
	
	static JFrame frame;
	
	public Window(int width, int height, String title, Game game){
		
		//The frame of the window
		frame = new JFrame(title);
		
		//Defines the dimensions of the frame
		frame.getContentPane().setPreferredSize(new Dimension(width, height));
		frame.getContentPane().setMaximumSize(new Dimension(width, height));
		frame.getContentPane().setMinimumSize(new Dimension(width, height));
		frame.setFocusable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);	//Means the frame starts in the middle
		frame.add(game);
		frame.setVisible(true);
		game.start();
		
	}
	
}
