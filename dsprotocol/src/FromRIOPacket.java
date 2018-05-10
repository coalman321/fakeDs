public class FromRIOPacket {

    byte[] stored;

    public FromRIOPacket(byte[] recived){
        stored = recived;
    }

    public FromRIOPacket(int len){
        stored = new byte[len];
    }

    int getCount(){
        return bytesToInt2(stored[1], stored[0]);
    }

    double getVoltage(){
        return (bytesToInt2(stored[6], stored[5]) / 256.0);
    }

    @Override
    public String toString() {
        return "packet count: " + getCount() + "    Voltage: " + getVoltage() ;
    }

    public static int bytesToInt2 (byte b1, byte b2)      // unsigned
    {
        return (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }
}
