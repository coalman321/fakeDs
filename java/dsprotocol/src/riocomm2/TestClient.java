package riocomm2;


import java.net.UnknownHostException;

public class TestClient {

    static RioCommProtocol protocol;
    static CustomDs ds;

    public static void main(String[] args){
        ds = new CustomDs();
        try{
            protocol = new RioCommProtocol(7303, "10.73.03.3");
            System.out.println("created protocol, starting communications");
            protocol.start();
            System.out.println("successfully started communications");
            ds.setRobotMode(SendingPacket.ControlMode.TELEOP.toString(), false);
            ds.getEnableButton().addActionListener(e -> enableButton());
            ds.getDisableButton().addActionListener(e -> disableButton());
            Thread thread = new Thread(() -> telemetryLoop());
            thread.setDaemon(true);
            thread.start();
            mainLoop();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void enableButton(){
        try {
            protocol.wantEnabled(true);
            ds.setRobotMode(protocol.getMode().toString(), true);
        }catch (NullPointerException exept){
            System.out.println("Could not perform request!");
        }
    }

    public static void disableButton(){
        try {
            protocol.wantEnabled(false);
            ds.setRobotMode(protocol.getMode().toString(), false);
        }catch (NullPointerException exept){
            System.out.println("Could not perform request!");
        }
    }

    public static void telemetryLoop(){
        while(true) {
            ds.setVoltage(protocol.getRecieving().getVoltage());
            ds.setPacketloss(protocol.getRecieving().getDropped());
        }
    }

    public static void mainLoop(){
        while (true) {
            String text = ds.textOut();
            int assertioncount = -1;
            try {
                if (text.equalsIgnoreCase("teleop")) {
                    protocol.setMode(SendingPacket.ControlMode.TELEOP);
                    ds.setRobotMode(SendingPacket.ControlMode.TELEOP.toString(), false);
                } else if (text.equalsIgnoreCase("auto")) {
                    protocol.setMode(SendingPacket.ControlMode.AUTO);
                    ds.setRobotMode(SendingPacket.ControlMode.AUTO.toString(), false);
                } else if (text.equalsIgnoreCase("test")) {
                    protocol.setMode(SendingPacket.ControlMode.TEST);
                    ds.setRobotMode(SendingPacket.ControlMode.TEST.toString(), false);
                } else if (text.equalsIgnoreCase("reset")) {
                    protocol.wantReset(true);
                    assertioncount = 0;
                } else if (text.equalsIgnoreCase("restart")) {
                    protocol.wantRestart(true);
                    assertioncount = 0;
                } else if (text.equalsIgnoreCase("close")) {
                    //System.out.println("closing connection");
                    protocol.close();
                    System.out.println("Closing!");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                    System.exit(1);
                }
                if(assertioncount > -1){
                    assertioncount++;
                }
                else if(assertioncount > 9){
                    protocol.wantReset(false);
                    protocol.wantRestart(false);
                    assertioncount = -1;
                }
            }catch (NullPointerException except){
                System.out.println("Could not perform request!");
            }
        }
    }

}
