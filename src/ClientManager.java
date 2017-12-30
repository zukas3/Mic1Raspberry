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
            try (DataInputStream dataInputStream = in = new DataInputStream(inFromServer)) {
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StartBuffer()
    {
        //Loops until meets a break
        while(true)
        {
            try {
                System.out.println("Enter path to a file you want to send: ");
                String filePath = scanner.nextLine();
                SendFile(filePath);

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

        out.writeInt(MESSAGE_TYPE.PROGRAM_TRANSFER_START.getValue());

        File file = new File(filePath);
        long fileSize = file.length();
        String fileName = file.getName();
        System.out.println("File size in bytes: " + fileSize);

        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[4096];
        out.writeLong(fileSize);
        out.writeUTF(fileName);

        while (fis.read(buffer) > 0) {
            out.write(buffer);
        }

        System.out.println("File sent: " + fileSize);
    }

    //Unfuctional
    public void SendFile() throws IOException {

        System.out.println("Enter path to a file you want to send: ");

        String filePath = scanner.nextLine();
        long fileSize = new File(filePath).length();

        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[4096];
        //out.writeInt();

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
