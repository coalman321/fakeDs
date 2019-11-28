package riocomm2;

import java.util.List;

class SendingPacket{
    private boolean wantReset, wantRestart, wantFMS, wantEstop, wantEnabled;
    private ControlMode mode;
    private AllianceStation station;
    private char counter = 0;
    private List<Tag> tags;

    public static final SendingPacket defaultPacket = new SendingPacket(
            false,false,false,false, false,
            ControlMode.TELEOP, AllianceStation.RED1);

    public SendingPacket(boolean reset, boolean restart, boolean fms,
                         boolean estop, boolean enabled, ControlMode mode,
                         AllianceStation station){
        wantReset = reset; wantRestart = restart; wantFMS = fms;
        wantEstop = estop; wantEnabled = enabled; this.mode = mode;
        this.station = station;
    }

    public void setPacket(boolean reset, boolean restart, boolean fms,
                          boolean estop, boolean enabled, ControlMode mode,
                          AllianceStation station){
        wantReset = reset; wantRestart = restart; wantFMS = fms;
        wantEstop = estop; wantEnabled = enabled; this.mode = mode;
        this.station = station;
    }

    public byte[] getPacket() {
        counter++;

        byte control = (byte)(wantEstop ? 0b10000000 : 0b0000000);
                  control |= (wantFMS ? 0b00001000 : 0b00000000);
                  control |= (wantEnabled ? 0b00000100 : 0b00000000);
                   control |= mode.id;

        byte request = (byte)(wantRestart ? 0b00001000 : 0b00000000);
                  request |= (wantReset ? 0b00000100 : 0b00000000);

        return new byte[] {(byte) (counter >> 8), (byte) (counter & 0xff), 0x01, control, request, station.id};
    }

    public boolean isWantReset(){
        return wantReset;
    }

    public void setWantReset(boolean wantReset) {
        this.wantReset = wantReset;
    }

    public boolean isWantRestart() {
        return wantRestart;
    }

    public void setWantRestart(boolean wantRestart) {
        this.wantRestart = wantRestart;
    }

    public boolean isWantFMS() {
        return wantFMS;
    }

    public void setWantFMS(boolean wantFMS) {
        this.wantFMS = wantFMS;
    }

    public boolean isWantEstop() {
        return wantEstop;
    }

    public void setWantEstop(boolean wantEstop) {
        this.wantEstop = wantEstop;
    }

    public boolean isWantEnabled() {
        return wantEnabled;
    }

    public void setWantEnabled(boolean wantEnabled) {
        this.wantEnabled = wantEnabled;
    }

    public ControlMode getMode() {
        return mode;
    }

    public void setMode(ControlMode mode) {
        this.mode = mode;
    }

    public AllianceStation getStation() {
        return station;
    }

    public void setStation(AllianceStation station) {
        this.station = station;
    }

    public enum ControlMode{
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

    public enum AllianceStation{
        RED1(0, "Red 1"),
        RED2(1, "Red 2"),
        RED3(2, "Red 3"),
        BLUE1(3, "Blue 1"),
        BLUE2(4, "Blue 2"),
        BLUE3(5, "Blue 3");

        private byte id;
        private String name;

        AllianceStation(int num, String name){
            id = (byte)num;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public class Tag{

        private byte size;
        private ID id;
        byte[] payload;

        public Tag(ID id, byte[] payload){

        }

    }

    public enum ID {

        COUNTDOWN(0x07),
        JOYSTICK(0x0c),
        DATE(0x0f),
        TIMEZONE(0x01);

        private byte internal;

        ID(int id){
            internal = (byte) id;
        }

        public byte getInternal() {
            return internal;
        }
    }
}
