package riocomm2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RioCommProtocol {

    InetAddress mRioAddress;


    public RioCommProtocol(int teamnum, String fallbackAddress) throws UnknownHostException{
        mRioAddress = getAddress(teamnum, fallbackAddress);

    }

    private InetAddress getAddress(int teamNum, String fallback) throws UnknownHostException {
        InetAddress rioAddress = null;
        try {
            rioAddress = InetAddress.getByName("roborio-" + teamNum + "-frc.local");
            System.out.println("Rio found at: " + rioAddress.getHostAddress());
            return rioAddress;
        } catch (UnknownHostException e) {
            System.out.println("System Error: could not find RIO with hostname '" + e.getMessage() + "'");
            try {
                rioAddress = InetAddress.getByName(fallback);
                System.out.println("Rio found at: " + rioAddress.getHostAddress());
                return rioAddress;
            } catch (UnknownHostException except1) {
                System.out.println("System Error: could not find RIO with hostname '" + e.getMessage() + "'");
            }
        }
        throw new UnknownHostException();
    }

    private class IOHandler{

        final InetAddress mToRio = mRioAddress;
        static final int portNumber = 1110;
        boolean wantStop = false;
        DatagramSocket sock;
        Exception mLastException = null;
        Thread runner;
        SendingPacket sending = SendingPacket.defaultPacket;

        final Runnable run = () -> {
            try{
                sock = new DatagramSocket();
                byte[] toSend;
                while (!wantStop) {
                    toSend = sending.getPacket();
                    DatagramPacket toRio = new DatagramPacket(toSend, toSend.length, mToRio, portNumber);
                    sock.send(toRio);

                    Thread.sleep(18);
                }
            } catch (Exception e){
                mLastException = e;
            }
        };


        public void start(){
            runner = new Thread(run);
            runner.start();
        }

        public void close(){
            wantStop = true;
            if(sock != null) sock.close();
        }

        public void setPacket(SendingPacket packet){
            sending = packet;
        }

        public SendingPacket getPacket(){
            return sending;
        }

    }

}
