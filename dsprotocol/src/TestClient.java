import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TestClient {

    private static int udpPort = 1110;
    private static char counter = 0;

    public static void main(String[] args) throws Exception{
        DatagramSocket sock = new DatagramSocket();
        InetAddress addr = InetAddress.getByName("roborio-7303-frc.local");
        System.out.println("Rio found at: " + addr.getHostAddress());
        byte[] toSend;
        byte[] toRecv = new byte[1024];
        System.out.println("beginning comms!");
        while (true) {
            //System.out.println("sending packet id " + (int)counter);
            toSend = new byte[] {(byte)(counter >> 8), (byte)(counter & 0xff), 0x01, 0b00000100, 0x00, 0x02};
            DatagramPacket toRio = new DatagramPacket(toSend, toSend.length, addr, udpPort);
            sock.send(toRio);
            counter++;
            Thread.sleep(18);
        }
        //sock.close();

    }

}
