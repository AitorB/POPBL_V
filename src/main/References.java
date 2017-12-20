/** @file References.java
 *  @brief Class with all the references of the program
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

package main;

import communication.CommunicationHandler;
import communication.FrameManagement;
import communication.SerialManagement;
import communication.SerialManagement.SerialReader;
import controller.KeyListenerPanel;
import controller.RecordPanel;
import controller.StatusPanel;
import resources.Chronometer;
import resources.ClipPlayer;
import resources.Countdown;
import resources.Reconnect;

public final class References {
	/** Main classes references */
	public static FrameManagement FRAME_MANAGEMENT;
	public static SerialManagement SERIAL_MANAGEMENT;
	public static SerialReader SERIAL_READER;
	public static CommunicationHandler COMMUNICATION_HANDLER;
	public static KeyListenerPanel KEYLISTENER_PANEL;
	public static RecordPanel RECORD_PANEL;
	public static StatusPanel STATUS_PANEL;
	public static Chronometer CHRONOMETER;
	public static ClipPlayer CLIP_PLAYER;
	public static Countdown COUNTDOWN;
	public static Reconnect RECONNECT;

	/** Window dimensions */
	public static final int WIDTH_WINDOW = 1280;
	public static final int HEIGHT_WINDOW = 720;

	/** Images */
	public static final String BACKGROUND_IMAGE = "image\\background.jpg";
	public static final String LOGO_IMAGE = "image\\mu.png";
	public static final String PLAY_IMAGE = "image\\play.png";
	public static final String PAUSE_IMAGE = "image\\pause.png";
	public static final String STOP_IMAGE = "image\\stop.png";
	public static final String DELETE_IMAGE = "image\\delete.png";
	public static final String STARTREC_IMAGE = "image\\startRec.png";
	public static final String STOPREC_IMAGE = "image\\stopRec.png";
	public static final String STANDBY_IMAGE = "image\\yellow.png";
	public static final String TRANSMITTING_IMAGE = "image\\green.png";
	public static final String WAITING_IMAGE = "image\\blue.png";
	public static final String RECEIVING_IMAGE = "image\\red.png";

	/** Text */
	public static final String STANDBY_TEXT = "STANDBY";
	public static final String TRANSMITTING_TEXT = "TRANSMITTING";
	public static final String WAITING_TEXT = "WAITING";
	public static final String RECEIVING_TEXT = "RECEIVING";

	/** Record status handler */
	public static final String STANDBY = "standby";
	public static final String TRANSMITTING = "transmitting";
	public static final String WAITING = "waiting";
	public static final String RECEIVING = "receiving";

	/** Clip handler */
	public static final String START = "START";
	public static final String STOP = "STOP";

	/** Record data storage */
	public static final String RECORD_PATH = "data\\records.dat";

	/** Communication handler parameters */
	public static final int COUNTDOWN_SEC = 10;
	public static final int RECONNECT_MILLISEC = 250;
	public static final int TRIES_MAX = 4;

	/** Audio sampling values */
	public static final float SAMPLE_RATE = 8000.0F; // Samples per second: 8000, 11025, 16000, 22050, 44100
	public static final int SAMPLE_SIZE_IN_BITS = 8; // Bits in each sample: 8, 16
	public static final int CHANNELS = 1; // Mono: 1, Stereo: 2
	public static final boolean SIGNED = false; // Data signed or unsigned
	public static final boolean BIG_ENDIAN = true; // bigEndian: ABCD, littleEndian: DCBA

	/** Serial parameters */
	public static final int BAUDRATE = 9600;
	public static final int RECEIVED_MAX_SIZE = 1024;
	public static final int PACKET_SIZE = 640;

	/** Frame data types */
	public static final int REQUEST_COMMUNICATION = 0;
	public static final int CONFIRM = 1;
	public static final int START_FRAME = 2;
	public static final int FRAME_IN_BETWEEN = 3;
	public static final int FINAL_FRAME = 4;
	public static final int FINISH_COMMUNICATION = 5;
	public static final int REJECT = 6;
	
	/** Synchronizations symbols */
	public static final byte QAM_S0 = 0;
	public static final byte QAM_S2 = 2;
	public static final byte QAM_S5 = 5;
	public static final byte QAM_S6 = 6;

	/** Frame parameters */
	public static final int FRAME_LENGTH = 37;
	public static final int PREAMBLE_LENGTH = 2;
	public static final int TYPE_ID_LENGTH = 1;
	public static final int DATAlENGTH_LENGTH = 1;
	public static final int HEADER_LENGTH = PREAMBLE_LENGTH + TYPE_ID_LENGTH + DATAlENGTH_LENGTH;
	public static final int DATA_LENGTH = 32;
	public static final int CHECKSUM_LENGTH = 1;
}