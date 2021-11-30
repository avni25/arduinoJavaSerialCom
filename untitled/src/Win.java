import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;


public class Win extends JFrame implements ActionListener{
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton connectButton;
    private JLabel label_status;
    private JLabel label_led1;
    private JLabel label_led2;
    private JLabel label_led3;
    private JLabel label_led4;
    private JLabel label_led5;
    private JPanel mypanel;
    private JLabel label_deg_val;
    private JButton button_deg;

    private SerialPort[] ports;
    private byte[] r = new byte[5];
    private Thread th = new Thread(() -> {
        for (int i = 0; i < 100; i++) {
            System.out.println("thread running!"+i);
            ports[0].readBytes(r,5);
            System.out.println(Main.convertBytesToString(r,5));
            label_deg_val.setText(Main.convertBytesToString(r,5));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException er) {
                er.printStackTrace();
            }
        }
    });

    public Win(){
        setLayout(new FlowLayout());
        setTitle("my Window");
        setLocationRelativeTo(this);
        setSize(450,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mypanel);
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
        connectButton.addActionListener(this);
        button_deg.addActionListener(this);
        setVisible(true);

        System.out.println("\nUsing Library Version v" + SerialPort.getVersion());
        ports = SerialPort.getCommPorts();
        System.out.println("\nAvailable Ports:\n" +ports.length);
        for (int i = 0; i < ports.length; ++i){
            System.out.println("   [" + i + "] " + ports[i].getSystemPortName() + ": " + ports[i].getDescriptivePortName() + " - " + ports[i].getPortDescription());

        }



    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button1){
            togglebutton(button1, ports[0], "on-1", "off-1", Color.GRAY, Color.yellow);
        }else if(e.getSource() == button2){
            togglebutton(button2, ports[0], "on-2", "off-2", Color.GRAY, Color.yellow);
        }else if(e.getSource() == button3){
            togglebutton(button3, ports[0], "on-3", "off-3", Color.GRAY, Color.yellow);
        }else if(e.getSource() == button4){
            togglebutton(button4, ports[0], "on-4", "off-4", Color.GRAY, Color.yellow);
        }else if(e.getSource() == button5){
            togglebutton(button5, ports[0], "on-5", "off-5", Color.GRAY, Color.yellow);
        }else if(e.getSource() == connectButton){
            if(connectButton.getText().equals("Connect")){
                Main.openPort(ports[0], 9600, 8, 1,0);
                connectButton.setBackground(Color.green);
                connectButton.setText("Disconnect");
                label_status.setText("Connected!");
            }else{
                Main.closePort(ports[0]);
                connectButton.setBackground(Color.red);
                connectButton.setText("Connect");
                label_status.setText("Disconnected!");
            }

        }else if(e.getSource() == button_deg){
            System.out.println("read button");
            this.th.start();
            System.out.println("read 2");
        }
    }

    public static void togglebutton(JButton b, SerialPort p, String toggle1_message, String toggle2_message, Color toggle1_color,Color toggle2_color){
        if(b.getText().equals("ON")){
            //sen data
            Main.sendString(p,toggle2_message);
            b.setText("OFF");
            b.setBackground(toggle1_color);
        }else{
            Main.sendString(p,toggle1_message);
            b.setText("ON");
            b.setBackground(toggle2_color);
        }
    }

    public JLabel getLabel_deg_val() {
        return label_deg_val;
    }

    public void setLabel_deg_val(JLabel label_deg_val) {
        this.label_deg_val = label_deg_val;
    }
}
