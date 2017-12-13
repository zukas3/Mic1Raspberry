import javax.swing.*;
import java.awt.event.ActionListener;

public class ServerClient extends JFrame /*implements ActionListener*/
{
    JCheckBox client, server;
    JTextField ip, port;

    ServerClient()
    {
        setTitle("Connecting window");
        setResizable(false);
        setSize(350,180);
        setVisible(true);
        setLayout(null);

        checkBox();
        add(client);
        add(server);

        IpPort();;
        add(ip);
        add(port);


    }

    public void checkBox()
    {
        client = new JCheckBox("Client");
        client.setBounds(30,20,70,30);

        server = new JCheckBox("Server");
        server.setBounds(140, 20,70,30);
        client.addActionListener(this);
    }

    public void IpPort()
    {
        ip = new JTextField("Ip..");
        ip.setBounds(30, 80, 90, 30);

        port = new JTextField("Port num.");
        port.setBounds(140,80,60,30);

    }


}
