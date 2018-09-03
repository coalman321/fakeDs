package riocomm;

public class FromRIOPacket {

    byte[] stored;
    int lastCount = 0, currentCount, dropped;

    public FromRIOPacket(byte[] recived){
        setData(recived);
    }

    public FromRIOPacket(int len){
        setData(new byte[len]);
    }

    public void setData(byte[] data){
        stored = data;
        currentCount = bytesToInt2(stored[1], stored[0]);
        if(lastCount < currentCount - 1)
            dropped++;
        lastCount = currentCount;
    }

    public int getCount(){
        return currentCount;
    }

    public int getDropped(){
        return dropped;
    }

    public double getVoltage(){
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
