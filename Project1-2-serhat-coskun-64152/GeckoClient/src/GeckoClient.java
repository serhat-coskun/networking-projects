import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GeckoClient {



        
	public static void main(String[] args) {
		
		/*
		 * Connect to the port given by command line
         * Otherwise connect to port 9998
		 */
		ClientCommandParser commandParser = new ClientCommandParser();
		
		System.out.println("This is the client");
		int port = -1;
        String host;
		if (args.length > 0) {
		    try {
		    	port = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0] + " must be an integer.");
		        System.exit(1);
		    }

            host = args[1];

		}else {
			port = 8887;
            host =  "localhost";
		}			
        
        // TODO code application logic here
        ConnectionToServer connectionToServer = new ConnectionToServer(host, port);
        connectionToServer.Connect();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter a message for the echo");
        String message = scanner.nextLine();
        while (!message.equals("termination"))
        {	
        	ArrayList<String> request = commandParser.getRequestAsSMP(message);
        	connectionToServer.sendRequest(request, message);
            message = scanner.nextLine();
        }

        connectionToServer.Disconnect();
        scanner.close();
		
	}

}


/*

System.out.println("Binding to port localhost: " + port);

		Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader stdIn = null;
        
        // Establish connection with the server
	    try {
	    
	    	echoSocket = new Socket (host, port);
	    	System.out.println("Connected to server at remote adress " + echoSocket.getRemoteSocketAddress());
            System.out.println("Timeout settings for client time: " + echoSocket.getSoTimeout());
            out = new PrintWriter ( echoSocket.getOutputStream (), true );
            in = new BufferedReader (new InputStreamReader ( echoSocket.getInputStream () ) );
            stdIn = new BufferedReader (new InputStreamReader ( System.in ));
	    } 	 	
	    catch (Exception e) {
	    	System.out.println("There is a problem in connection!");
	    	System.out.println(e.getMessage());
	    }
     
        // Exchange messages with the server
	    try {
	    	while (true) {
	    		System.out.print(">>> ");
		        String userInput;
		        userInput = stdIn.readLine();
		        out.println ( userInput );
		        
		        if (userInput.equals("exit")) {
					System.out.println("Exitting the chat application!");
			        System.exit(1);
				}
		        
		        String recievedMessage = in.readLine();
		        System.out.println("<<< " + recievedMessage); 
		          
		        if (recievedMessage.equals("exit")) {
					System.out.println("Exitting the chat application!");
			        System.exit(1);
				}

                if (recievedMessage.equals("timeout")) {
					System.out.println("Can't send the message!");
                    System.out.println("Server timedout because of idleness!");
			        System.exit(1);
				}
			}
	    }
	    catch (Exception e) {
			System.out.println("There is a problem with message transfer!");
			System.out.println(e.getMessage());
		}   



*/




/*

	import org.json.JSONArray;
	import org.json.JSONObject;
	private String processRequest(String request) throws MalformedURLException, IOException {
	    
	    
	    
	    JSONArray jsonObject = new JSONArray(inline);
	    for (int i = 0; i < jsonObject.length(); i++)
	    {
	        String id = jsonObject.getJSONObject(i).getString("id");
	        String name = jsonObject.getJSONObject(i).getString("name");
	        System.out.println(id + "\t" + name);
	    }
	    
	}
*/