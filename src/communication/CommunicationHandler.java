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
import java.util.Iterator;
import java.util.List;

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
import resources.Reconnect;

public class CommunicationHandler implements Observer {
	/** Record data */
	private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE; // Audio file format
	private AudioInputStream recordAIS;
	private byte[] recordData;
	// private List<Byte> recordData;
	private InputStream recordIS;

	/** Transmitted data */
	private AudioFormat audioFormat;
	private DataLine.Info dataLineInfo;
	private TargetDataLine targetDataLine;
	private AudioInputStream audioInputStream;
	private SourceDataLine sourceDataLine;
	private ByteArrayOutputStream byteArrayOutputStream;
	private byte[] transmitData;

	/** Received data */

	/** Threads */
	private Thread transmitThread;
	private Thread receiveThread;

	/** Local */
	private JFrame window;
	private int triesCont;

	public CommunicationHandler(JFrame window) {
		this.window = window;

		References.COUNTDOWN = new Countdown(References.COUNTDOWN_SEC);
		References.COUNTDOWN.addObserver(this);

		References.RECONNECT = new Reconnect(References.RECONNECT_MILLISEC);
		References.RECONNECT.addObserver(this);
		
		References.SERIAL_MANAGEMENT.addObserver(this);
		

		initializeMixer();
	}

	/** Initialize local mixer: microphone */
	public void initializeMixer() {
		audioFormat = new AudioFormat(References.SAMPLE_RATE, References.SAMPLE_SIZE_IN_BITS, References.CHANNELS,
				References.SIGNED, References.BIG_ENDIAN);
		dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

		// recordData = new ArrayList<>();
	}

	/** Start transmission data */
	public void startTransmission() {
		References.COUNTDOWN.stop();
		References.STATUS_PANEL.setStatus("transmitting");
		References.RECORD_PANEL.setSystemStatus("transmissionON");

		References.TRANSMISSION_ON = true;
		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		transmitThread = new Thread(new TransmitThread());
		transmitThread.start();

		receiveThread = new Thread(new ReceiveThread());
		receiveThread.start();
	}

	/** Send data */
	public void sendData() {
		References.COUNTDOWN.stop();
		References.STATUS_PANEL.setStatus("transmitting");
		References.RECORD_PANEL.setSystemStatus("transmissionON");
		targetDataLine.start();
	}

	/** Receive data */
	public void receiveData() {
		References.COUNTDOWN.start();
		References.STATUS_PANEL.setStatus("waitting");
		if (targetDataLine.isActive()) {
			targetDataLine.stop();
		}
	}

	/**
	 * Observable: 1) COUNTDOWN: time elapsed 2) SERIAL_READER: Data received
	 */
	@Override
	public void update(Observable observable, Object object) {
		if (observable instanceof Countdown) {
			References.COUNTDOWN.stop();
			References.STATUS_PANEL.setStatus("standBy");
			References.RECORD_PANEL.setSystemStatus("transmissionOFF");
			if (References.RECORD_PANEL.getRecordON()) {
				References.RECORD_PANEL.stopRecord();
			}
			References.TRANSMISSION_ON = false;
			targetDataLine.close();
		} else if (observable instanceof SerialManagement) {
			readReceivedFrame();
		} else if (observable instanceof Reconnect) {
			References.SERIAL_MANAGEMENT.sendFrame(References.FRAME_MANAGEMENT.requestCommunicationFrame());
		}
	}

	public void readReceivedFrame() {
		List<Frame> receivedFrame = References.SERIAL_MANAGEMENT.getReceivedBuffer();
		Iterator<Frame> iterator = receivedFrame.iterator();

		while (iterator.hasNext()) {
			Frame frame = iterator.next();
			
			if(References.FRAME_MANAGEMENT.validateFrame(frame)) {
				switch (frame.getType()) {
				case References.REQUEST_COMMUNICATION:
					References.SERIAL_MANAGEMENT.sendFrame(References.FRAME_MANAGEMENT.confirmCommunicationFrame());
					break;

				case References.CONFIRM:
					References.CHANNEL_READY = true;
					break;

				case References.START_FRAME:
					/** 
					 * 1) Meter al buffer del altavoz para reproducir
					 * 2) Meter al buffer de grabación si se está grabando
					 * */ 
					break;

				case References.FRAME_IN_BETWEEN:
					/** 
					 * 1) Meter al buffer del altavoz para reproducir
					 * 2) Meter al buffer de grabación si se está grabando
					 * */
					break;

				case References.FINAL_FRAME:
					/** 
					 * 1) Meter al buffer del altavoz para reproducir
					 * 2) Meter al buffer de grabación si se está grabando
					 * */
					break;

				case References.FINISH_COMMUNICATION:
					References.CHANNEL_READY = false;
					break;
				}

				iterator.remove();
			} 
		}
	}
	
	/** Transmit Thread to send data */
	private class TransmitThread implements Runnable {
		byte tempBuffer[] = new byte[10000];

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
	private class ReceiveThread implements Runnable {

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
		RecordDialog dialog = new RecordDialog(window, 420, 150);

		if (dialog.getAcceptRecord()) {
			Record newRecord = new Record(dialog.getName(), References.CHRONOMETER.getMinute(),
					References.CHRONOMETER.getSecond(), References.CHRONOMETER.getHundredths());
			References.RECORD_PANEL.getRecordModel().addElement(newRecord);
			References.RECORD_PANEL.setSystemStatus("stop");
			References.CHRONOMETER.stop();
			/*
			 * byte data[];
			 * 
			 * data = byteArrayOutputStream.toByteArray();
			 * 
			 * for (int i = 0; i < data.length; i++) { recordData.add(data[i]); }
			 */
			recordData = byteArrayOutputStream.toByteArray();
			recordIS = new ByteArrayInputStream(recordData);
			recordAIS = new AudioInputStream(recordIS, audioFormat, recordData.length / audioFormat.getFrameSize());
			File wavFile = new File(newRecord.getRelativePath());
			try {
				AudioSystem.write(recordAIS, fileType, wavFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (References.TRANSMISSION_ON) {
				References.RECORD_PANEL.setSystemStatus("transmissionON");
			}
		}

		if (References.KEYLISTENER_PANEL.isKeyIsDown()) {
			References.KEYLISTENER_PANEL.setKeyIsDown();
			References.KEYLISTENER_PANEL.getKeyReleasedAction().actionPerformed(null);
		}
	}

	/** Establish communication */
	public boolean stablishCommunication() {
		triesCont = 0;
		
		References.RECONNECT.start();
		
		if (References.RECONNECT.getTries() < References.TRIES && !References.CHANNEL_READY) {
			System.out.println("CommunicationHandler linea 287... esto no funciona");
		}
		
		References.RECONNECT.stop();
		
		return References.CHANNEL_READY;
	}
}