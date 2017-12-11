package controller;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import main.Main;

public class Transmission extends JPanel {
	private static final long serialVersionUID = 1L;

	private JFrame window;
	
	private boolean keyIsDown = false;
	private boolean available = true;

	public Transmission(JFrame window) {
		super(new FlowLayout());
		this.window = window;
		
		keyListener();
	}

	private void keyListener() {
		InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, false), "keyPressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, true), "keyReleased");

		am.put("keyPressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!keyIsDown && available) {
					keyIsDown = true;
					Main.controller.getRecordPanel().setButtonStatus("recording");
					startTransmission();
				} else if (!available){
					JOptionPane.showConfirmDialog(window, "Stop the record before starting a communitacion!", "Error!", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		am.put("keyReleased", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				keyIsDown = false;
				Main.controller.getRecordPanel().setButtonStatus("stopped");
				stopTransmission();
			}
		});
	}

	public void startTransmission() {
		System.out.println("TRANSMISSION STARTED!");
		// START listening to microphone
	}
	
	public void stopTransmission() {
		System.out.println("TRANSMISSION ENDED!");
		// STOP listening to microphone
	}
	
	public void communicationAvailable(boolean available) {
		this.available = available;
	}
}
