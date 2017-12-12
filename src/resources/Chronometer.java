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
	private int minute, second, milisecond;
	private boolean pause = false;

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
		if (!pause) {
			minute = 0;
			second = 0;
			milisecond = 0;

			timer.setInitialDelay(0);
			timer.start();
		} else {
			pause = false;
			timer.start();
		}

	}

	public void stop() {
		chronometer.setText("00 : 00 : 00");
		timer.stop();
	}

	public void pause() {
		pause = true;
		timer.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		chronometer.setText(String.format("%02d : %02d : %02d", minute, second, milisecond));

		milisecond++;
		if (milisecond == 100) {
			milisecond = 0;
			second++;
		}
		if (second == 60) {
			second = 0;
			minute++;
		}
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

	public int getMilisecond() {
		return milisecond;
	}

	public void setChronometer(String newValue) {
		chronometer.setText(newValue);
	}

}