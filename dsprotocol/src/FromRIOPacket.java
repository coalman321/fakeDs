public class FromRIOPacket {

    byte[] stored;

    public FromRIOPacket(byte[] recived){
        stored = recived;
    }

    public FromRIOPacket(int len){
        stored = new byte[len];
    }

    int getCount(){
        return ((char)(stored[0] << 8 | stored[1]));
    }

    double getVoltage(){
        return (((char)(stored[5] << 8 | stored[6])) / 256.0);
    }

    @Override
    public String toString() {
        return "packet count: " + getCount() + "   Raw bytes: " + Byte.toString(stored[0]) + Byte.toString(stored[1])
                + "    Voltage: " + getVoltage() + "   Raw bytes: " + Byte.toString(stored[5]) + Byte.toString(stored[6]);
    }
}
