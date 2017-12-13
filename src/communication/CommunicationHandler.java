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

package communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;

import controller.Record;
import dialog.RecordDialog;
import interfaces.Observable;
import interfaces.Observer;
import main.References;
import resources.Countdown;

public class CommunicationHandler implements Observer {

	/** Record data */
	private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE; // Audio file format
	private AudioInputStream recordAIS;
	private byte recordData[];
	private InputStream recordIS;

	/** Transmit data */
	private AudioFormat audioFormat;
	private DataLine.Info dataLineInfo;
	private TargetDataLine targetDataLine;
	private AudioInputStream audioInputStream;
	private SourceDataLine sourceDataLine;
	private ByteArrayOutputStream byteArrayOutputStream;
	private boolean transmissionON = false;

	/** Threads */
	private Thread transmitThread;
	private Thread receiveThread;

	/** Local */
	private JFrame window;
	private Countdown countdown;
	private boolean IsChannelReady = true;

	public CommunicationHandler(JFrame window) {
		this.window = window;
		countdown = new Countdown(References.DURATION_SEC);
		countdown.addObserver(this);
		initializeMixer();
	}

	/** Initialize local mixer: microphone */
	public void initializeMixer() {
		audioFormat = getAudioFormat();
		dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

	}

	/** Sending data */
	public void startTransmission() {
		countdown.stop();
		References.STATUS_PANEL.setStatus("transmitting");
		References.RECORD_PANEL.setSystemStatus("transmissionON");

		if (!transmissionON) {
			transmissionON = true;
			try {
				targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
				targetDataLine.open(audioFormat);
				targetDataLine.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			transmitThread = new Thread(new TransmitThread());
			transmitThread.start();
		} else {
			targetDataLine.start();
		}
	}

	/** Receiving data */
	public void stopTransmission() {
		countdown.start();
		References.STATUS_PANEL.setStatus("receiving");
		targetDataLine.stop();
	}

	/** End off transmission */
	@Override
	public void update(Observable observable, Object object) {
		countdown.stop();
		References.STATUS_PANEL.setStatus("standBy");
		References.RECORD_PANEL.setSystemStatus("transmissionOFF");
		if (References.RECORD_PANEL.getRecordON()) {
			References.RECORD_PANEL.stopRecord();
		}
		transmissionON = false;
		targetDataLine.close();
	}

	/** Transmit Thread to send data */
	public class TransmitThread implements Runnable {
		byte tempBuffer[] = new byte[8];

		@Override
		public void run() {
			byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				/**
				 * Es posible que necesite otro while mirando si se está transmitiendo o no, por
				 * el targetDataLine.stop()
				 */
				while (References.KEYLISTENER_PANEL.isKeyIsDown()) {
					int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
					if (cnt > 0) {
						byteArrayOutputStream.write(tempBuffer, 0, cnt);
					}
				}
				byteArrayOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** Receive Thread to send data */
	public class ReceiveThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
		}
	}

	/** Start recoding */
	public void startRecord() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Stop recoding and save if requested */
	public void stopRecord() {
		Record newRecord;
		RecordDialog dialog = new RecordDialog(window, 420, 150);
		if (dialog.getAcceptRecord()) {
			newRecord = new Record(dialog.getTitle(), References.CHRONOMETER.getMinute(),
					References.CHRONOMETER.getSecond(), References.CHRONOMETER.getHundredths());
			References.RECORD_PANEL.getRecordModel().addElement(newRecord);
			References.RECORD_PANEL.setSystemStatus("stop");
			References.CHRONOMETER.stop();
			
			recordData = byteArrayOutputStream.toByteArray();
			recordIS = new ByteArrayInputStream(recordData);
			recordAIS = new AudioInputStream(recordIS, audioFormat, recordData.length / audioFormat.getFrameSize());

			File wavFile = new File(newRecord.getRelativePath()); // Audio file FULLNAME
			try {
				AudioSystem.write(recordAIS, fileType, wavFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if(transmissionON) {
				References.RECORD_PANEL.setSystemStatus("transmissionON");
			}
		}
	}

	/** Define how to sample Audio */
	private AudioFormat getAudioFormat() {
		return new AudioFormat(References.SAMPLE_RATE, References.SAMPLE_SIZE_IN_BITS, References.CHANNELS,
				References.SIGNED, References.BIG_ENDIAN);
	}

	/** Control purposes */
	public boolean getIsChannelReady() {
		return this.IsChannelReady;
	}
}