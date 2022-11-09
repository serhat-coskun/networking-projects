import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class StructuredMessageProtocol {
	
	
	static final int SIZEOFDATAINBYTES = 16;
	static final int DATAOFFSET = 2;
	
	static final byte REQUEST = 0;
	static final byte RESPONSE = 1;
	static final byte TERMINATION = 2;
	static final byte ERROR = 3;
	
	public StructuredMessageProtocol() {
		super();
	}

	public ArrayList<byte[]> getListOfPackets(byte messageType, String data) {
		
		ArrayList<byte[]> packets = new ArrayList<byte[]>();
		
		byte[] dataInBytes = data.getBytes();
		int numberOfPackets = (dataInBytes.length / SIZEOFDATAINBYTES) + 1;
		
		byte[] numberOfPacketsData = new byte[SIZEOFDATAINBYTES];
		numberOfPacketsData[0] = (byte) numberOfPackets;
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(messageType);
		outputStream.write(0);
		outputStream.write(numberOfPacketsData, 0, SIZEOFDATAINBYTES);
		
	;
		packets.add(outputStream.toByteArray( ));
		
		
		for (byte i = 1; i <= numberOfPackets; i++) {
			byte[] partition = Arrays.copyOfRange(dataInBytes, i * SIZEOFDATAINBYTES, (i + 1) * SIZEOFDATAINBYTES);
			outputStream = new ByteArrayOutputStream();
			outputStream.write(messageType);
			outputStream.write(i);
			outputStream.write(partition, 0, SIZEOFDATAINBYTES);

			byte packet[] = outputStream.toByteArray( );
			packets.add(packet);
		}
		return packets;
	}
	
	public ArrayList<String> getListOfPacketsAsString(byte messageType, String data) {
		return this.fromListOfBytesToString(this.getListOfPackets(messageType, data));
	}
	
	
	public String getMessageData(ArrayList<byte[]> packets) {
		
		
		int numberOfPackets = packets.size();		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		for (byte i = 0; i < numberOfPackets; i++) {
			byte[] packet = packets.get(i);
			byte[] data = Arrays.copyOfRange(packet, DATAOFFSET, SIZEOFDATAINBYTES);
			outputStream.write(data, 0, SIZEOFDATAINBYTES);			
		}  
		
		String fullMessage = new String(outputStream.toByteArray());
		return fullMessage;
	}
	
	
	public byte getMessageType(byte[] packet) {
		return packet[0];
	}
	
	public byte getMessageType(String packet) {
		return packet.getBytes()[0];
	}
	
	public byte getSequenceNumber(byte[] packet) {
		return packet[1];
	}
	
	public byte getSequenceNumber(String packet) {
		return packet.getBytes()[1];
	}
	
	public byte[] getMessagePartOfSinglePacket(String packet) {
		byte[] bytePacket = packet.getBytes();
		byte[] data = Arrays.copyOfRange(bytePacket, DATAOFFSET, SIZEOFDATAINBYTES);
		return data;
	}
	
	public byte getNumberOfPackets(String packet) {
		byte[] messageData = this.getMessagePartOfSinglePacket(packet);
		return messageData[0];
	}
	
	public ArrayList<String> fromListOfBytesToString(ArrayList<byte[]> listOfBytes){
		
		ArrayList<String> listOfString = new ArrayList<String>();
		
		for (Iterator<byte[]> iterator = listOfBytes.iterator(); iterator.hasNext();) {
			String string = new String(iterator.next());
			listOfString.add(string);
		}
		return listOfString;
	}
	
	public ArrayList<byte[]> fromListOfStringToBytes(ArrayList<String> listOfString){
		
		ArrayList<byte[]>  listOfBytes= new ArrayList<byte[]>();
		
		for (Iterator<String> iterator = listOfString.iterator(); iterator.hasNext();) {
			byte[] bytes =iterator.next().getBytes();
			listOfBytes.add(bytes);
		}
		return listOfBytes;
	}
	
	public ArrayList<byte[]> getErrorPackets(String errorMessage) {
		return this.getListOfPackets(StructuredMessageProtocol.ERROR,  "{information:" + errorMessage + "}");
	}
	
	public ArrayList<String> getErrorPacketsAsString(String errorMessage) {
		return this.fromListOfBytesToString(this.getListOfPackets(StructuredMessageProtocol.ERROR,  "{information:" + errorMessage + "}"));
	}
	
	
	public ArrayList<byte[]> getTerminationPackets(String terminationMessage) {
		return this.getListOfPackets(StructuredMessageProtocol.TERMINATION,  "{information:" + terminationMessage + "}");
	}
	
	public ArrayList<String> getTerminationPacketsAsString(String terminationMessage) {
		return this.fromListOfBytesToString(this.getListOfPackets(StructuredMessageProtocol.TERMINATION,  "{information:" + terminationMessage + "}"));
	}
	


}
