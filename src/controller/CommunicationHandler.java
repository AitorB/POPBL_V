/** @file CommunicationHandler.java
 *  @brief Class that manages the communication between the sender and the receiver
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

import interfaces.Observable;
import interfaces.Observer;
import main.Main;
import resources.Countdown;

public class CommunicationHandler implements Observer {
	private final static int DURATION_SEC = 10;

	private Countdown countdown;
	private boolean channelIsReady = true;

	public CommunicationHandler() {
		countdown = new Countdown(DURATION_SEC);
		countdown.addObserver(this);
	}

	public void start() {
		System.out.println("TRANSMISSION STARTED!");
		countdown.stop();
		Main.getController().getStatusPanel().setStatus("transmitting");
		Main.getController().getRecordPanel().setSystemStatus("transmissionON");
	}

	public void stop() {
		countdown.start();
		Main.getController().getStatusPanel().setStatus("receiving");
	}

	@Override
	public void update(Observable observable, Object object) {
		System.out.println("TRANSMISSION ENDED!");
		countdown.stop();
		Main.getController().getStatusPanel().setStatus("standBy");
		Main.getController().getRecordPanel().setSystemStatus("transmissionOFF");
	}

	public boolean getChannelIsReady() {
		return channelIsReady;
	}

}
