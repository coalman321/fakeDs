import com.org.ConsoleLibs.ConOutMain;

public class TestClient {

    public static void main(String[] args) throws Exception{
        ConOutMain.createConsole(true);
        RioCommProtocol protocol = new RioCommProtocol();
        protocol.startCommunication();
        while (true){
            String text = ConOutMain.getRef().textOut();
            if(text.equalsIgnoreCase("enable")){
                protocol.setControlMode(RioCommProtocol.RobotState.TELEOPENABLED);
            }
            else if(text.equalsIgnoreCase("disable")){
                protocol.setControlMode(RioCommProtocol.RobotState.TELEOPDISABLED);
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
