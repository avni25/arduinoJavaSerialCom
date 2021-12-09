import com.fazecast.jSerialComm.SerialPort;
import org.jfree.ui.RefineryUtilities;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


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
    private JButton heatChartButton;

    private SerialPort[] ports;
    private byte[] r = new byte[5];  // porttan gelen/giden byte degerlerini tutmak icin olusuturlmustur
    private boolean execute = true;  // thread i baslatmak ve sonlandirmak icin olusturulmustur
    public static ArrayList<Double> heatValues = new ArrayList<>();

    /**
     * arduinodan gelen sicaklik bilgisi icin olusturukmustur.
     * buton ile thread baslar
     * yine buton ile sonlanir.
     * her 1 saniyede arduinodan gelen veri label a yazdirilir
     * */
    private Thread readHeat = new Thread(() -> {
//        System.out.println("thread opened");
        String h="";
        Double d=0.0;
        while(execute) {
            System.out.println("reading heat!");

            try{
                ports[0].readBytes(r,5);
                h = Main.convertBytesToString(r,5);
                System.out.println(h);
                label_deg_val.setText(h);
                d = Double.parseDouble(h);
            }catch (Exception er){
                System.out.println("cant read data.");
                d=null;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException er) {
                er.printStackTrace();
            }
            heatValues.add(d);

        }
    });

    /**
     * constructor
     * pencereyi hazırlar
     * actionlistenerlar barindirir
     * ve aktif portlari alir ve console da yazdidirir
     * */
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
        heatChartButton.addActionListener(this);
        setVisible(true);

        System.out.println("\nUsing Library Version v" + SerialPort.getVersion());
        ports = SerialPort.getCommPorts();
        System.out.println("\nAvailable Ports:\n" +ports.length);
        for (int i = 0; i < ports.length; ++i){
            System.out.println("   [" + i + "] " + ports[i].getSystemPortName() + ": " + ports[i].getDescriptivePortName() + " - " + ports[i].getPortDescription());

        }

    }

    /**
     * tuslarin komutlarini icerir
     * led tuslarina basildiginda togglebutton methodu ile arduinoya ilgili
     * string degerlerini gonderir. bu degerlere gore arduino da ledleri yakar veya sondurur
     * connec butonu ile port acilir ve porta baglanir.
     * Butonların hepsi toggle buton seklindedir
     *
     * */
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
                if(button_deg.getText().equals("getDegree")){
                    this.execute = true;
                    try{
                        this.readHeat.start();
                    }catch(Exception er){
                        System.out.println("error: "+er);
                    }
                    button_deg.setText("Stop");
                }else{
                    this.execute = false;
                    button_deg.setText("getDegree");
                }
        }else if(e.getSource() == heatChartButton){
            Chart c = new Chart("title", "chart title", heatValues);
            c.pack();
            RefineryUtilities.centerFrameOnScreen( c );
            c.setVisible( true );
        }

    }

    /**
     * 5 led butonu aynni isiyaptıgindan hepsi icin ayni kodlari yazmak yterine
     * ortak bir method olusturularak kod sadelestirmek icin olusturulmustur.
     * buton text on ise ilgili string degerini gonderir ve text i off yapar.
     * buton string off ise ilgili degiskeni gonderir ve text i on yapar.
     * bu sekilde bir tus iki amacli kullanilabilir
     * */
    public static void togglebutton(JButton b, SerialPort p, String toggle1_message, String toggle2_message, Color toggle1_color,Color toggle2_color){
        if(b.getText().equals("ON")){
            Main.sendString(p,toggle2_message);
            b.setText("OFF");
            b.setBackground(toggle1_color);
        }else{
            Main.sendString(p,toggle1_message);
            b.setText("ON");
            b.setBackground(toggle2_color);
        }
    }




}
