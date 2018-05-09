public class ToRIOPacket {


    enum ControlMode{
        AUTO(2, "Autonomous"),
        TEST(1, "Test"),
        TELEOP(0, "Teleoperated");

        private byte id;
        private String name;

        ControlMode(int idNo, String name){
            id = (byte)idNo;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    enum AllianceNum{
        RED1(0, "Red 1"),
        RED2(1, "Red 2"),
        RED3(2, "Red 3"),
        BLUE1(3, "Blue 1"),
        BLUE2(4, "Blue 2"),
        BLUE3(5, "Blue 3");

        private byte id;
        private String name;

        AllianceNum(int num, String name){
            id = (byte)num;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class ControlByte {

        private byte data;

        public ControlByte(boolean isEstop, boolean isFMS, boolean isEnabled, ControlMode mode){
            data |= (isEstop? 0b10000000: 0b0000000);
            data |= (isFMS? 0b00001000: 0b00000000);
            data |= (isEnabled? 0b00000100: 0b00000000);
            data |= mode.id;
        }

        public byte getData() {
            return data;
        }
    }

    public static class RequestByte {

        private byte data;

        public RequestByte(boolean restart, boolean reset){
            data |= (restart? 0b00001000: 0b00000000);
            data |= (reset? 0b00000100: 0b00000000);
        }

        public byte getData() {
            return data;
        }
    }

    private char counter = 0;
    private int countAtRequest = -1, numRequests = 10;
    private ControlByte control;
    private RequestByte request;
    private AllianceNum alliance;
    private static RequestByte defaultRequest;
    static{
        defaultRequest = new RequestByte(false, false);
    }

    public ToRIOPacket(AllianceNum alliance){
        this(false, false, false, ControlMode.TELEOP, false, false, alliance);
    }

    public ToRIOPacket(boolean isEstop, boolean isEnabled, ControlMode mode, AllianceNum alliance){
        this(isEstop,false, isEnabled, mode,false,false, alliance);
    }

    public ToRIOPacket(boolean isEstop, boolean isFMS, boolean isEnabled, ControlMode mode, boolean restart, boolean reset, AllianceNum alliance){
        control = new ControlByte(isEstop, isFMS, isEnabled, mode);
        request = new RequestByte(restart, reset);
        this.alliance = alliance;
    }

    public byte[] getPacket(){
        counter++;
        if(countAtRequest != -1 && (counter - countAtRequest) < numRequests){
            return new byte[]{getCounterMSB(), getCounterLSB(), 0x01, control.data, request.data, alliance.id};
        }
        else {
            countAtRequest = -1;
            return new byte[]{getCounterMSB(), getCounterLSB(), 0x01, control.data, defaultRequest.data, alliance.id};
        }
    }

    public void setControl(boolean isEstop, boolean isFMS, boolean isEnabled, ControlMode mode){
        control = new ControlByte(isEstop, isFMS, isEnabled, mode);
    }

    public void setRequest(boolean restart, boolean reset){
        if(reset || restart){
            countAtRequest = counter;
        }
        request = new RequestByte(restart, reset);
    }

    protected byte getCounterMSB(){
        return (byte) (counter >> 8);
    }

    protected byte getCounterLSB(){
        return (byte) (counter & 0xff);
    }
}
