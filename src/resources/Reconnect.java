/** @file Reconnect.java
 *  @brief Class 
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

public class Reconnect implements ActionListener, Observable {
	private Timer timer;
	private int cont;
	private Observer observer;

	public Reconnect(int durationMillisec) {
		timer = new Timer(durationMillisec, this);
	}

	public void start() {
		timer.setInitialDelay(0);
		timer.start();
	}

	public void stop() {
		if (timer.isRunning()) {
			timer.stop();
			cont = 0;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		cont++;
		this.notifyObservers();
	}
	
	public int getTries() {
		return cont;
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