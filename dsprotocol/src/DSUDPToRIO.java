import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DSUDPToRIO {


    private static int udpPort = 1110;
    private boolean shouldKill = false;
    private ToRIOPacket sending;
    protected byte[] toSend;
    private DatagramSocket sock;
    private InetAddress address;
    private Thread thread;

    private Runnable run = () ->{
        try {
            System.out.println("thread started");
            sock = new DatagramSocket();
            while (!shouldKill) {
                toSend = sending.getPacket();
                DatagramPacket toRio = new DatagramPacket(toSend, toSend.length, address, udpPort);
                sock.send(toRio);

                Thread.sleep(18);
            }
        }
        catch (Exception e){
            System.out.println("system Error : could not connect to RIO");
            System.out.println(e);
            e.printStackTrace();
            close();
        }
    };


    public DSUDPToRIO(InetAddress address){
        this(ToRIOPacket.AllianceNum.RED1, address);
    }

    public DSUDPToRIO(ToRIOPacket.AllianceNum alliance, InetAddress address){
        System.out.println("Initializing to RIO UDP connection");
        this.address = address;
        sending = new ToRIOPacket(false, false, ToRIOPacket.ControlMode.TELEOP, alliance);
        thread = new Thread(run);
        thread.setDaemon(true);
    }

    public void setPacket(ToRIOPacket packet){
        sending = packet;
    }

    public ToRIOPacket getPacket(){
        return sending;
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

    public InetAddress getAddress(){
        return address;
    }


}
