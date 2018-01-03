import java.io.*;
import java.net.*;

public class ServerManager
{
    public static ServerManager main;

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
            e.printStackTrace();
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

            main = this;
            StartListening();
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

            case INPUT_CHAR:
                try {
                    ProcessCharInput();
                } catch (IOException e) { e.printStackTrace(); }
                break;

            case PROGRAM_TRANSFER_START:
                try {
                    ProcessProgramTransfer();
                } catch (IOException e) { e.printStackTrace(); }
                break;

            default:
                System.out.println("Couldn't find a situation for this type of message");
                break;
        }
    }

    void ProcessCharInput() throws IOException
    {
        char c = in.readChar();
        mic1.PutCharIntoMemory(c);
    }

    void ProcessProgramTransfer() throws IOException
    {
        //Read file size
        long fileSize = in.readLong();
        String fileName = in.readUTF();

        int totalRead = 0;      //Total amount of bytes already processed
        int read = 0;           //Used in a while loop
        int remaining = (int)fileSize;

        byte[] buffer = new byte[1024];
        String path = fileManager.PathToFiles() + "/" + fileName;
        FileOutputStream fos = new FileOutputStream(path);

        while ((read = in.read(buffer, 0, Math.min(buffer.length, remaining))) > 0)
        {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }

        long toSkip = fileSize % 1024;
        in.skip(in.available()); //We gotta clean up the space of buffer leftover
        fos.close();

        File file = new File(fileManager.PathToFiles() + "/" + fileName);
        CheckProgramAndLoad(file);
    }

    void CheckProgramAndLoad(File file)
    {
        String format = "";
        String fileName = "";

        //Separate file name from format name
        int i = file.getName().lastIndexOf('.');
        format = file.getName().substring(i+1);
        fileName = file.getName().substring(0,i); //Without the dot ".", so minus one

        SendStatusMessage("Checking received file");

        if(format.equals("jas"))
        {
            System.out.println("Received file format is JAS, preparing assembler");
            String outPath = fileManager.PathToFiles() + "/" + fileName + ".ijvm";
            IJVMAssembler assembler = new IJVMAssembler(file.getAbsolutePath(), outPath);
            mic1.loadProgram(outPath);

            try {
                mic1.run();
                SendStatusMessage("MIC-1 is now running");
            }
            catch (NullPointerException e)
            {
                System.out.println("Error while trying to run the program");
                SendErrorMessage("Error while trying to run the program");
                Shutdown();
            }
        }
        else if(format.equals("ijvm"))
        {
            System.out.println("Received file format is IJVM, no need to compile");
            mic1.loadProgram(file.getAbsolutePath());

            try {
                mic1.run();
                SendStatusMessage("MIC-1 is now running");
            }
            catch (NullPointerException e)
            {
                System.out.println("Error while trying to run the program");
                SendErrorMessage("Error while trying to run the program");
                //Shutdown();
            }
        }
        else
        {
            System.out.println("Couldn't tell the file format for file at: " + file.getAbsolutePath());
            //Shutdown();
        }
    }

    public void SendOutputChar(char c)
    {
        try {
            out.writeInt(MESSAGE_TYPE.OUTPUT_CHAR.getValue());
            out.writeChar(c);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void SendErrorMessage(String s)
    {
        try {
            out.writeInt(MESSAGE_TYPE.ERROR_MESSAGE.getValue());
            out.writeUTF(s);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void SendStatusMessage(String s)
    {
        try {
            out.writeInt(MESSAGE_TYPE.SERVER_STATUS.getValue());
            out.writeUTF(s);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Shutdown()
    {
        try {
            out.writeInt(MESSAGE_TYPE.CONNECTION_TERMINATION.getValue());

            in.close();
            out.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {e.printStackTrace();};
    }
}
