import com.org.ConsoleLibs.ConOutMain;

public class TestClient {

    public static void main(String[] args) throws Exception{
        ConOutMain.createConsole(true);
        DSUdpProtocol connection = new DSUdpProtocol(DSUdpProtocol.AllianceNum.RED1);
        connection.startComms();
        while (true){
            Thread.sleep(100);
        }
    }

}
