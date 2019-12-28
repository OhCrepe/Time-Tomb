package main.Audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class SoundPlayer{

	static AudioInputStream stream;
	static AudioFormat format;
	static DataLine.Info info;
	static Clip clip;
	static FloatControl gainControl;
	
	public static synchronized void playSound(String fileName){

		try {
			stream = AudioSystem.getAudioInputStream(new File(fileName));
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
			
	}
	
	public static synchronized void playSound(String fileName, float volumeControl){
		
		if(fileName == null)return;
		try {
			stream = AudioSystem.getAudioInputStream(new File(fileName));
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volumeControl);
			clip.start();
			gainControl.setValue(volumeControl);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}		
		
	}

	public static synchronized void playMusic(String fileName, float volumeControl) {

        if(fileName == null)return;
        try {
			stream = AudioSystem.getAudioInputStream(new File(fileName));
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volumeControl);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			gainControl.setValue(volumeControl);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}		
        
    }
	
}
