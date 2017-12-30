import java.io.IOException;
import java.util.Scanner;

public class Main {

    static ServerManager serverManager;
    static ClientManager clientManager;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        if(args.length > 1)
        {
            int port = Integer.parseInt(args[1]);

            if(args[0].equals("server"))
                InitializeServer(port);
            else
                InitializeClient(args[0],port);

        } else {

            System.out.printf("Do you want to start a (S)erver or (C)lient? ");
            String s = scanner.nextLine();

            System.out.printf("User entered: " + s + '\n');

            if (s.equals("S"))
                InitializeServer();
            else if (s.equals("C"))
                InitializeClient();
            else
                System.out.printf("Couldn't understand the given command... ");

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
        try {
            serverManager.StartListening();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void InitializeClient()
    {
        System.out.printf("Enter the IP and port that you want to connect to, separated by a ':'\n");
        String s = scanner.nextLine();
        String[] split = s.split(":");

        //Start client and start text buffer
        clientManager = new ClientManager(split[0],Integer.parseInt(split[1]));
        clientManager.StartBuffer();
    }

    public static void InitializeClient(String ip, int port)
    {
        //Start client and start text buffer
        clientManager = new ClientManager(ip,port);
        clientManager.StartBuffer();
    }

    public static void CreateUI(){
        WindowUI windowUI = new WindowUI();
    }
}

