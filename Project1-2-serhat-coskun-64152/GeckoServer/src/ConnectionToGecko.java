/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.MessageFormat;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author AbdulrazakZakieh
 */
public class ConnectionToGecko {

	
	public ConnectionToGecko() {
		super();
	}
	
	public String getCoinPrice(String coinId) throws MalformedURLException, IOException {
		String request = MessageFormat.format("https://api.coingecko.com/api/v3/simple/price?ids={0}&vs_currencies=try", coinId);
		String response = this.requestFromCoinGecko(request);
		return response;
	}
	
	public String getListOfCoins() throws MalformedURLException, IOException {
		
		String response = this.requestFromCoinGecko("https://api.coingecko.com/api/v3/coins/list");
		return response;
	}
	
	private String requestFromCoinGecko(String httpRequest) throws MalformedURLException, IOException{
		URL url = new URL(httpRequest);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); 
        conn.connect();
        String inline = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }
        //Close the scanner
        scanner.close();  
		return inline;
	}
	
	public ArrayList<String> evaluateRequest(ArrayList<String> listOfPackets)  throws MalformedURLException, IOException{
		
		String responseFromGecko;
		StructuredMessageProtocol protocol = new StructuredMessageProtocol();
		ArrayList<byte[]> listOfPacketsAsBytes = protocol.fromListOfStringToBytes(listOfPackets);
		
		byte messageType = protocol.getMessageType(listOfPacketsAsBytes.get(0));
		if (messageType != StructuredMessageProtocol.REQUEST) {
			return protocol.getErrorPacketsAsString("Not a request!");
		}
		String messageData = protocol.getMessageData(listOfPacketsAsBytes);
		JSONObject jsonObject = new JSONObject(messageData);
		
		String command = jsonObject.getString("command");
		String arguments = jsonObject.getString("arguments");
		
		if ("pricing".equals(command)){
			responseFromGecko = this.getCoinPrice(arguments);
			
		}else if ("listing".equals(command)) {
			responseFromGecko = this.getListOfCoins();
		}else {
			return protocol.getErrorPacketsAsString("Does not have proper command in the request!");
		}
		
		return protocol.getListOfPacketsAsString(StructuredMessageProtocol.RESPONSE, responseFromGecko);
	}
 
}
