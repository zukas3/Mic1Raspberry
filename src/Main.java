import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

public class Main {

    static ServerManager serverManager;
    static ClientManager clientManager;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        if(args.length > 1)
        {

            if(args[0].equals("server"))
            {
                int port = Integer.parseInt(args[1]);
                InitializeServer(port);
            }
            else if(args[0].equals("client"))
            {
                int port = Integer.parseInt(args[2]);
                InitializeClient(args[1], port);
            }

        } else {

            CreateIntroUI();

        }
    }

    //Initialize without initial port
    public static void InitializeServer()
    {
        System.out.printf("Enter a port you want to use for the server ");
        String s = scanner.nextLine();

        int port = Integer.parseInt(s);

        //Start server and start listening
        serverManager = new ServerManager(port);
        try {
            serverManager.StartListening();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //Initialize with a port
    public static void InitializeServer(int port)
    {
        serverManager = new ServerManager(port);
    }

    public static void InitializeClient()
    {
        System.out.printf("Enter the IP and port that you want to connect to, separated by a ':'\n");
        String s = scanner.nextLine();
        String[] split = s.split(":");

        //Start client and start text buffer
        clientManager = new ClientManager(split[0],Integer.parseInt(split[1]));
    }


    public static void InitializeClient(String ip, int port)
    {
        //Start client and start text buffer
        CreateMainUI();
        clientManager = new ClientManager(ip, port);
    }

    public static void CreateIntroUI()
    {
        ServerClient serverClient = new ServerClient();
    }

    public static void CreateMainUI()
    {
        SwingUtilities.invokeLater(new WindowUI());
        //WindowUI windowUI = new WindowUI();
    }
}

