import java.awt.FlowLayout;
import java.awt.TextArea;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.*;

public class WindowUI extends JFrame implements ActionListener {

    JTextField t1, t2;
    JButton b,b1;

    WindowUI(){
        setTitle("Assembler compiler");

        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\aurim\\Pictures\\MIF_zenklas_png.png");

        setIconImage(icon);

        buttons();

        add(b1);
        add(b);
        setSize(600, 600);
        setLayout(null);
        setVisible(true);
        add(b1);

        TextField();
        add(t1);
        add(t2);

    }
    public void buttons(){
        b1 = new JButton("Load files");
        b1.addActionListener(this);
        b1.setBounds(20,10,100,30);

        b1.setVisible(true);
        b = new JButton();
        b.setText("Start");
        b.setBounds(50, 115, 70, 30);
        b.addActionListener(this);
    }


    public void TextField(){
        t1= new JTextField();
        t1.setBounds(50,50,150,60);
        t2 = new JTextField();
        t2.setBounds(50,200,150,60);
        t2.setEditable(false);
    }





    @Override
    public void actionPerformed(ActionEvent e) {
        String s1 = t1.getText();

        if(e.getSource()==b1){
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
