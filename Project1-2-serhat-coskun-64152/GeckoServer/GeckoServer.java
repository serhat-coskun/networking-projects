/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author AbdulrazakZakieh
 */
public class GeckoServer {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("This is the server");
		
		int port = 9998;
		int timeout = 3;

		if (args.length > 0) { 
		    try {
		    	port = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0] + " must be an integer.");
		        System.exit(1);
		    }

			if (args.length == 2) {
				try {
					timeout = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					System.err.println("Argument" + args[1] + " must be an integer.");
					System.exit(1);
				}
			}
		}


        Server server = new Server(port, timeout);
    }
}
