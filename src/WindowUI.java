//import java.awt.FlowLayout;
//import java.awt.TextArea;
import javax.swing.*;
import java.awt.event.*;
//import java.awt.event.ActionEvent;
import java.io.*;
//import java.awt.event.ActionListener;
import java.awt.*;

public class WindowUI extends JFrame implements ActionListener, Runnable
{
    public static WindowUI main;

    JTextArea t1, t2;
    JTextArea files, statusBar;
    JButton b,b1, conn;

    WindowUI()
    {
        setTitle("MIC-1 Control Window");

        //Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\aurim\\Pictures\\MIF_zenklas_png.png");
        //setIconImage(icon);

        buttons();
        TextField();
        TextArea();

        add(b1);

        add(t2);
        add(t1);
        add(files);
        add(statusBar);
        files.setVisible(true);

        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(119,136,153));
        setResizable(false);
        setSize(400, 330);
        setVisible(true);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        main = this;
    }

    public void run()
    {

    }

    public void buttons()
    {
        b1 = new JButton("Load files");
        b1.addActionListener(this);
        b1.setBounds(30,20,100,30);
        b1.setVisible(true);
    }




    public void TextField()
    {
        t1= new JTextArea();
        t1.setBounds(30,180,150,60);
        t1.setLineWrap(true);
        t1.addKeyListener( new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                try {
                    ClientManager.main.SendCharInput(e.getKeyChar());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        t2 = new JTextArea();
        t2.setBounds(200,180,150,60);
        t2.setEditable(false);
        t2.setLineWrap(true);
    }

    public void TextArea()
    {
        files = new JTextArea();
        files.setBounds(150,20,150,30);
        files.setEditable(false);

        statusBar = new JTextArea();
        statusBar.setBounds(30,80,300,40);
        statusBar.setEditable(false);
    }

    public void SetStatusText(String s)
    {
        statusBar.setText(s);
    }

    public void PutCharOutput(char c)
    {
        t2.setText(t2.getText() + c);
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        String s1 = t1.getText();

        if(e.getSource()==b1)
        {
            JFileChooser fc = new JFileChooser();
            int i = fc.showOpenDialog(this);
            if( i == JFileChooser.APPROVE_OPTION){
                String s = fc.getSelectedFile().getAbsolutePath();
                if(s.endsWith(".jas") || s.endsWith(".ijvm"))
                {
                    files.setText(s);
                    try {
                        ClientManager.main.SendFile(s);
                    } catch (IOException ee) { ee.printStackTrace(); }
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "You must select '.jas' or '.ijvm' file.");
                }
            }
        }
    }
}
