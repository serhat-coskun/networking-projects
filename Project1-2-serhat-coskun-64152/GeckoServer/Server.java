
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class Server
{   
    private int port;
    private int timeoutSeconds;
    private ServerSocket serverSocket;
    public static final int DEFAULT_SERVER_PORT = 8887;
    private static final int MINUTE = 1000;
    /**
     * Initiates a server socket on the input port, listens to the line, on receiving an incoming
     * connection creates and starts a ServerThread on the client
     * @param port
     */
    public Server(int port, int timeoutSeconds)
    {  

        this.port = port;
        this.timeoutSeconds = timeoutSeconds;

        try
        {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(MINUTE * this.timeoutSeconds);
            System.out.println("Oppened up a server socket on " + Inet4Address.getLocalHost());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("Server class.Constructor exception on oppening a server socket");
        }
        while (true)
        {
            ListenAndAccept();
        }
    }

    /**
     * Listens to the line and starts a connection on receiving a request from the client
     * The connection is started and initiated as a ServerThread object
     */
    private void ListenAndAccept()
    {
        Socket s;
        try
        {
            s = serverSocket.accept();
            s.setSoTimeout(MINUTE * this.timeoutSeconds);
            System.out.println("A connection was established with a client on the address of " + s.getRemoteSocketAddress());
            ServerThread st = new ServerThread(s);
            st.start();
        }
        catch (SocketTimeoutException e) {
	    	System.out.println("Client timed out! Exitting!");
	    	System.out.println(e.getMessage());
	    	System.exit(1);
	    }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Server Class.Connection establishment error inside listen and accept function");
        }  
    }

}

