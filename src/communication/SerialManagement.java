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

	/**  */
	private static Frame sendFrame;
	private static Frame receivedFrame;
	private static List<Frame> receiveBuffer;

	/** Input and Output streams */
	private InputStream inputStream;
	private OutputStream outputStream;
	
	/** Booleans */
	private static boolean connected = false;
	
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
		private InputStream InputStream;

		public SerialReader(InputStream inputStream) {
			this.InputStream = inputStream;
		}

		public void run() {
			List<Byte> dataBuffer = new ArrayList<>();
			byte[] dataReceived = new byte[References.FRAME_LENGTH];
			int length;
			boolean dataReaded = false;

			try {
				while (true) {
					while ((length = this.InputStream.read(dataReceived)) > 0) {
						for (int i = 0; i < length; i++) {
							dataBuffer.add(dataReceived[i]);
						}
						dataReaded = true;
					}
					if (dataReaded == true) {
						receivedFrame = new Frame(arrayListToArray(dataBuffer));
						References.SERIAL_MANAGEMENT.addReceivedBuffer(receivedFrame);
						
						dataReaded = false;
						dataBuffer.clear();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private byte[] arrayListToArray(List<Byte> buffer) {
			byte[] receivedFrame = new byte[References.FRAME_LENGTH];
			
			for (int i = 0; i < buffer.size(); i++) {
				receivedFrame[i] = buffer.get(i);
			}
			
			return receivedFrame;
		}
	}

	/** Thread methods: write thread */
	private static class SerialWriter implements Runnable {
		private OutputStream outputStream;

		public SerialWriter(OutputStream outputStream) {
			this.outputStream = outputStream;
		}

		public void run() {
			try {
				this.outputStream.write(sendFrame.getFrame());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Method to send a Frame */
	public void sendFrame(Frame frame) {
		sendFrame = frame;
		writeThread = new Thread(new SerialWriter(outputStream));
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
 * 
 */