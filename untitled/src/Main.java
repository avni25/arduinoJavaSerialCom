import java.io.IOException;
import com.fazecast.jSerialComm.*;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException{

        Win win = new Win();




    }
    /**
     * portun baslatma islemleri icin kullanlıan method
     *
     * */
    public static int openPort(SerialPort p, int newBaudRate, int newDataBits, int newStopBits, int newParity){
        try{
            p.openPort();       // portu acar
            p.setComPortParameters(newBaudRate, newDataBits, newStopBits, newParity); // port ayarlarini yaapr
            p.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
            if(p.openPort()){   //port aciksa ekrana bilgi yazar
                System.out.println("port connected");
                return 1;       // ve bir degerini donduurur
            }else{
                System.out.println("failed to open port");
                return -1;
            }
        }catch(Exception e){
            System.out.println("error: "+e.getMessage());
            return -1;
        }

    }
    /**
     * porta string deger gonderir.
     * method aldigi string degeri byte seklinde porta gonderir
     * */
    public static void sendString(SerialPort p, String text){
        p.writeBytes(text.getBytes(StandardCharsets.UTF_8), text.length());
        try {
            p.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * sadece bir byte porta gonderir
     * */
    public static void sendByte(SerialPort p, byte b){
        p.writeBytes(new byte[]{b}, 1);
    }

    /**
     * portu kapatır
     * */
    public static void closePort(SerialPort p){
        p.closePort();

        if (p.closePort()) {
            System.out.println("Port is closed.");
        } else {
            System.out.println("Failed to close port.");
            return;
        }
    }
    /**
     * porta gelen veriler byte olarak geldigi icin daha okunur olmasi icin ve
     * string ifade de gonderebilmek icin olusturuklmustur
     * byte array i stringe cevirir
     * */
    public static String convertBytesToString(byte[] b, int bytesToConvert){

        char[] ch = new char[bytesToConvert];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = '0';
        }
        for (int i = 0; i < b.length; i++) {
            ch[i] = (char)b[i];
        }

        return String.valueOf(ch);
    }


}


