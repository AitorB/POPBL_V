package communication;

import main.References;

public class FrameManagement {
	
	byte[] frame;
	byte[] header;
	int dataSize;
	byte counter = 0;
	
	public byte[] createFrame(byte[] data, int dataType) {
		
		this.header = obtainHeader(data,dataType);
		return this.frame;
	}
	
	public String readFrame(byte[] frame) {
		String frameType = null;
		
		return frameType;
	}
	
	public byte[] obtainHeader(byte[] data,int dataType) {
		byte s0,s2,s5,s6;
	    s0 = References.QAM_S0 << 4;
		s2 = References.QAM_S2;
		s5 = References.QAM_S5 << 4;
		s6 = References.QAM_S6;
		
		byte[] preamble = {(byte) (s0+s2), (byte) (s5+s6)};
		
		counter = (byte) (counter << 4);
		byte type = (byte) dataType;
		
		byte counterType = {counter+type};
		
		byte length = (byte) data.length;
		
		byte[] header = {preamble[0],preamble[1],counterType,length};
		
		if(counter == 16) {
			counter = 0;
		}
		counter++;
		
		return header;
	}
}
