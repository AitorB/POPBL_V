package communication;

import main.References;

public class FrameManagement {

	byte[] frame;
	byte[] header;
	int dataSize;
	byte counter = 0;
	byte checksum = 0;

	public FrameManagement() {

	}

	/**
	 * Method to send the data frame dataType == 0 Request communication dataType ==
	 * 1 Confirm dataType == 2 Start frame dataType == 3 Frame in-between dataType
	 * == 4 Final frame dataType == 5 Finish communication
	 */
	public byte[] createFrame(byte[] data, int dataType) {

		this.header = obtainHeader(data, dataType);

		frame = new byte[References.FRAME_LENGTH];

		System.arraycopy(header, 0, frame, 0, References.HEADER_LENGTH);
		System.arraycopy(data, 0, frame, References.HEADER_LENGTH, References.DATA_LENGTH);
		System.arraycopy(checksum, 0, frame, References.HEADER_LENGTH + References.DATA_LENGTH,
				References.CHECKSUM_LENGTH);

		return this.frame;
	}

	
	/** Proceso de lectura de frame
	 *  1) comprobar que el  checkSum está bien
	 *  2) Si está bien,coger contador y validar
	 *  3) Leer tipo de frame
	 *  	3.1) value == 0 : enviar frame con value = 1. (Confirmación)
	 *  	3.2) value == 1 : enviar frames con datos y value = 2 (startFrame = true) || Luego con value = 3
	 *  	3.3) value == 2 : Leer primera trama del paquete
	 *  	3.4) value == 3 : Leer trama intermedia
	 *  	3.5) value == 4 : Leer trama final del paquete
	 *  	3.6) value == 5 : Leer final de la comunicación
	 *  	
	 *  */
	public byte[] readFrame(byte[] frame) {
		byte[] frame2 = null;
		
		return frame;
	}

	public byte[] obtainHeader(byte[] data, int dataType) {
		byte s0, s2, s5, s6;
		s0 = References.QAM_S0 << 4;
		s2 = References.QAM_S2;
		s5 = References.QAM_S5 << 4;
		s6 = References.QAM_S6;

		byte[] preamble = { (byte) (s0 + s2), (byte) (s5 + s6) };

		counter = (byte) (counter << 4);
		byte type = (byte) dataType;

		byte counterType = (byte) (counter + type);

		byte length = (byte) data.length;

		byte[] header = new byte[References.HEADER_LENGTH];

		System.arraycopy(preamble, 0, header, 0, preamble.length);
		System.arraycopy(counterType, 0, header, preamble.length, 1);
		System.arraycopy(length, 0, header, preamble.length + 1, 1);

		if (counter == 15) {
			counter = 0;
		} else {
			counter++;
		}

		return header;
	}
	
	
	public boolean validateFrame(byte type, byte checkSum) {
		boolean valid = false;

		return valid;
	}
	
	public boolean validateCheckSum() {
		boolean valid = true;
		
		return valid;
	}
}
