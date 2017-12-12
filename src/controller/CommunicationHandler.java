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

import javax.sound.sampled.AudioFormat;

import interfaces.Observable;
import interfaces.Observer;
import main.References;
import resources.Countdown;

public class CommunicationHandler implements Observer {

	private Countdown countdown;
	private boolean IsChannelReady = true;

	public CommunicationHandler() {
		countdown = new Countdown(References.DURATION_SEC);
		countdown.addObserver(this);
	}

	public void start() {
		System.out.println("TRANSMISSION STARTED!");
		countdown.stop();
		References.STATUS_PANEL.setStatus("transmitting");
		References.RECORD_PANEL.setSystemStatus("transmissionON");
	}

	public void stop() {
		countdown.start();
		References.STATUS_PANEL.setStatus("receiving");
	}

	@Override
	public void update(Observable observable, Object object) {
		System.out.println("TRANSMISSION ENDED!");
		countdown.stop();
		References.STATUS_PANEL.setStatus("standBy");
		References.RECORD_PANEL.setSystemStatus("transmissionOFF");
		if (References.RECORD_PANEL.getRecordON()) {
			References.RECORD_PANEL.stopRecord();
		}
	}

	public void transmit() {

	}

	public void receive() {

	}

	public void startRecord() {
		// start recoring
	}

	public void stopRecord() {
		// save record to disk
	}

	private AudioFormat getAudioFormat() {
		return new AudioFormat(References.SAMPLE_RATE, References.SAMPLE_SIZE_IN_BITS, References.CHANNELS,
				References.SIGNED, References.BIG_ENDIAN);
	}

	public boolean getIsChannelReady() {
		return this.IsChannelReady;
	}
}