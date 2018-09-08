package riocomm2;


import java.net.UnknownHostException;

public class TestClient {

    static RioCommProtocol protocol;
    static CustomDs ds;
    static boolean errored = false;

    public static void main(String[] args){
        ds = new CustomDs();
        try{
            protocol = new RioCommProtocol(7303, "10.73.03.3");
            System.out.println("created protocol, starting communications");
            protocol.start();
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
        Exception recieveError;
        int errorPersist = -1;
        ds.setCustomReadOut1("Errors", "none");
        while(true) {
            ds.setVoltage(protocol.getRecieving().getVoltage());
            ds.setPacketloss(protocol.getRecieving().getDropped());
            recieveError = protocol.getExceptions();
            if(recieveError != null){
                errorPersist = 0;
                errored = true;
                ds.getDisableButton().setEnabled(false);
                ds.getEnableButton().setEnabled(false);
                ds.setCustomReadOut1("Errors", recieveError.getMessage());
                recieveError.printStackTrace();
                System.out.println("Irrecoverable Error:");
                System.out.println(recieveError.getLocalizedMessage());
            }
            if(errorPersist > 500){
                ds.setCustomReadOut1("Errors", "none");
                errorPersist = -1;
            }
            else if(errorPersist > -1) {
                errorPersist++;
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e){
            }
        }
    }

    public static void mainLoop(){
        while (!errored) {
            String text = ds.textOut();
            int assertioncount = -1;
            try {
                if (text.equalsIgnoreCase("teleop")) {
                    protocol.setMode(SendingPacket.ControlMode.TELEOP);
                    protocol.wantEnabled(false);
                    ds.setRobotMode(SendingPacket.ControlMode.TELEOP.toString(), false);
                } else if (text.equalsIgnoreCase("auto")) {
                    protocol.setMode(SendingPacket.ControlMode.AUTO);
                    protocol.wantEnabled(false);
                    ds.setRobotMode(SendingPacket.ControlMode.AUTO.toString(), false);
                } else if (text.equalsIgnoreCase("test")) {
                    protocol.setMode(SendingPacket.ControlMode.TEST);
                    protocol.wantEnabled(false);
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
        while (!ds.textOut().equalsIgnoreCase("close")) {}
        //System.out.println("closing connection");
        protocol.close();
        System.out.println("Closing!");

    }

}
