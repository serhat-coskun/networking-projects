import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClientCommandParser {
	
	private final StructuredMessageProtocol protocol = new StructuredMessageProtocol();	
	
	
	public String getRequestTypeString(String message) {
		StringTokenizer messageTokens = new StringTokenizer(message);
		messageTokens.nextToken();
		return messageTokens.nextToken();
	}
	
	public ArrayList<String> getRequestAsSMP(String message) {
		
		StringTokenizer messageTokens = new StringTokenizer(message);
		int numberOfTokens = messageTokens.countTokens();
    	String command = messageTokens.nextToken(); 
    	byte commandType;
    	if (command.equals("termination")) {
    		commandType = StructuredMessageProtocol.TERMINATION;
    	}else if (command.equals("request")) {
    		commandType = StructuredMessageProtocol.REQUEST;
		}else {
			commandType = StructuredMessageProtocol.ERROR;
		}
    			
		if (commandType == StructuredMessageProtocol.TERMINATION) {
			return protocol.getTerminationPacketsAsString("");
		}else if (commandType == StructuredMessageProtocol.REQUEST) {
			if (numberOfTokens == 2) {
				String data = "{command:listing, arguments:a}";
				return protocol.getListOfPacketsAsString(commandType, data);
				
			}else if (numberOfTokens == 3) {
		    	String specificCommand = messageTokens.nextToken(); 
		    	String arguments =  messageTokens.nextToken(); 
		    	String data = "{command:pricing, arguments:"+arguments+"}";
				return protocol.getListOfPacketsAsString(commandType, data);
		    	
			}else {
				return protocol.getErrorPacketsAsString("");
			}
		}else {
			return protocol.getErrorPacketsAsString("");
		}
	}
}
