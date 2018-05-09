import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DSUDPToRIO {


    private static int udpPort = 1110;
    private boolean shouldKill = false;
    private ToRIOPacket sending;
    protected byte[] toSend;
    private DatagramSocket sock;
    private InetAddress addr;
    private Thread thread;

    private Runnable run = () ->{
        try {
            System.out.println("thread started");
            sock = new DatagramSocket();
            addr = InetAddress.getByName("roborio-7303-frc.local");
            System.out.println("Rio found at: " + addr.getHostAddress());
            while (!shouldKill) {
                toSend = sending.getPacket();
                DatagramPacket toRio = new DatagramPacket(toSend, toSend.length, addr, udpPort);
                sock.send(toRio);

                Thread.sleep(18);
            }
        }
        catch (Exception e){
            System.out.println("system Error : could not find or connect to RIO");
            System.out.println(e.getMessage());
            e.printStackTrace();
            close();
        }
    };


    public DSUDPToRIO(){
        this(ToRIOPacket.AllianceNum.RED1);
    }

    public DSUDPToRIO(ToRIOPacket.AllianceNum alliance){
        System.out.println("Initializing to RIO UDP connection");
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
        return addr;
    }


}
