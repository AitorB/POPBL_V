/** @file Countdown.java
 *  @brief Class to do a countdown of a given duration in miliseconds
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Countdown implements ActionListener {
	private Timer timer;
	private boolean timeOver;

	public Countdown(int durationMS) {
		timer = new Timer(durationMS, this);
	}

	public void start() {
		timeOver = false;
		timer.setInitialDelay(0);
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timeOver = true;
	}

	public boolean getTimeOver() {
		return timeOver;
	}

}