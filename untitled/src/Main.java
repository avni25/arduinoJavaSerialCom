import java.io.IOException;
import com.fazecast.jSerialComm.*;

import java.io.Serial;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException{
        System.out.println("\nUsing Library Version v" + SerialPort.getVersion());
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("\nAvailable Ports:\n" +ports.length);
        for (int i = 0; i < ports.length; ++i){
            System.out.println("   [" + i + "] " + ports[i].getSystemPortName() + ": " + ports[i].getDescriptivePortName() + " - " + ports[i].getPortDescription());

        }

//        s.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
//        s.setComPortParameters(9600, 8, 1, 0);

        ports[0].openPort();
        ports[0].setComPortParameters(9600, 8, 1, 0);
        ports[0].setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
        if(ports[0].openPort()){
            System.out.println("port connected");

        }else{
            System.out.println("failed to open port");
            return;
        }
        Thread.sleep(2000);
        ports[0].getOutputStream().write(8);
        Thread.sleep(2000);
        ports[0].getOutputStream().write(9);
//        for (Integer i = 0; i < 100; ++i) {
//            ports[0].getOutputStream().write(i.byteValue());
//            ports[0].getOutputStream().flush();
//            System.out.println("Sent number: " + i);
//            Thread.sleep(1000);
//        }

        ports[0].closePort();

        if (ports[0].closePort()) {
            System.out.println("Port is closed :)");
        } else {
            System.out.println("Failed to close port :(");
            return;
        }



    }






}


