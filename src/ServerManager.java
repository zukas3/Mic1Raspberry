import java.io.*;
import java.net.*;

public class ServerManager
{
    ServerSocket serverSocket;
    Socket socket;

    DataInputStream in;
    DataOutputStream out;

    public ServerManager(int port)
    {
        try
        {
            serverSocket = new ServerSocket(port); //Initial socket
            socket = serverSocket.accept(); //Waits for client to connect

            System.out.printf("New Client has connected with the IP: " + socket.getLocalSocketAddress());

            OutputStream outToServer = socket.getOutputStream(); //Output
            out = new DataOutputStream(outToServer);

            InputStream inFromServer = socket.getInputStream(); //Input
            in = new DataInputStream(inFromServer);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

    }
}
