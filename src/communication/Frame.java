/** @file Frame.java
 *  @brief Frame class to store received and send frames data
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

import java.util.Arrays;

import main.References;

public class Frame {
	private byte[] preamble;
	private int type;
	private int id;
	private byte typeId;
	private int dataLength;
	private byte[] data;
	private byte checksum;

	private byte[] frame;

	/** Constructor: Send data frame */
	public Frame(int type, int id, byte[] data) {
		this.preamble = generatePreamble();
		this.type = type;
		this.id = id;
		this.typeId = generateTypeId((byte) type, (byte) id);

		this.dataLength = data.length;
		this.data = data;

		this.checksum = generateChecksum();

		this.frame = new byte[References.FRAME_LENGTH];
		generateFrame();
	}

	private byte[] generatePreamble() {
		byte s0, s2, s5, s6;

		s0 = References.QAM_S0 << 4;
		s2 = References.QAM_S2;
		s5 = References.QAM_S5 << 4;
		s6 = References.QAM_S6;

		byte[] symbols = { (byte) (s0 + s2), (byte) (s5 + s6) };

		return symbols;
	}

	private byte generateTypeId(byte type, byte id) {
		return (byte) (((byte) (type << 4)) + id);
	}

	public byte generateChecksum() {
		byte code;
		
		code = 0x00;
		
		return code;
	}

	private void generateFrame() {
		System.arraycopy(generateHeader(), 0, frame, 0, References.HEADER_LENGTH);
		System.arraycopy(data, 0, frame, References.HEADER_LENGTH, References.DATA_LENGTH);
		frame[References.HEADER_LENGTH + References.DATA_LENGTH] = checksum;
	}

	private byte[] generateHeader() {
		byte[] header = new byte[References.HEADER_LENGTH];

		System.arraycopy(preamble, 0, header, 0, References.PREAMBLE_LENGTH);
		header[References.PREAMBLE_LENGTH] = typeId;
		header[References.PREAMBLE_LENGTH + References.TYPE_ID_LENGTH] = (byte) dataLength;

		return header;
	}
	
	/** Constructor: Receive data frame */
	public Frame(byte[] frame) {
		this.preamble = Arrays.copyOfRange(frame, 0, 1);
		this.type = frame[2] >> 4;
		this.id = 0x07 & frame[2]; 
		this.typeId = frame[2];
		this.dataLength = frame[3];
		this.data = Arrays.copyOfRange(frame, 4, 35);
		this.checksum = frame[36];
		
		this.frame = frame;
	}
	
	/** Getters */
	public byte[] getPreamble() {
		return this.preamble;
	}

	public int getType() {
		return this.type;
	}
	
	public int getId() {
		return this.id;
	}
	
	public byte getTypeId() {
		return this.typeId;
	}

	public int getDataLength() {
		return this.dataLength;
	}

	public byte[] getData() {
		return this.data;
	}

	public byte getChecksum() {
		return this.checksum;
	}

	public byte[] getFrame() {
		return this.frame;
	}
}