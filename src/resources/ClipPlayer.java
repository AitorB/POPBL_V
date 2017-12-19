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

import controller.Record;
import main.References;

public class ClipPlayer implements LineListener {
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
				loadClip(new File(References.RECORD_PANEL.getSelectedRecord().getRelativePath()));
				clip.start();
				clip.addLineListener(this);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showConfirmDialog(window, "Failed to load audio clip!", "Error", JOptionPane.CLOSED_OPTION,
						JOptionPane.ERROR_MESSAGE);
				References.RECORD_PANEL.setUIStatus("stop");
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
			References.RECORD_PANEL.setUIStatus("stop");
			References.KEYLISTENER_PANEL.setClipON(false);
			References.CHRONOMETER.stop();
			if (!References.RECORD_PANEL.getRecordModel().isEmpty()) {
				Record record = References.RECORD_PANEL.getSelectedRecord();
				References.CHRONOMETER.setChronometerValue(String.format("%02d : %02d . %02d", record.getMinutes(),
						record.getSeconds(), record.getHundreths()));
			}
		}
	}

	private void loadClip(File audioFile) {
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(LineEvent e) {
		String eventType = e.getType().toString();
		if (eventType.equalsIgnoreCase(References.START)) {
			References.KEYLISTENER_PANEL.setClipON(true);
			References.CHRONOMETER.start();
		} else if (eventType.equalsIgnoreCase(References.STOP)) {
			if (!pause) {
				stop();
			} else {
				References.CHRONOMETER.pause();
			}
		}
	}
}