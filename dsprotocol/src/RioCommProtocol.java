
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RioCommProtocol {

    DSUDPToRIO toRIO;
    DSUDPFromRIO fromRIO;
    ToRIOPacket.ControlMode robotMode = ToRIOPacket.ControlMode.TELEOP;

    public RioCommProtocol(){
        try {
            InetAddress address = InetAddress.getByName("roborio-7303-frc.local");
            System.out.println("Rio found at: " + address.getHostAddress());
            System.out.println("Beginning RIO comms connection");
            toRIO = new DSUDPToRIO(address);
            fromRIO = new DSUDPFromRIO(address);
        } catch (UnknownHostException e) {
            System.out.println("System Error: could not find RIO with hostname '" + e.getMessage() + "'");
            //e.printStackTrace();
        }
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

    public void setControlMode(boolean isEnabled, ToRIOPacket.ControlMode mode){
        if(toRIO != null) {
            System.out.println((robotMode == mode)? (isEnabled? "Enabling": "Disabling"): "Switching to " + mode.toString());
            robotMode = mode;
            ToRIOPacket packet = toRIO.getPacket();
            packet.setControl(false, false, isEnabled, mode);
            toRIO.setPacket(packet);
        }
        else {
            System.out.println("Cannot set mode");
        }
    }

    public void enable(boolean enable){
        setControlMode(enable, robotMode);
    }

    public void request(boolean shouldRestart, boolean shouldReset){
        if(toRIO != null) {
            if(shouldReset) System.out.println("Resetting RIO");
            if(shouldRestart) System.out.println("Restarting RIO");
            ToRIOPacket packet = toRIO.getPacket();
            packet.setRequest(shouldRestart, shouldReset);
            toRIO.setPacket(packet);
        }
        else{
            System.out.println("Cannot perform request");
        }
    }

}
