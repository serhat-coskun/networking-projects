import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Yahya Hassanzadeh on 20/09/2017.
 */

public class ConnectionToServer
{
    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 8887;
    private Socket s;
    //private BufferedReader br;
    private BufferedReader is;
    private PrintWriter os;
    private StructuredMessageProtocol protocol = new StructuredMessageProtocol();
    private String serverAddress;
    private int serverPort;

    /**
     *
     * @param address IP address of the server, if you are running the server on the same computer as client, put the address as "localhost"
     * @param port port number of the server
     */
    public ConnectionToServer(String address, int port)
    {
        this.serverAddress = address;
        this.serverPort    = port;
    }

    /**
     * Establishes a socket connection to the server that is identified by the serverAddress and the serverPort
     */
    public void Connect()
    {
        try
        {
            s = new Socket(serverAddress, serverPort);
            //br= new BufferedReader(new InputStreamReader(System.in));
            /*
            Read and write buffers on the socket
             */
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

            System.out.println("Successfully connected to " + serverAddress + " on port " + serverPort);
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            System.err.println("Error: no server has been found on " + serverAddress + "/" + serverPort);
        }
    }

    /**
     * sends the message String to the server and retrives the answer
     * @param message input message string to the server
     * @return the received server answer
     */
    public String SendForAnswer(String message)
    {
        String response = new String();
        try
        {
            /*
            Sends the message to the server via PrintWriter
             */
            os.println(message);
            os.flush();
            /*
            Reads a line from the server via Buffer Reader
             */
            response = is.readLine();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
        }
        return response;
    }


    /**
     * Disconnects the socket and closes the buffers
     */
    public void Disconnect()
    {
        try
        {
            is.close();
            os.close();
            //br.close();
            s.close();
            System.out.println("ConnectionToServer. SendForAnswer. Connection Closed");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

	public String sendRequest(ArrayList<String> request, String originalMessage) {
		String response = new String();
        try
        {
            /*
            Sends the message to the server via PrintWriter
             */
        	
        	os.println(request.remove(0));
            os.flush();
        	for (String packet : request) {
        		os.println(packet);
                os.flush();
			}
            
        	
        	String recievedPacket = is.readLine();
        	byte numberOfPackets = protocol.getNumberOfPackets(recievedPacket);
            ArrayList<String> packets = new ArrayList<String>();
            for (int i = 0; i < numberOfPackets; i++) {
            	recievedPacket = is.readLine();
            	packets.add(recievedPacket);
			}
            
            
            
    		ArrayList<byte[]> listOfPacketsAsBytes = protocol.fromListOfStringToBytes(packets);
    		byte messageType = protocol.getMessageType(listOfPacketsAsBytes.get(0));
    		if (messageType != StructuredMessageProtocol.REQUEST) {
    			return "Error!";
    		}
    		String messageData = protocol.getMessageData(listOfPacketsAsBytes);
    		
    		ClientCommandParser parser = new ClientCommandParser();
    		
    		String type = parser.getRequestTypeString(originalMessage);
    		
    		if (type.equals("pricing")) {
    			JSONObject jsonObject = new JSONObject(messageData);
        		String out = jsonObject.toString();
	            System.out.println(out);

	            return "\n";
			}
    		else if (type.equals("listing")) {
    			JSONArray jsonObject = new JSONArray(messageData);
    	        for (int i = 0; i < jsonObject.length(); i++)
    	        {
    	            String id = jsonObject.getJSONObject(i).getString("id");
    	            String name = jsonObject.getJSONObject(i).getString("name");
    	            System.out.println(id + "\t" + name);
    	            return "\n";
    	        }
			}else {
				return "error!";
			}
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
        }
        return response;
		
	}
}
