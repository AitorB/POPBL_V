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

public class SerialManagement implements Observable {
	/** Observable class */
	private Observer observer;

	/** Threads */
	private Thread writeThread;
	private Thread readThread;

	/** Send & receive parameters */
	private List<Frame> receiveBuffer;
	private static int sendId = 0;

	/** Input and Output streams */
	private InputStream inputStream;
	private OutputStream outputStream;

	/** Booleans */
	private boolean connected = false;

	/** Constructor */
	public SerialManagement() {
		this.receiveBuffer = new ArrayList<>();
		this.openPort();
	}

	/** Method to find available serial ports **/
	private void openPort() {
		for (int i = 1; i < 20 && connected == false; i++) {
			String portName = "COM" + i;
			try {
				connect(portName);
				System.out.println("Connected to port: " + portName);
				this.connected = true;
			} catch (Exception e) {
				// System.out.println("Error opening the port: " + portName);
			}
		}
		if (!connected) {
			System.out.println("Error: Unable to connect through serial port!");
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

				References.SERIAL_READER = new SerialReader(this.inputStream);
				readThread = new Thread(References.SERIAL_READER);
				readThread.start();
			} else {
				System.out.println("Error: Only serial ports allowed");
			}
		}
	}

	/** Thread methods: read thread */
	public static class SerialReader implements Runnable {
		/** To receive data in serial port */
		private InputStream InputStream;

		/** Data received management */
		private static List<Byte> dataBuffer;
		private static byte[] dataReceived;
		private static int arrivedBytes = 0;
		private int packetsArrived = 0;

		/** Constructor */
		public SerialReader(InputStream inputStream) {
			this.InputStream = inputStream;
		}

		public void run() {
			dataBuffer = new ArrayList<>();
			dataReceived = new byte[References.RECEIVED_MAX_SIZE];
			int length = -1;

			try {
				while (true) {
					while ((length = this.InputStream.read(dataReceived)) > 0) {
						for (int i = 0; i < length; i++) {
							dataBuffer.add(dataReceived[i]);
						}
						arrivedBytes = arrivedBytes + length;
						packetsArrived = arrivedBytes / References.FRAME_LENGTH;

						if (packetsArrived > 0) {
							takePacketsFromBuffer(packetsArrived);
						}
						packetsArrived = 0;
					}
					arrivedBytes = 0;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void takePacketsFromBuffer(int packetsArrived2) {
			byte[] dataReceived = new byte[References.FRAME_LENGTH];
			int capturedPackets = 0;
			int packetIndex = 0;
			int sizeDividedPacket = dataBuffer.size() - packetsArrived * References.FRAME_LENGTH;

			for (int i = 0; i < dataBuffer.size(); i++) {
				dataReceived[packetIndex] = dataBuffer.get(i);
				packetIndex++;

				if (i == ((capturedPackets + 1) * (References.FRAME_LENGTH - 1)) + capturedPackets) {
					capturedPackets++;
					packetIndex = 0;
					Frame receivedFrame = new Frame(dataReceived);
					References.SERIAL_MANAGEMENT.addReceivedBuffer(receivedFrame);
				}
			}
			dataBuffer.clear();

			for (int i = 0; i < sizeDividedPacket; i++) {
				dataBuffer.add(dataReceived[i]);
			}

			arrivedBytes = sizeDividedPacket;
		}
	}

	/** Thread methods: write thread */
	private static class SerialWriter implements Runnable {
		/** To send data in serial port */
		private OutputStream outputStream;

		/** Data received management */
		private Frame sendFrame;
		private int currentPacketID = 0;

		/** Constructor */
		public SerialWriter(OutputStream outputStream, Frame frame) {
			this.outputStream = outputStream;
			this.sendFrame = frame;
			this.currentPacketID = frame.getId();
		}

		public void run() {
			try {
				while (currentPacketID != sendId) {
				}
				this.outputStream.write(sendFrame.getFrame());
				this.outputStream.flush();

				if (sendId == 15) {
					sendId = 0;
				} else {
					sendId++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Method to send a Frame */
	public void sendFrame(Frame frame) {
		writeThread = new Thread(new SerialWriter(outputStream, frame));
		writeThread.start();
	}

	private void addReceivedBuffer(Frame frame) {
		receiveBuffer.add(frame);
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

	/** Getters and Setters */
	public boolean isConnected() {
		return connected;
	}

	public List<Frame> getReceivedBuffer() {
		return receiveBuffer;
	}
}

/**
 * Proceso de lectura de frame: 
 * 1) comprobar que el checkSum está bien 
 * 2) Si está bien,coger contador y validar 
 * 3) Leer tipo de frame 
 * 3.1) value == 0 : enviar frame con value = 1. (Confirmación) 
 * 3.2) value == 1 : enviar frames con datos y value = 2 (startFrame = true) || Luego con value = 3 
 * 3.3) value == 2 : Leer primera trama del paquete 
 * 3.4) value == 3 : Leer trama intermedia
 * 3.5) value == 4 : Leer trama final del paquete 
 * 3.6) value == 5 : Leer final de la comunicación
 */