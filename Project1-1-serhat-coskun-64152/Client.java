import java.net.*;
import java.io.*;

public class Client {


	public static void main(String[] args) {
		
		/*
		 * Connect to the port given by command line
         * Otherwise connect to port 9998
		 */
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
			port = 9998;
            host =  "localhost";
		}			

		
		System.out.println("Binding to port localhost: " + port);

		Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader stdIn = null;
        
        // Establish connection with the server
	    try {
	    
	    	echoSocket = new Socket (host, port);
	    	System.out.println("Connected to server at remote adress " + echoSocket.getRemoteSocketAddress());
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

	}

}
