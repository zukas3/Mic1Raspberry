import java.util.Scanner;

public class Main {

    static ServerManager serverManager;
    static ClientManager clientManager;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.printf("Do you want to start a (S)erver or (C)lient? ");
        String s = scanner.nextLine();

        if(s == "S")
            InitializeServer();
        else if(s == "C")
            InitializeClient();


    }

    public static void InitializeServer()
    {
        System.out.printf("Enter a port you want to use for the server ");
        String s = scanner.nextLine();

        int port = Integer.parseInt(s);

        serverManager = new ServerManager(port);
    }

    public static void InitializeClient()
    {
        System.out.printf("Enter the IP and port that you want to connect to, separated by a ':'\n");
        String s = scanner.nextLine();
        String[] split = s.split(":");
        clientManager = new ClientManager(split[0],Integer.parseInt(split[1]));
    }
}

