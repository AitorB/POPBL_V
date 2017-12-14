/** @file SerialManagement.java
 *  @brief Class that manages the serial port communication
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import interfaces.Observable;
import interfaces.Observer;
import main.References;

public class SerialManagement {
	/** Threads */
	private Thread writeThread;
	private Thread readThread;

	/** Data management byte arrays */
	private static byte[] sendData;
	private static byte[] received;
	private static byte[] receivedData;
	private static List<Byte> buffer;

	/** Booleans */
	private static boolean receive = false;
	private static boolean connected = false;

	/** Input and Output streams */
	private InputStream inputStream;
	private OutputStream outputStream;

	/** Constructor */
	public SerialManagement() {
		this.openPort();
	}

	/** Method to find available serial ports **/
	private void openPort() {
		for (int i = 1; i < 20 && connected == false; i++) {
			String portName = "COM" + i;
			try {
				connect(portName);
				System.out.println("Connected to port: " + portName);
				connected = true;
			} catch (Exception e) {
				/**
				 * System.out.println("\nError opening the port: " + portName + "'");
				 */
			}
		}
		if (!connected) {
			System.out.println("\nError: Unnable to connect");
		}
	}

	/** Method to connect to an available port */
	private void connect(String portName) throws Exception {
		CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);

		if (commPortIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = commPortIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(References.BAUDRATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				this.inputStream = serialPort.getInputStream();
				this.outputStream = serialPort.getOutputStream();

				References.SERIAL_READER = new SerialReader(inputStream);
				readThread = new Thread(References.SERIAL_READER);
				readThread.start();
			} else {
				System.out.println("Error: Only serial ports allowed");
			}
		}
	}

	/** Thread methods: read thread */
	public static class SerialReader implements Runnable, Observable {
		private Observer observer;

		private InputStream InputStream;

		public SerialReader(InputStream inputStream) {
			this.InputStream = inputStream;
		}

		public void run() {
			buffer = new ArrayList<>();
			received = new byte[References.DATA_LENGTH];
			int length = -1;

			try {
				while (true) {
					while ((length = this.InputStream.read(received)) > 0) {
						for (int i = 0; i < length; i++) {
							buffer.add(received[i]);
						}
						receive = true;
					}
					if (receive == true) {
						receivedData = new byte[buffer.size()];

						for (int i = 0; i < buffer.size(); i++) {
							receivedData[i] = buffer.get(i);
						}
						
						this.notifyObservers();
						buffer.clear();
						receive = false;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	/** Method to send the wanted byte array */
	@SuppressWarnings("static-access")
	public void sendData(byte[] frame) {
		this.sendData = frame;
		writeThread = new Thread(new SerialWriter(outputStream));
		writeThread.start();
	}

	/** Thread methods: write thread */
	private static class SerialWriter implements Runnable {
		private OutputStream outputStream;

		public SerialWriter(OutputStream outputStream) {
			this.outputStream = outputStream;
		}

		public void run() {
			try {
				this.outputStream.write(sendData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Getters and Setters */
	public static boolean isConnected() {
		return connected;
	}

	public static byte[] getReceivedData() {
		return receivedData;
	}
}