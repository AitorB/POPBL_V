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
import controller.KeyListenerPanel;
import controller.RecordPanel;
import controller.StatusPanel;
import resources.Chronometer;
import resources.ClipPlayer;

public final class References {
	/** Main classes references */
	public static RecordPanel RECORD_PANEL;
	public static StatusPanel STATUS_PANEL;
	public static KeyListenerPanel KEYLISTENER_PANEL;
	public static ClipPlayer CLIP_PLAYER;
	public static CommunicationHandler COMMUNICATION_HANDLER;
	public static Chronometer CHRONOMETER;
	
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
	public static final String TRANSMITTING_IMAGE = "image\\green.png";
	public static final String STANDBY_IMAGE = "image\\yellow.png";
	public static final String RECEIVING_IMAGE = "image\\red.png";

	/** Record status handler */
	public static final String TRANSMITTING_TEXT = "TRANSMITTING";
	public static final String STANDBY_TEXT = "STANDBY";
	public static final String RECEIVING_TEXT = "RECEIVING";

	/** Clip handler */
	public static final String START = "START";
	public static final String STOP = "STOP";

	/** Record data storage */
	public static final String RECORD_PATH = "data\\records.dat";
	
	/** Communication terminate duration */
	public final static int DURATION_SEC = 10;

	/** Audio sampling values */
	public final static float SAMPLE_RATE = 8000.0F; // Samples per second: 8000, 11025, 16000, 22050, 44100
	public final static int SAMPLE_SIZE_IN_BITS = 8; // Bits in each sample: 8, 16
	public final static int CHANNELS = 1; // Mono: 1, Stereo: 2
	public final static boolean SIGNED = true; // Data signed or unsigned
	public final static boolean BIG_ENDIAN = false; // bigEndian: ABCD, littleEndian: DCBA
}