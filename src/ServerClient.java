import javafx.scene.control.ToggleGroup;
import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerClient extends JFrame implements ActionListener
{
    JRadioButton client, server;
    JTextField ip, port;
    JLabel label;
    JButton co;

    ServerClient()
    {
        setTitle("Connecting window");

        Label();
        add(label);

        checkBox();
        add(client);
        add(server);

        IpPort();

        add(ip);
        add(port);

        Butt();
        add(co);

        getContentPane().setLayout(null);

        setResizable(false);
        setSize(350,180);
        setVisible(true);

        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\aurim\\Pictures\\MIF_zenklas_png.png");

        setIconImage(icon);



    }

    public void Label(){
        label = new JLabel("Choose one: ");
        label.setBounds(10,10,100,20);

    }

    public void checkBox()
    {
        ButtonGroup bg = new ButtonGroup();
        client = new JRadioButton("Client");
        client.setBounds(30,30,70,30);

        server = new JRadioButton("Server");
        server.setBounds(140, 30,70,30);

        bg.add(client);
        bg.add(server);

        client.addActionListener(this);
        server.addActionListener(this);
    }

    public void IpPort()
    {
        ToggleGroup group = new ToggleGroup();
        ip = new JTextField("Ip..");
        ip.setBounds(30, 80, 90, 30);
        //ip.setToggleGroup(group);

        port = new JTextField("Port num.");
        port.setBounds(140,80, 60, 30);

    }

    public void Butt()
    {
        co  = new JButton("Connect");
        co.setBounds(220,80,90,30);
        co.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {

            if (server.isSelected() )
            {
                ip.setEditable(false);
            }
            if (client.isSelected()) {
                ip.setEditable(true);
            }



    }

}
