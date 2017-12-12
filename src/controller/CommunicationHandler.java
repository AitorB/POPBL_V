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

import main.Main;
import resources.Countdown;

public class CommunicationHandler {
	private Countdown countdown;
	private boolean channelIsReady = true;
	
	public CommunicationHandler() {
		countdown = new Countdown(10000);
	}
	
	public void start() {
		countdown.start();
		System.out.println("TRANSMISSION STARTED!");
		Main.getController().getStatusPanel().setStatus("transmitting");
		Main.getController().getRecordPanel().setSystemStatus("transmissionON");
	}
	
	public void stop() {
		if(countdown.getTimeOver()) {
			System.out.println("TRANSMISSION ENDED!");
			Main.getController().getStatusPanel().setStatus("standBy");
			Main.getController().getRecordPanel().setSystemStatus("transmissionOFF");
		} else {
			Main.getController().getStatusPanel().setStatus("receiving");
		}
		
	}
	public boolean getChannelIsReady() {
		return channelIsReady;
	}

}
