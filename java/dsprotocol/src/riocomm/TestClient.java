package riocomm;

import specialconsole.CustomDs;

import java.lang.reflect.Field;

public class TestClient {

    static RioCommProtocol protocol;
    static CustomDs ds;

    public static void main(String[] args){
        ds = new CustomDs();
        protocol = new RioCommProtocol(7303);
        protocol.startCommunication();
        ds.setRobotMode(ToRIOPacket.ControlMode.TELEOP, false);
        ds.getEnableButton().addActionListener(e -> enableButton());
        ds.getDisableButton().addActionListener(e -> disableButton());
        Thread thread = new Thread(() -> telemetryLoop());
        thread.setDaemon(true);
        thread.start();
        mainLoop();
    }

    public static void enableButton(){
        try {
            protocol.enable(true);
            ds.setRobotMode(protocol.getRobotMode(), true);
        }catch (NullPointerException exept){
            System.out.println("Could not perform request!");
        }
    }

    public static void disableButton(){
        try {
            protocol.enable(false);
            ds.setRobotMode(protocol.getRobotMode(), false);
        }catch (NullPointerException exept){
            System.out.println("Could not perform request!");
        }
    }

    public static void telemetryLoop(){
        while(true) {
            ds.setVoltage(protocol.getLastPacket().getVoltage());
            ds.setPacketloss(protocol.getLastPacket().getDropped());
        }
    }

    public static void mainLoop(){
        while (true) {
            String text = ds.textOut();
            try {
                if (text.equalsIgnoreCase("teleop")) {
                    protocol.setControlMode(ToRIOPacket.ControlMode.TELEOP);
                    ds.setRobotMode(ToRIOPacket.ControlMode.TELEOP, false);
                } else if (text.equalsIgnoreCase("auto")) {
                    protocol.setControlMode(ToRIOPacket.ControlMode.AUTO);
                    ds.setRobotMode(ToRIOPacket.ControlMode.AUTO, false);
                } else if (text.equalsIgnoreCase("test")) {
                    protocol.setControlMode(ToRIOPacket.ControlMode.TEST);
                    ds.setRobotMode(ToRIOPacket.ControlMode.TEST, false);
                } else if (text.equalsIgnoreCase("reset")) {
                    protocol.request(false, true);
                } else if (text.equalsIgnoreCase("restart")) {
                    protocol.request(true, false);
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
            }catch (NullPointerException except){
                System.out.println("Could not perform request!");
            }
        }
    }

}
