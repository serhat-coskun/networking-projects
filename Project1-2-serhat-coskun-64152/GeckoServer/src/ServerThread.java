

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

class ServerThread extends Thread
{
    private BufferedReader is;
    private PrintWriter os;
    private Socket s;
    private String recievedPacket = new String();
    private StructuredMessageProtocol protocol = new StructuredMessageProtocol();
    private ConnectionToGecko geckoConnection = new ConnectionToGecko();

    /**
     * Creates a server thread on the input socket
     *
     * @param s input socket to create a thread on
     */
    public ServerThread(Socket s)
    {
        this.s = s;
    }

    /**
     * The server thread, echos the client until it receives the QUIT string from the client
     */
    public void run()
    {
        try
        {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

        }
        catch (IOException e)
        {
            System.err.println("Server Thread. Run. IO error in server thread");
        }

        try
        {
            recievedPacket = is.readLine();
            byte messageType = protocol.getMessageType(recievedPacket);
            
            while (messageType != StructuredMessageProtocol.TERMINATION) {
				
            	byte numberOfPackets = protocol.getNumberOfPackets(recievedPacket);
                ArrayList<String> packets = new ArrayList<String>();
                for (int i = 0; i < numberOfPackets; i++) {
                	recievedPacket = is.readLine();
                	packets.add(recievedPacket);
    			}
                
                ArrayList<String> responsePackets = geckoConnection.evaluateRequest(packets);
            	
                os.println(responsePackets.remove(0));
                os.flush();
                for (String packet : responsePackets) {
                	os.println(packet);
                    os.flush();
				}
                
            	recievedPacket = is.readLine();
                messageType = protocol.getMessageType(recievedPacket);
			}
            
        }
        
        
        catch (IOException e)
        {
            recievedPacket = this.getName(); //reused String line for getting thread name
            System.err.println("Server Thread. Run. IO Error/ Client " + recievedPacket + " terminated abruptly");
        }
        catch (NullPointerException e)
        {
            recievedPacket = this.getName(); //reused String line for getting thread name
            System.err.println("Server Thread. Run.Client " + recievedPacket + " Closed");
        } finally
        {
            try
            {
                System.out.println("Closing the connection");
                if (is != null)
                {
                    is.close();
                    System.err.println(" Socket Input Stream Closed");
                }

                if (os != null)
                {
                    os.close();
                    System.err.println("Socket Out Closed");
                }
                if (s != null)
                {
                    s.close();
                    System.err.println("Socket Closed");
                }

            }
            catch (IOException ie)
            {
                System.err.println("Socket Close Error");
            }
        }//end finally
    }
}
