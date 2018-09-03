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
        }
        catch (UnknownHostException e) {
            System.out.println("Could not find Localhost");
        }
        try {
            toRIO = new DSUDPToRIO(getAddress(teamNum, false));
            System.out.println("Beginning RIO comms connection");
        }
        catch (UnknownHostException except){
            System.out.println("System Error: could not find RIO");
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

    private InetAddress getAddress(int teamNum, boolean fixedIP) throws UnknownHostException{
        InetAddress rioAddress = null;
        try {
            rioAddress = InetAddress.getByName("roborio-" + teamNum + "-frc.local");
            System.out.println("Rio found at: " + rioAddress.getHostAddress());
            return rioAddress;
        } catch (UnknownHostException e) {
            System.out.println("System Error: could not find RIO with hostname '" + e.getMessage() + "'");
            //e.printStackTrace();
            if(fixedIP) {
                try {
                    String num1 = ("." + teamNum).substring(0, 3) + ".";
                    String num2 = (teamNum + "").substring(2) + ".";
                    rioAddress = InetAddress.getByName("10" + num1 + num2 + "2");
                    return rioAddress;
                } catch (UnknownHostException except1) {
                    System.out.println("System Error: could not find RIO with hostname '" + e.getMessage() + "'");
                }
            }
        }
        throw new UnknownHostException();
        //return rioAddress;
    }
}

