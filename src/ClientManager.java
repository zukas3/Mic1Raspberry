import java.io.*;
import java.net.*;

public class ClientManager
{
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public ClientManager(String address, int port) {
        try {
            System.out.println("Connecting to " + address + " on port " + port);
            socket = new Socket(address, port);

            System.out.println("Just connected to " + socket.getRemoteSocketAddress());

            OutputStream outToServer = socket.getOutputStream();
            out = new DataOutputStream(outToServer);

            InputStream inFromServer = socket.getInputStream();
            in = new DataInputStream(inFromServer);

            System.out.println("Server says " + in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendFile(String filePath) throws IOException {

        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[4096];

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
