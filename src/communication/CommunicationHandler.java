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
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.Record;
import dialog.RecordDialog;
import interfaces.Observable;
import interfaces.Observer;
import main.References;
import resources.Countdown;
import resources.Reconnect;

public class CommunicationHandler implements Observer {
	/** Communication handler */
    boolean transmissionON = false;
	boolean receivingON = false;
	
	
	/** Record data */
	private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	private AudioInputStream recordAIS;
	private byte[] recordData;
	private InputStream recordIS;

	/** Transmitted data */
	private AudioFormat audioFormat;
	private DataLine.Info dataLineInfo;
	private TargetDataLine targetDataLine;
	private AudioInputStream audioInputStream;
	private SourceDataLine sourceDataLine;
	private ByteArrayOutputStream byteArrayOutputStream;

	/** Received data */
	private int dataIndex;
	private int packetLength;
	private byte[] receivedBuffer;
	private byte[] playbuffer;
	private boolean newPacket = false;
	
	/** Threads */
	private Thread transmitThread;
	private Thread playThread;

	/** Local */
	private JFrame window;
	private int tries;

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

		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Start transmission data */
	public void startTransmission() {
		transmissionON = true;
		References.STATUS_PANEL.setStatus(References.TRANSMITTING);
		References.RECORD_PANEL.setUIStatus("transmissionON");

		targetDataLine.start();
		
		transmitThread = new Thread(new TransmitThread());
		transmitThread.start();
	}

	/** Send data */
	public void sendData(byte[] data) {
		byte[] sendData = new byte[References.DATA_LENGTH];
		int numberOfPackets = data.length / References.DATA_LENGTH;
		int lastPacketLength = data.length - (numberOfPackets * References.DATA_LENGTH);
		int index = data.length - lastPacketLength;
		
		if(lastPacketLength == 0) {
			index = index - References.DATA_LENGTH;
		}
		
		for (int i = 0; i < index; i = i + References.DATA_LENGTH) {
			if(i == 0) {
				System.arraycopy(data, i, sendData, 0, References.DATA_LENGTH);
				References.SERIAL_MANAGEMENT.sendFrame(References.FRAME_MANAGEMENT.startFrame(sendData));
			} else {
				System.arraycopy(data, i, sendData, 0, References.DATA_LENGTH);
				References.SERIAL_MANAGEMENT.sendFrame(References.FRAME_MANAGEMENT.betweenFrame(sendData));
			}
		}
		
		if(lastPacketLength != 0) {
			sendData = new byte[lastPacketLength];
			System.arraycopy(data, data.length - lastPacketLength, sendData, 0, lastPacketLength);
			References.SERIAL_MANAGEMENT.sendFrame(References.FRAME_MANAGEMENT.finalFrame(sendData));
		} else {
			sendData = new byte[References.DATA_LENGTH];
			System.arraycopy(data, data.length - References.DATA_LENGTH, sendData, 0, References.DATA_LENGTH);
			References.SERIAL_MANAGEMENT.sendFrame(References.FRAME_MANAGEMENT.finalFrame(sendData));
		}
	}

	public void readReceivedFrame() {
		List<Frame> receivedFrame = References.SERIAL_MANAGEMENT.getReceivedBuffer();
		Iterator<Frame> iterator = receivedFrame.iterator();

		while (iterator.hasNext()) {
			Frame frame = iterator.next();

			if (References.FRAME_MANAGEMENT.validateFrame(frame)) {
				switch (frame.getType()) {
				case References.REQUEST_COMMUNICATION:
					References.SERIAL_MANAGEMENT.sendFrame(References.FRAME_MANAGEMENT.confirmCommunicationFrame());
					receivingON = true;
					References.STATUS_PANEL.setStatus(References.RECEIVING);
					References.COUNTDOWN.stop();
					
					receivedBuffer = new byte[References.RECEIVED_MAX_SIZE];
					
					playThread = new Thread(new PlayThread());
					playThread.start();
					break;

				case References.CONFIRM:
					References.RECONNECT.stop();
					startTransmission();
					break;

				case References.START_FRAME:
					packetLength = 0;
					dataIndex = 0;
					receiveData(frame.getFrame());
					break;

				case References.FRAME_IN_BETWEEN:
					receiveData(frame.getFrame());
					break;

				case References.FINAL_FRAME:
					receiveData(frame.getFrame());
					playbuffer = new byte[packetLength];
					System.arraycopy(receivedBuffer, 0, playbuffer, 0, packetLength);
					newPacket = false;
					break;

				case References.FINISH_COMMUNICATION:
					receivingON = false;
					References.STATUS_PANEL.setStatus(References.WAITING);
					References.COUNTDOWN.start();
					break;
				}

				iterator.remove();
			}
		}
	}
	
