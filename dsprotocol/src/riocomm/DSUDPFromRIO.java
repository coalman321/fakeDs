package riocomm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DSUDPFromRIO {

    private static int udpPort = 1150;
    private boolean shouldKill = false;
    private DatagramSocket sock;
    private Thread thread;
    private byte[] toSend;
    private FromRIOPacket packet;


    private Runnable run = () ->{
        try {
            System.out.println("thread started");
            sock = new DatagramSocket(udpPort);
            System.out.println(shouldKill);
            while (!shouldKill) {
                DatagramPacket fromRio = new DatagramPacket(toSend, toSend.length);
                sock.receive(fromRio);

                packet.setData(fromRio.getData());

                Thread.sleep(18);
            }
        }
        catch (SocketException e){
            System.out.println("system Error : could not connect open port");
            System.out.println(e);
            e.printStackTrace();
            close();
        }
        catch (IOException e){
            System.out.println("system Error : could not receive data");
            System.out.println(e);
            e.printStackTrace();
            close();
        }
        catch (InterruptedException e){
            //ignore means nothing
        }
    };

    public DSUDPFromRIO(){
        System.out.println("Initializing from RIO UDP connection");
        toSend = new byte[64];
        packet = new FromRIOPacket(64);
        thread = new Thread(run);
        thread.setDaemon(true);
    }

    public FromRIOPacket getPacket(){
        return packet;
    }

    public void startComms(){
        System.out.println("starting thread");
        thread.start();
    }

    public void close(){
        System.out.println("Closing connection");
        shouldKill = true;
        if(sock != null) {
            sock.close();
        }
    }

}
