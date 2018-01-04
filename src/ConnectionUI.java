import javafx.scene.control.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;

public class ConnectionUI extends JFrame implements ActionListener
{
    JRadioButton client, server;
    JTextField ip, port;
    JLabel label;
    JButton co;

    ConnectionUI()
    {
        setTitle("Connection Window");

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

        //Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\aurim\\Pictures\\MIF_zenklas_png.png");
        //setIconImage(icon);
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
        client.setSelected(true);

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
        ip = new JTextField("IP Address");
        ip.setBounds(30, 80, 90, 30);
        ip.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                ip.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ip.getText().equals(""))
                {
                    ip.setText("IP Address");
                }

            }
        });

        port = new JTextField("Port");
        port.setBounds(140,80, 60, 30);
        port.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (port.getText().equals(""))
                {
                    port.setText("Port");
                }
            }
            @Override
            public void focusGained(FocusEvent e) {
                port.setText("");
            }
    });
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

                if (e.getSource() == co)
                {
                    try {
                        int p = Integer.parseInt(port.getText());
                        this.dispose();
                        Main.InitializeServer(p);
                    }
                    catch (NumberFormatException e1)
                    {
                        JOptionPane.showMessageDialog(this, "Port is not valid");
                    }
                }
            }
            if (client.isSelected())
            {
                ip.setEditable(true);

                //Pressed on connect button
                if (e.getSource() == co)
                {
                    String s = ip.getText();
                    if(Utility.validIP(s))
                    {
                        try {
                            int p = Integer.parseInt(port.getText());
                            this.dispose();
                            Main.InitializeClient(s,p);
                        }
                        catch (NumberFormatException e1)
                        {
                            JOptionPane.showMessageDialog(this, "Port is not valid");
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this, "IP is not valid");
                    }
                }
            }

    }

}
