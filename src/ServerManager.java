import java.io.*;
import java.net.*;

public class ServerManager
{
    ServerSocket serverSocket;
    Socket clientSocket;

    public ServerManager(int port)
    {
        try
        {
            serverSocket = new ServerSocket(port); //Initial socket
            clientSocket = serverSocket.accept(); //Waits for client to connect
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); //Output
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Input
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

    }
}
