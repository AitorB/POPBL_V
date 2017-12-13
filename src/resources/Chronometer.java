/** @file Chronometer.java
 *  @brief Class to show and calculate the duration of a record
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Chronometer extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel chronometer;
	private Timer timer;
	private int minutes, seconds, milliseconds;
	private boolean paused = false;

	public Chronometer(Color fontColor, Font font, Color backgroundColor, boolean opaque) {
		this.setLayout(new BorderLayout());
		this.setBackground(backgroundColor);
		this.setOpaque(opaque);

		chronometer = new JLabel("00 : 00 : 00");
		chronometer.setForeground(fontColor);
		chronometer.setFont(font);
		chronometer.setHorizontalAlignment(JLabel.CENTER);

		timer = new Timer(10, this);

		this.add(chronometer, BorderLayout.CENTER);
	}

	public void start() {
		if (!paused) {
			minutes = 0;
			seconds = 0;
			milliseconds = 0;

			timer.setInitialDelay(0);
			timer.start();
		} else {
			paused = false;
			timer.start();
		}

	}

	public void stop() {
		chronometer.setText("00 : 00 : 00");
		timer.stop();
	}

	public void pause() {
		paused = true;
		timer.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		chronometer.setText(String.format("%02d : %02d : %02d", minutes, seconds, milliseconds));

		milliseconds++;
		if (milliseconds == 100) {
			milliseconds = 0;
			seconds++;
		}
		else if (seconds == 60) {
			seconds = 0;
			minutes++;
		}
	}

	public int getMinute() {
		return this.minutes;
	}

	public int getSecond() {
		return this.seconds;
	}

	public int getMilisecond() {
		return this.milliseconds;
	}

	public void setChronometerValue (String newValue) {
		this.chronometer.setText(newValue);
	}
}