import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DSUdpProtocol {

    enum ControlMode{
        AUTO(2),
        TEST(1),
        TELEOP(0);

        private int id;

        ControlMode(int idNo){
            id = idNo;
        }

    }

    enum AllianceNum{
        RED1(0),
        RED2(1),
        RED3(2),
        BLUE1(3),
        BLUE2(4),
        BLUE3(5);

        private int id;

        AllianceNum(int num){
            id = num;
        }
    }

    public class ControlPacket{

        private byte datapacket;

        public ControlPacket(boolean isEstop, boolean isFMS, boolean isEnabled, ControlMode mode){
            datapacket |= (isEstop? 0b10000000: 0b0000000);
            datapacket |= (isFMS? 0b00001000: 0b00000000);
            datapacket |= (isEnabled? 0b00000100: 0b00000000);
            datapacket |= (byte)(mode.id);
        }
    }


    private static int udpPort = 1110;
    private char counter = 0;
    private boolean shouldKill = false;
    private ControlPacket current = new ControlPacket(false, false, false, ControlMode.TELEOP);
    private AllianceNum alliance;
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
                toSend = new byte[]{getCounterMSB(), getCounterLSB(), 0x01, current.datapacket, 0x00,(byte)alliance.id };
                DatagramPacket toRio = new DatagramPacket(toSend, toSend.length, addr, udpPort);
                sock.send(toRio);
                counter++;
                Thread.sleep(18);
            }
        }
        catch (Exception e){
            System.out.println("system Error : could not find or connect to RIO");
            System.out.println("Check error stream for more details");
            e.printStackTrace();
            close();
        }
    };


    public DSUdpProtocol(AllianceNum alliance){
        this.alliance = alliance;
        thread = new Thread(run);
        thread.setDaemon(true);
    }

    public void startComms(){
        System.out.println("starting thread");
        thread.start();
    }

    public void setCurrentDataPacket(ControlPacket packet){
        current = packet;
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

    protected byte getCounterMSB(){
        return (byte) (counter >> 8);
    }

    protected byte getCounterLSB(){
        return (byte) (counter & 0xff);
    }

}
