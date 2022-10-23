import java.net.*;
import java.io.*;

public class Server {

	public static void main(String[] args) {
		
        /*
		 * Publish on the port given by command line
         * Otherwise publish on port 9998
		 */
		System.out.println("This is the server");
		int port = -1;
		if (args.length > 0) { 
		    try {
		    	port = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0] + " must be an integer.");
		        System.exit(1);
		    }
		}else {
			port = 9998;
		}		
		
		
		

		//String host =  "localhost";
        System.out.println("Listening to port: " + port);

        ServerSocket serverSocket = null;
        Socket clientSocket = null;        
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader stdIn = null;

        // Wait for connection with an client
	    try {
	    
			serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(3*1000);
	        clientSocket = serverSocket.accept();
	        System.out.println("Client accepted at remote adress " + clientSocket.getRemoteSocketAddress());            
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        stdIn = new BufferedReader (new InputStreamReader ( System.in ));
	        
	        }
	    catch (SocketTimeoutException e) {
	    	System.out.println("Server timed out! Exitting!");
	    	System.out.println(e.getMessage());
	    	System.exit(1);
	    }
	    catch (Exception e) {
	    	System.out.println("There is a problem in connection!");
	    	System.out.println(e.getMessage());
	    }
     
        
	    try {
	    	while (true) {
	    		// Read and print client message
	    		String inputLine;
		        inputLine = in.readLine();
		        System.out.println ( "<<< " + inputLine );
		        
		        if (inputLine.equals("exit")) {
					System.out.println("Exitting the chat application!");
			        System.exit(1);
				}
		        
		        // Send a message from server
		        System.out.print(">>> ");
		        String userInput;
		        userInput = stdIn.readLine();
		        out.println ( userInput );
		        
		        if (userInput.equals("See you")) {
					System.out.println("Exitting the chat application!");
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
