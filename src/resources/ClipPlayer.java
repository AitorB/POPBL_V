/** @file ClipPlayer.java
 *  @brief Class that allows you to play audio clips
 *  @authors
 *  Name          | Surname        | Email                                |
 *  ------------- | -------------- | ------------------------------------ |
 *  Aitor         | Barreiro       | aitor.barreirom@alumni.mondragon.edu |
 *  Mikel         | Hernandez      | mikel.hernandez@alumni.mondragon.edu |
 *  Unai          | Iraeta         | unai.iraeta@alumni.mondragon.edu     |
 *  Iker	      | Mendi          | iker.mendi@alumni.mondragon.edu      |
 *  Julen	      | Uribarren	   | julen.uribarren@alumni.mondragon.edu |
 *  @date 20/01/2018
 */

package resources;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.Main;

public class ClipPlayer implements LineListener {
	private static final String START = "START";
    private static final String STOP = "STOP";
    private boolean pause = false;
	
	private JFrame window;
	private Clip clip;
	private int lastFrame;

	public ClipPlayer(JFrame window) {
		this.window = window;
	}

	public void play() {
		if (clip == null) {
			try {
				loadClip(new File(Main.getController().getRecordPanel().getSelectedRecord().getRoute()));
				clip.start();
				clip.addLineListener(this);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showConfirmDialog(window, "Failed to load audio clip!", "Error", JOptionPane.CLOSED_OPTION,
						JOptionPane.ERROR_MESSAGE);
				Main.getController().getRecordPanel().setSystemStatus("stop");
			}
		} else {
			pause();
		}
	}

	public void pause() {
		if (clip.isRunning()) {
			pause = true;
			lastFrame = clip.getFramePosition();
			clip.stop();
		} else {
			pause = false;
			if (lastFrame < clip.getFrameLength()) {
				clip.setFramePosition(lastFrame);
			} else {
				clip.setFramePosition(0);
			}
			clip.start();
		}
	}

	public void stop() {
		if (clip != null) {
			lastFrame = 0;
			clip.stop();
			clip = null;
		}
	}

	private void loadClip(File audioFile) throws Exception {
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		clip = AudioSystem.getClip();
		clip.open(audioStream);
	}

	@Override
	public void update(LineEvent e) {
        String eventType = e.getType().toString();
        if (eventType.equalsIgnoreCase(START)) {
    		Main.getController().getKeyListenerPanel().setClipON(true);
    		Main.getController().getRecordPanel().getChronometer().start();
        } else if (eventType.equalsIgnoreCase(STOP)) {
        	if(!pause) {
        		Main.getController().getRecordPanel().getChronometer().stop();
            	Main.getController().getRecordPanel().setSystemStatus("stop");
            	Main.getController().getKeyListenerPanel().setClipON(false);
            	stop();
        	} else {
        		Main.getController().getRecordPanel().getChronometer().pause();
        	}
        }
	}

}
