package riocomm;

import specialconsole.CustomDs;

import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestClient {

    public static void main(String[] args){
        JFrame frame = new JFrame("CustomDs");
        CustomDs ds = new CustomDs();
        JPanel mainpanel = ds.getMainPanel();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        frame.setContentPane(mainpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        RioCommProtocol protocol = new RioCommProtocol(7303);
        protocol.startCommunication();
        ds.getEnableButton().addActionListener(e -> {
            protocol.enable(true);
        });
        ds.getDisableButton().addActionListener(e -> {
            protocol.enable(false);
        });
        ds.setVoltage(12.75);

        while (true) {
            String text = ds.textOut();
            if (text.equalsIgnoreCase("teleop")) {
                protocol.setControlMode(ToRIOPacket.ControlMode.TELEOP);
                ds.setRobotMode(ToRIOPacket.ControlMode.TELEOP);
            } else if (text.equalsIgnoreCase("auto")) {
                protocol.setControlMode(ToRIOPacket.ControlMode.AUTO);
                ds.setRobotMode(ToRIOPacket.ControlMode.AUTO);
            } else if (text.equalsIgnoreCase("test")) {
                protocol.setControlMode(ToRIOPacket.ControlMode.TEST);
                ds.setRobotMode(ToRIOPacket.ControlMode.TEST);
            } else if (text.equalsIgnoreCase("reset")) {
                protocol.request(false, true);
            } else if (text.equalsIgnoreCase("restart")) {
                protocol.request(true, false);
            } else if (text.equalsIgnoreCase("close")) {
                //System.out.println("closing connection");
                protocol.close();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                System.exit(1);
            }
        }
    }

}
