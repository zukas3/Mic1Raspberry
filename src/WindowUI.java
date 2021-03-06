//import java.awt.FlowLayout;
//import java.awt.TextArea;
import javax.swing.*;
import javax.swing.border.Border;
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
    JButton b1;
    JScrollPane sInput, sOutput;

    WindowUI()
    {
        setTitle("MIC-1 Control Window");

        //Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\aurim\\Pictures\\MIF_zenklas_png.png");
        //setIconImage(icon);

        buttons();
        TextField();
        TextArea();

        add(b1);

        add(files);
        add(statusBar);
        files.setVisible(true);
        getContentPane().add(sInput);
        getContentPane().add(sOutput);
        
        getContentPane().setLayout(null);
        //getContentPane().setBackground(new Color(119,136,153));
        setResizable(false);
        setSize(400, 330);
        setVisible(true);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        SetBorders();
        main = this;
    }

    public void run()
    {

    }

    public void SetBorders()
    {
        Border border = BorderFactory.createLineBorder(Color.BLACK);

        t1.setBorder(border);
        t2.setBorder(border);
        files.setBorder(border);
        //statusBar.setBorder(border);
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
        t1.setEditable(true);
        sInput = new JScrollPane(t1);
        sInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sInput.setBounds(30,160,155,80);

        t1.setLineWrap(true);
        t1.addKeyListener( new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                if (e.getKeyCode() <= 32 || e.getKeyCode() ==127)
                {

                }
                else {
                    try {
                        ClientManager.main.SendCharInput(e.getKeyChar());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        t2 = new JTextArea();
        sOutput = new JScrollPane(t2);
        sOutput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sOutput.setBounds(200,160,155,80);
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

    public void Reset()
    {
        t1.setText("");
        t2.setText("");
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
                    files.setText(fc.getSelectedFile().getName());
                    try
                    {
                        ClientManager.main.SendFile(s);
                        Reset();
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
