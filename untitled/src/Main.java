import java.io.IOException;
import com.fazecast.jSerialComm.*;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException{
        System.out.println("\nUsing Library Version v" + SerialPort.getVersion());
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("\nAvailable Ports:\n" +ports.length);
        for (int i = 0; i < ports.length; ++i){
            System.out.println("   [" + i + "] " + ports[i].getSystemPortName() + ": " + ports[i].getDescriptivePortName() + " - " + ports[i].getPortDescription());

        }


        openPort(ports[0], 9600, 8, 1,0);
        byte[] r = new byte[5];

        Thread th = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("thread running!"+i);
                ports[0].readBytes(r,5);
                System.out.println(convertBytesToString(r,5));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        th.start();

        Thread.sleep(2000);
//        ports[0].getOutputStream().write(8);
        sendString(ports[0], "on");
        Thread.sleep(2000);
        sendString(ports[0], "off");

//        closePort(ports[0]);


//        ports[0].getOutputStream().write(9);
//        for (Integer i = 0; i < 100; ++i) {
//            ports[0].getOutputStream().write(i.byteValue());
//            ports[0].getOutputStream().flush();
//            System.out.println("Sent number: " + i);
//            Thread.sleep(1000);
//        }



    }

    public static void openPort(SerialPort p, int newBaudRate, int newDataBits, int newStopBits, int newParity){
        p.openPort();
        p.setComPortParameters(newBaudRate, newDataBits, newStopBits, newParity);
        p.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
        if(p.openPort()){
            System.out.println("port connected");
        }else{
            System.out.println("failed to open port");
            return;
        }
    }

    public static void sendString(SerialPort p, String text){
        p.writeBytes(text.getBytes(StandardCharsets.UTF_8), text.length());
    }

    public static void sendByte(SerialPort p, byte b){
        p.writeBytes(new byte[]{b}, 1);
    }

    public static void closePort(SerialPort p){
        p.closePort();

        if (p.closePort()) {
            System.out.println("Port is closed.");
        } else {
            System.out.println("Failed to close port.");
            return;
        }
    }

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


