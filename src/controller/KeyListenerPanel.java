/** @file KeyListenerPanel.java
 *  @brief Class that allows you to hear the keystroke during program execution
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

import main.References;

public class KeyListenerPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JFrame window;

	private Thread keyPressedThread;
	private Thread keyReleasedThread;

	private InputMap inputMap;
	private ActionMap actionMap;

	private boolean keyIsDown = false;
	private boolean clipON = false;

	public KeyListenerPanel(JFrame window) {
		super(new FlowLayout());
		this.window = window;

		keyListener();
	}

	private void keyListener() {
		inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		actionMap = getActionMap();

		keyPressedThread = new Thread(new KeyPressedThread());
		keyPressedThread.start();

		keyReleasedThread = new Thread(new KeyReleasedThread());
		keyReleasedThread.start();
	}

	public class KeyPressedThread implements Runnable {
		@Override
		public void run() {
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, false), "keyPressed");
			actionMap.put("keyPressed", new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					if (!keyIsDown && !clipON) {
						keyIsDown = true;
						startTransmission();
					} else if (clipON) {
						JOptionPane.showConfirmDialog(window, "Stop the clip before starting a communitacion!",
								"Error!", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
	}

	public class KeyReleasedThread implements Runnable {
		@Override
		public void run() {
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, true), "keyReleased");

			actionMap.put("keyReleased", new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					keyIsDown = false;
					stopTransmission();
				}
			});
		}
	}

	public void startTransmission() {
		if (References.COMMUNICATION_HANDLER.stablishCommunication()) {
			References.COMMUNICATION_HANDLER.startTransmission();
		} else {
			JOptionPane.showConfirmDialog(window, "Stablishing communication, channel not ready!", "Warning!",
					JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
		}
	}

	public void stopTransmission() {
		References.COMMUNICATION_HANDLER.stopTransmission();
	}

	public void setClipON(boolean clipON) {
		this.clipON = clipON;
	}

	public boolean isKeyIsDown() {
		return keyIsDown;
	}
}