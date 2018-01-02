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
        System.out.println("Enter path to a file you want to send: ");
        String filePath = scanner.nextLine();
        try {
            SendFile(filePath);
        } catch (IOException e) { e.printStackTrace(); }



        //Loops until meets a break
        while(true)
        {
            //First check if we have something to read
            try {
                while(in.available() > 0) //We have some bytes to read
                {
                    int num = in.readInt();
                    MESSAGE_TYPE type = MESSAGE_TYPE.GetValue(num);
                    System.out.printf("Message type received: " + type.toString() + '\n');
                    ProcessMessage(type);
                }
            } catch (IOException e) { e.printStackTrace(); }

            //Otherwise we
            try {
                System.out.println("Enter a char to send: ");
                char c = scanner.next().charAt(0);
                SendCharInput(c);

            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void ProcessMessage(MESSAGE_TYPE type)
    {
        switch(type)
        {
            case OUTPUT_CHAR:
                try {
                    ReceiveChar();
                } catch (IOException e) { e.printStackTrace(); }
                break;
        }
    }

    public void ReceiveChar() throws IOException
    {
        char c = in.readChar();
        System.out.println(c);
    }

    //Not used anymore
    public void SendUTF(String message) throws IOException
    {
        System.out.println("Sending UTF message: " + message);
        out.writeInt(MESSAGE_TYPE.UTF.getValue());
        out.writeUTF(message);
    }

    public void SendCharInput(char c) throws IOException
    {
        System.out.println("Sending key input: " + c);
        out.writeInt(MESSAGE_TYPE.INPUT_CHAR.getValue());
        out.writeChar(c);
    }

    public void SendFile(String filePath) throws IOException {

        out.writeInt(MESSAGE_TYPE.PROGRAM_TRANSFER_START.getValue());

        File file = new File(filePath);
        long fileSize = file.length();
        String fileName = file.getName();
        System.out.println("File size in bytes: " + fileSize);

        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        out.writeLong(fileSize);
        out.writeUTF(fileName);

        while (fis.read(buffer) > 0) {
            out.write(buffer);
        }

        fis.close();
        System.out.println("File sent: " + fileName);
    }

    //Unfuctional
    public void SendFile() throws IOException {

        System.out.println("Enter path to a file you want to send: ");

        String filePath = scanner.nextLine();
        long fileSize = new File(filePath).length();

        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
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
