import java.io.*;
import java.net.*;

public class ServerManager
{
    Mic1Wrapper mic1;
    FileManager fileManager;

    ServerSocket serverSocket;
    Socket socket;

    DataInputStream in;
    DataOutputStream out;

    public ServerManager(int port)
    {
        //Start the MIC1
        try
        {
            fileManager = new FileManager();
            String path = fileManager.PathToMicroprogram();

            System.out.println("Starting MIC-1 with microprogram at path: " + path);
            mic1 = new Mic1Wrapper(path);
        }
        catch(Exception e)
        {
            System.out.println("Failed to start MIC-1");
        }

        //Start the server socket
        try
        {
            serverSocket = new ServerSocket(port); //Initial socket
            System.out.printf("Waiting for a new client to connect..." + '\n');
            socket = serverSocket.accept(); //Waits for client to connect

            System.out.printf("New Client has connected with the IP: " + socket.getLocalSocketAddress() + '\n');

            //Create streams to be used in the process
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

    public void StartListening() throws IOException
    {
        //This part runs until it's told to break
        while(true)
        {
            if (in.available() > 0) //We have some bytes to read
            {
                int num = in.readInt();
                MESSAGE_TYPE type = MESSAGE_TYPE.GetValue(num);
                System.out.printf("Message type received: " + type.toString() + '\n');
                ProcessMessage(type);
            }
        }
    }

    void ProcessMessage(MESSAGE_TYPE type)
    {
        switch (type)
        {
            case ERROR:
                System.out.println("Error happened while processing message");
                break;

            case UTF:
                try {
                    String s = in.readUTF();
                    System.out.println(s);
                } catch (IOException e) { e.printStackTrace(); }
                break;

            case PROGRAM_TRANSFER_START:
                try {
                    ReceiveFile();
                } catch (IOException e){ e.printStackTrace(); }
                break;

                default:
                    System.out.println("Couldn't find a situation for this type of message");
                    break;
        }
    }

    void ReceiveFile() throws IOException
    {
        long fileSize = in.readLong();
        String fileName = in.readUTF();

        int totalRead = 0;
        int read = 0;
        int remaining = (int)fileSize;

        byte[] buffer = new byte[4096];
        FileOutputStream fos = new FileOutputStream(fileManager.PathToFiles() + fileName);

        while ((read = in.read(buffer, 0, Math.min(buffer.length, remaining))) > 0)
        {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }

        //fos.close();
    }

    public void Shutdown() throws IOException
    {
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
    }
}
