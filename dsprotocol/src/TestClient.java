import com.org.ConsoleLibs.ConOutMain;

public class TestClient {

    public static void main(String[] args) throws Exception{
        ConOutMain.createConsole(true);
        DSUDPToRIO connection = new DSUDPToRIO();
        while (true){
            String text = ConOutMain.getRef().textOut();
            Thread.sleep(100);
            if(text.equalsIgnoreCase("stop") && (connection != null)){
                connection.close();
            }
            if(text.equalsIgnoreCase("start")){
                connection = new DSUDPToRIO(ToRIOPacket.AllianceNum.RED1);
                connection.startComms();
            }
        }
    }

}
