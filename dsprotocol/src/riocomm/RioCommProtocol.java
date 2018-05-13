package riocomm;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RioCommProtocol {

    DSUDPToRIO toRIO;
    DSUDPFromRIO fromRIO;
    ToRIOPacket.ControlMode robotMode = ToRIOPacket.ControlMode.TELEOP;

    public RioCommProtocol(int teamNum){
        try {
            InetAddress local = InetAddress.getLocalHost();
            System.out.println("Localhost found at: " + local.getHostAddress());
            fromRIO = new DSUDPFromRIO();
            InetAddress rioAddress = InetAddress.getByName("roborio-" + teamNum + "-frc.local");
            //InetAddress rioAddress = InetAddress.getByName("10.41.45.2");
            System.out.println("Rio found at: " + rioAddress.getHostAddress());
            toRIO = new DSUDPToRIO(rioAddress);
            System.out.println("Beginning RIO comms connection");
        } catch (UnknownHostException e) {
            System.out.println("System Error: could not find RIO with hostname '" + e.getMessage() + "'");
            //e.printStackTrace();
        }
    }

    public FromRIOPacket getLastPacket() {
        if(fromRIO != null) {
            return fromRIO.getPacket();
        }
        return new FromRIOPacket(64);
    }

    public void startCommunication(){
        if(toRIO != null && fromRIO != null){
            toRIO.startComms();
            fromRIO.startComms();
        }
    }

    public void close(){
        if(toRIO != null && fromRIO != null){
            toRIO.close();
            fromRIO.close();
        }
    }

    public void setControlMode(ToRIOPacket.ControlMode mode){
        this.setControlMode(false, mode);
    }

    public void setControlMode(boolean isEnabled, ToRIOPacket.ControlMode mode) throws NullPointerException{
        ToRIOPacket packet = toRIO.getPacket();
        packet.setControl(false, false, isEnabled, mode);
        toRIO.setPacket(packet);
        System.out.println((robotMode == mode)? (isEnabled? "Enabling": "Disabling"): "Switching to " + mode.toString());
        robotMode = mode;
    }

    public void enable(boolean enable){
        setControlMode(enable, robotMode);
    }

    public void request(boolean shouldRestart, boolean shouldReset) throws NullPointerException{
        ToRIOPacket packet = toRIO.getPacket();
        packet.setRequest(shouldRestart, shouldReset);
        toRIO.setPacket(packet);
        if(shouldReset) System.out.println("Resetting RIO");
        if(shouldRestart) System.out.println("Restarting RIO");
    }

    public ToRIOPacket.ControlMode getRobotMode() {
        return robotMode;
    }
}

