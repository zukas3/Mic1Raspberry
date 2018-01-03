import java.awt.FlowLayout;
import java.awt.TextArea;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.*;

public class WindowUI extends JFrame implements ActionListener
{

    JTextField t1, t2, con;
    JTextArea files;
    JButton b,b1, conn;

    WindowUI()
    {
        setTitle("Assembler compiler");

        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\aurim\\Pictures\\MIF_zenklas_png.png");

        setIconImage(icon);

        buttons();
        TextField();
        TextArea();

        add(b1);


        add(t2);
        add(t1);
        add(files);
        files.setVisible(true);

        getContentPane().setLayout(null);
        setResizable(false);
        setSize(400, 330);
        setVisible(true);
        setLayout(null);





    }
    public void buttons()
    {
        b1 = new JButton("Load files");
        b1.addActionListener(this);
        b1.setBounds(30,20,100,30);
        b1.setVisible(true);

        /*b = new JButton();
        b.setText("Start");
        b.setBounds(50, 115, 70, 30);
        b.addActionListener(this);*/


    }




    public void TextField()
    {
        t1= new JTextField();
        t1.setBounds(30,180,150,60);
        t2 = new JTextField();

        t2.setBounds(200,180,150,60);
        t2.setEditable(false);
    }

    public void TextArea()
    {
        files = new JTextArea();
        files.setBounds(30,80,320,70);
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
                if(s.endsWith(".jas"))
                {
                    //FilesToBytes.ToBytes(s);
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "You must select '.jas' file.");
                }
            }
        }
    }
}
