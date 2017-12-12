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

import interfaces.Observable;
import interfaces.Observer;

public class Countdown implements ActionListener, Observable {
	private Timer timer;
	private int durationSec;
	private Observer observer;

	public Countdown(int durationSec) {
		timer = new Timer(1000, this);
		this.durationSec = durationSec;
	}

	public void start() {
		timer.setInitialDelay(durationSec * 1000);
		timer.start();
	}

	public void stop() {
		if (timer.isRunning()) {
			timer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.notifyObservers();
	}

	@Override
	public void addObserver(Observer observer) {
		this.observer = observer;
	}

	@Override
	public void removeObserver(Observer observer) {
		this.observer = null;
	}

	@Override
	public void notifyObservers() {
		this.observer.update(this, this);
	}

}