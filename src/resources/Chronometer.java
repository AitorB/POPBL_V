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

public class Chronometer extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel chronometer;
	private Timer timerSecond, timerHundredth;
	private int minutes, seconds, hundredths;
	private boolean paused = false;
	private boolean newSecond = false;

	public Chronometer(Color fontColor, Font font, Color backgroundColor, boolean opaque) {
		this.setLayout(new BorderLayout());
		this.setBackground(backgroundColor);
		this.setOpaque(opaque);

		chronometer = new JLabel("00 : 00 . 00");
		chronometer.setForeground(fontColor);
		chronometer.setFont(font);
		chronometer.setHorizontalAlignment(JLabel.CENTER);

		timerDeclaration();

		this.add(chronometer, BorderLayout.CENTER);
	}

	public void timerDeclaration() {
		timerSecond = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setText();
				seconds++;
				newSecond = true;
				if (seconds == 60) {
					seconds = 0;
					minutes++;
				}
			}
		});

		timerHundredth = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setText();
				hundredths++;
				if (hundredths == 99 || newSecond) {
					hundredths = 0;
					newSecond = false;
				}
			}
		});
	}

	public void setText() {
		chronometer.setText(String.format("%02d : %02d . %02d", minutes, seconds, hundredths));
	}

	public void start() {
		if (!paused) {
			minutes = 0;
			seconds = 0;
			hundredths = 0;

			timerSecond.setInitialDelay(1000);
			timerSecond.start();

			timerHundredth.setInitialDelay(10);
			timerHundredth.start();
		} else {
			paused = false;

			timerSecond.start();
			timerHundredth.start();
		}
	}

	public void stop() {
		minutes = 0;
		seconds = 0;
		hundredths = 0;
		setText();
		
		paused = false;
		timerSecond.stop();
		timerHundredth.stop();
	}

	public void pause() {
		paused = true;

		timerSecond.stop();
		timerHundredth.stop();
	}

	public int getMinute() {
		return this.minutes;
	}

	public int getSecond() {
		return this.seconds;
	}

	public int getHundredths() {
		return this.hundredths;
	}

	public void setChronometerValue(String newValue) {
		this.chronometer.setText(newValue);
	}
}