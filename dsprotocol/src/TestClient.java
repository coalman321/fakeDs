import com.org.ConsoleLibs.ConOutMain;

public class TestClient {

    public static void main(String[] args) throws Exception{
        ConOutMain.createConsole(true);
        RioCommProtocol protocol = new RioCommProtocol();
        protocol.startCommunication();
        while (true){
            String text = ConOutMain.getRef().textOut();
            if(text.equalsIgnoreCase("enable")){
                protocol.enable(true);
            }
            else if(text.equalsIgnoreCase("disable")){
                protocol.enable(false);
            }
            else if(text.equalsIgnoreCase("teleop")){
                protocol.setControlMode(ToRIOPacket.ControlMode.TELEOP);
            }
            else if(text.equalsIgnoreCase("auto")){
                protocol.setControlMode(ToRIOPacket.ControlMode.AUTO);
            }
            else if(text.equalsIgnoreCase("test")){
                protocol.setControlMode(ToRIOPacket.ControlMode.TEST);
            }
            else if(text.equalsIgnoreCase("reset")){
                protocol.request(false, true);
            }
            else if(text.equalsIgnoreCase("restart")){
                protocol.request(true, false);
            }
        }
    }

}
