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

    JTextField t1, t2, con, files;
    JButton b,b1, conn;

    WindowUI()
    {
        setTitle("Assembler compiler");

        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\aurim\\Pictures\\MIF_zenklas_png.png");

        setIconImage(icon);

        setResizable(false);
        setSize(400, 350);
        setVisible(true);
        setLayout(null);

        buttons();
        TextField();

        add(b1);
        add(conn);
        add(con);
        add(t2);
        add(t1);
        add(files);
        files.setVisible(true);






    }
    public void buttons()
    {
        b1 = new JButton("Load files");
        b1.addActionListener(this);
        b1.setBounds(240,60,100,30);
        b1.setVisible(true);

        /*b = new JButton();
        b.setText("Start");
        b.setBounds(50, 115, 70, 30);
        b.addActionListener(this);*/

        conn = new JButton("Connect");
        conn.setBounds(240,20,100,30);
        conn.setVisible(true);
    }




    public void TextField()
    {
        con = new JTextField("Connect to...");
        con.setBounds(10,20,200,30);

        files = new JTextField();
        files.setBounds(12,100,300,60);
        files.setEditable(false);

        t1= new JTextField();
        t1.setBounds(30,200,150,60);
        t2 = new JTextField();
        t2.setBounds(200,200,150,60);
        t2.setEditable(false);
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
                    System.out.printf("Ok");
                }
                else
                {
                    System.out.printf("not");
                }
            }
        }
    }


}
