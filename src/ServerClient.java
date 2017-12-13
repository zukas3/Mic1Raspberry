import javax.swing.*;
import java.awt.event.ActionListener;


public class ServerClient extends JFrame /*implements ActionListener*/
{
    JCheckBox client, server;
    JTextField ip, port;

    ServerClient()
    {
        setTitle("Assembler compiler");
        setResizable(false);
        setSize(300,180);
        setVisible(true);
        setLayout(null);

        CheckBox();
        add(client);
        add(server);

        IpPort();
        add(ip);
        add(port);

    }

    public void CheckBox()
    {
        client = new JCheckBox("Client");
        client.setBounds(20,20,100,50);

        server = new JCheckBox("Server");
        server.setBounds(150,20,100,50);

    }

    public void IpPort()
    {
        ip =  new JTextField("Ip...");
        ip.setBounds(20,70,100,30);

        port = new JTextField("Port num.");
        port.setBounds(150,70,60,30);
    }




}
