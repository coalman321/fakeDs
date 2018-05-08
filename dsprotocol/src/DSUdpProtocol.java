public class DSUdpProtocol {

    enum ControlMode{
        AUTO(2),
        TEST(1),
        TELEOP(0);

        private int ID;

        ControlMode(int idNo){
            ID =idNo;
        }

        public int getID(){
            return ID;
        }

    }

    public class ControlPacket{

        private byte datapacket;

        public ControlPacket(boolean isEstop, boolean isFMS, boolean isEnabled, ControlMode mode){
            datapacket |= (isEstop? 0b10000000 : 0b0000000);
            datapacket |= (isFMS? 0b00001000: 0b00000000);
            datapacket |= (isEnabled? 0b00000100: 0b00000000);
        }

        public byte getControlPacket (){
            return datapacket;
        }
    }

    private char counter = 0;
    private static int udpPort = 1110;

}