	private void receiveData(byte[] data) {
		packetLength = packetLength + data.length;
		System.arraycopy(data, 0, receivedBuffer, dataIndex, data.length);
		dataIndex = dataIndex + References.DATA_LENGTH;
	}
	
	private class PlayThread implements Runnable {
		byte tempBuffer[] = new byte[8];
		
		@Override
		public void run() {
			while (receivingON) {
				if(newPacket == true ) {
					openMixer();
					newPacket = false;
				
					try {
						int cnt;
	
						while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
							if (cnt > 0) {
								sourceDataLine.write(tempBuffer, 0, cnt);
							}
						}
						sourceDataLine.drain();
						sourceDataLine.close();
						targetDataLine.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		public void openMixer() {
			InputStream byteArrayInputStream = new ByteArrayInputStream(playbuffer);		
			audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, playbuffer.length / audioFormat.getFrameSize());
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

			try {
				sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				sourceDataLine.open(audioFormat);
				sourceDataLine.start();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	/** Transmit Thread to send data */
	private class TransmitThread implements Runnable {
		byte tempBuffer[] = new byte[References.PACKET_SIZE];

		@Override
		public void run() {
			byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				while (References.KEYLISTENER_PANEL.isKeyIsDown()) {
					int cnt = targetDataLine.read(tempBuffer, 0, References.PACKET_SIZE);
					if (cnt > 0) {
						byteArrayOutputStream.write(tempBuffer, 0, cnt);
						sendData(byteArrayOutputStream.toByteArray());
					}
				}
				byteArrayOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			References.RECORD_PANEL.setUIStatus("stop");
			References.CHRONOMETER.stop();

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
			if (transmissionON) {
				References.RECORD_PANEL.setUIStatus("transmissionON");
			}
		}

		if (References.KEYLISTENER_PANEL.isKeyIsDown()) {
			References.KEYLISTENER_PANEL.setKeyIsDown();
			References.KEYLISTENER_PANEL.getKeyReleasedAction().actionPerformed(null);
		}
	}

	/** Establish communication */
	public void establishCommunication() {
		tries = 0;
		References.RECONNECT.start();
	}

	/** Stop communication */
	public void stopTransmission() {
		if (transmissionON) {
			References.SERIAL_MANAGEMENT.sendFrame(References.FRAME_MANAGEMENT.finishCommunicationFrame());
			targetDataLine.stop();
			
			References.COUNTDOWN.start();
			References.STATUS_PANEL.setStatus(References.WAITING);
			if (targetDataLine.isActive()) {
				targetDataLine.stop();
			}
		}
	}
	
	/** Observable */
	@Override
	public void update(Observable observable, Object object) {
		if (observable instanceof Countdown) {
			References.COUNTDOWN.stop();
			References.STATUS_PANEL.setStatus(References.STANDBY);
			References.RECORD_PANEL.setUIStatus("transmissionOFF");
			if (References.RECORD_PANEL.getRecordON()) {
				References.RECORD_PANEL.stopRecord();
			}
			transmissionON = false;
		} else if (observable instanceof SerialManagement) {
			readReceivedFrame();
		} else if (observable instanceof Reconnect) {
			if (tries < References.TRIES_MAX) {
				References.SERIAL_MANAGEMENT.sendFrame(References.FRAME_MANAGEMENT.requestCommunicationFrame());
				tries++;
			} else {
				References.RECONNECT.stop();
				JOptionPane.showConfirmDialog(window, "Unable to connect, check communication channel", "Error!",
						JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}