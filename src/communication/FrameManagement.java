/** @file FrameManagement.java
 *  @brief Class to manage sent and received data frames
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

import main.References;

public class FrameManagement {
	private int sendId;
	private int lastId;
	private int currentId;

	public FrameManagement() {
		sendId = 0;
		lastId = 0;
		currentId = 0;
	}

	/** Request of communication frame */
	public Frame requestCommunicationFrame() {
		byte[] empty = new byte[References.DATA_LENGTH];

		return new Frame(References.REQUEST_COMMUNICATION, sendId, empty);
	}

	/** Confirmation to start communication frame */
	public Frame confirmCommunicationFrame() {
		byte[] empty = new byte[References.DATA_LENGTH];

		return new Frame(References.CONFIRM, sendId, empty);
	}

	/** Start frame */
	public Frame startFrame(byte[] data) {
		generateSendId();

		return new Frame(References.START_FRAME, sendId, data);
	}

	/** Between frame */
	public Frame betweenFrame(byte[] data) {
		generateSendId();

		return new Frame(References.FRAME_IN_BETWEEN, sendId, data);
	}

	/** Final frame */
	public Frame finalFrame(byte[] data) {
		generateSendId();

		return new Frame(References.FINAL_FRAME, sendId, data);
	}

	/** Start frame of communication */
	public Frame finishCommunicationFrame() {
		byte[] empty = new byte[References.DATA_LENGTH];

		generateSendId();

		Frame frame = new Frame(References.FINAL_FRAME, sendId, empty);

		sendId = 0;

		return frame;
	}

	/** Generates next send ID */
	private void generateSendId() {
		if (sendId == 15) {
			sendId = 0;
		} else {
			sendId++;
		}
	}
	
	public boolean validateFrame(Frame frame) {
		boolean valid = false;

		currentId = frame.getId();

		if (validateChecksum(frame)) {
			if (currentId == 0 && lastId == 0) {
				lastId = -1;
			} else if (currentId == 0 && lastId == 15) {
				lastId = -1;
			}
			if (currentId == (lastId + 1)) {
				valid = true;
			} else {
				valid = false;
				System.out.println("ERROR: Invalid ID on frame " + frame.getId());
			}
		} else {
			valid = false;
			System.out.println("ERROR: Invalid checksum on frame " + frame.getId());
		}

		lastId = currentId;

		return valid;
	}

	private boolean validateChecksum(Frame frame) {
		boolean valid = false;

		if (frame.getChecksum() == frame.generateChecksum()) {
			valid = true;
		}

		return valid;
	}
}