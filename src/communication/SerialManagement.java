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

public class SerialManagement {

	/** Threads */
	private Thread writeThread;
	private Thread readThread;

	/** Data management byte arrays */
	private static byte[] data;
	private static byte[] received;
	public static byte[] receivedData;
	private static List<Byte> buffer;

	/** Booleans */
	private static boolean receive = false;
	private static boolean connected = false;
	public static boolean processed = false;

	/** Baudrate */
	private final int BAUDRATE = 9600;

	/** Input and Output streams */
	private InputStream in;
	private OutputStream out;

	/** Constructor */
	public SerialManagement() {
		super();
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
				System.out.println("\nError opening the port '" + portName + "'");
			}

		}

	}

	/** Method to connect to an available port */
	private void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				this.in = serialPort.getInputStream();
				this.out = serialPort.getOutputStream();

				readThread = new Thread(new SerialReader(in));
				// writeThread = new Thread(new SerialWriter(out));
				// writeThread.start();
				readThread.start();

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	/** Thread methods: read thread */
	private static class SerialReader implements Runnable {
		InputStream in;

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void run() {
			buffer = new ArrayList<Byte>();
			received = new byte[1024];
			int len = -1;

			try {
				while (true) {/**
								 * When something arrives, returns the number of elements arrived (len) and
								 * fills "received" with the elements that have arrived. Returns -1 when there
								 * is nothing to read
								 */

					while ((len = this.in.read(received)) > 0) {

						for (int i = 0; i < len; i++) {
							buffer.add(received[i]);
						}
						receive = true;
					}

					if (receive == true) {
						receivedData = new byte[buffer.size()];

						for (int i = 0; i < buffer.size(); i++) {
							receivedData[i] = buffer.get(i);
						}
						processed = true;
						buffer.clear();
						receive = false;
						processed = false;
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/** Thread methods: write thread */
	private static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		public void run() {
			System.out.println("trying to send");
			try {
				this.out.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Method to send the wanted byte array */
	@SuppressWarnings("static-access")
	public void sendData(byte[] frame) {

		this.data = frame;
		writeThread = new Thread(new SerialWriter(out));
		writeThread.start();
	}

	/** Getters and Setters */
	public static boolean isConnected() {
		return connected;
	}

	public static byte[] getReceivedData() {
		return receivedData;
	}

	public int getBAUDRATE() {
		return BAUDRATE;
	}

	public static boolean isProcessed() {
		return processed;
	}

}