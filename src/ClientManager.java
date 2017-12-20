import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientManager
{
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    Scanner scanner = new Scanner(System.in);

    public ClientManager(String address, int port) {
        try {
            System.out.println("Connecting to " + address + " on port " + port);
            socket = new Socket(address, port);

            System.out.println("Just connected to " + socket.getRemoteSocketAddress());

            OutputStream outToServer = socket.getOutputStream();
            out = new DataOutputStream(outToServer);

            InputStream inFromServer = socket.getInputStream();
            in = new DataInputStream(inFromServer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StartBuffer()
    {
        //Loops until meets a break
        while(true)
        {
            String s = scanner.nextLine();
            try {
                SendUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void SendUTF(String message) throws IOException
    {
        System.out.println("Sending UTF message: " + message);
        out.writeInt(MESSAGE_TYPE.UTF.getValue());
        out.writeUTF(message);
    }

    public void SendFile(String filePath) throws IOException {

        long fileSize = new File(filePath).length();

        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[4096];
        //out.writeLong();

        while (fis.read(buffer) > 0) {
            out.write(buffer);
        }
    }

    public void CloseConnection() throws IOException
    {
        out.close();
        in.close();
        socket.close();
    }
}
