import sun.dc.pr.PRError;

public class RioCommProtocol {

    public enum RobotState{
        AUTOENABLED(true, ToRIOPacket.ControlMode.AUTO),
        AUTODISABLED(false, ToRIOPacket.ControlMode.AUTO),
        TESTENABLED(true, ToRIOPacket.ControlMode.TEST),
        TESTDISABLED(false, ToRIOPacket.ControlMode.TEST),
        TELEOPENABLED(true, ToRIOPacket.ControlMode.TELEOP),
        TELEOPDISABLED(false, ToRIOPacket.ControlMode.TELEOP);

        private boolean isEnabled;
        private ToRIOPacket.ControlMode mode;

        RobotState(boolean isEnabled, ToRIOPacket.ControlMode mode){
            this.isEnabled = isEnabled;
            this.mode = mode;
        }

    }

    DSUDPToRIO toRIO;
    DSUDPFromRIO fromRIO;

    public RioCommProtocol(){
        System.out.println("beginning RIO comms connection");
        toRIO = new DSUDPToRIO();
        fromRIO = new DSUDPFromRIO();
    }

    public void startCommunication(){
        toRIO.startComms();
    }

    public void setControlMode(RobotState state){
        ToRIOPacket packet = toRIO.getPacket();
        packet.setControl(false, false, state.isEnabled, state.mode);
        toRIO.setPacket(packet);
    }

    public void request(boolean shouldRestart, boolean shouldReset){
        ToRIOPacket packet = toRIO.getPacket();
        packet.setRequest(shouldRestart, shouldReset);
        toRIO.setPacket(packet);
    }

}
