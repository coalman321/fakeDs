package specialconsole;

import com.org.ConsoleLibs.CustomOutputStream;
import riocomm.RioCommProtocol;
import riocomm.ToRIOPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.Scanner;

public class CustomDs {
    private JTextField consoleReadIn;
    private JTextArea consoleReadOut;
    private JTextField packetReadOut;
    private JTextField customReadOut2;
    private JTextField customReadOut1;
    private JTextField voltageReadOut;
    private JButton disableButton;
    private JButton enableButton;
    private JTextField robotModeReadOut;
    private JLabel customLabel1;
    private JLabel customLabel2;
    private JLabel packetLossLabel;
    private JLabel voltageLabel;
    private JLabel robotModeLabel;
    private JLabel systemStatusLabel;
    private JLabel systemDiagnosticsLabel;
    private JPanel mainPanel;
    private JPanel consolePanel;
    private JPanel systemPanel;
    private JPanel diagnosticsPanel;
    private JPanel diagnosticLabelsPanel;
    private JPanel diagnosticReadoutPanel;
    private JPanel systemStatusPanel;
    private JPanel robotModePanel;
    private String Input;
    private String lastInput;

    public CustomDs() {
        $$$setupUI$$$();
        PrintStream printStream = new PrintStream(new CustomOutputStream(consoleReadOut));
        System.setOut(printStream);
        consoleReadIn.addActionListener(e -> {
            Input = consoleReadIn.getText();
            consoleReadIn.setText(null);
            consoleReadOut.append(Input + "\n");
            consoleReadOut.setCaretPosition(consoleReadOut.getDocument().getLength());

            try {
                Thread.sleep(1L);
            } catch (InterruptedException uselessexceptioncode) {

            }
        });
        consoleReadIn.requestFocusInWindow();
        consoleReadIn.requestFocus();
        JFrame frame = new JFrame("CustomDs");
        JPanel mainpanel = getMainPanel();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        frame.setContentPane(mainpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton getEnableButton() {
        return enableButton;
    }

    public JButton getDisableButton() {
        return disableButton;
    }

    public void setVoltage(double voltage) {
        voltageReadOut.setText(String.format("%.3f", voltage) + " v");
    }

    public void setPacketloss(int lost) {
        packetReadOut.setText("" + lost);
    }

    public void setRobotMode(ToRIOPacket.ControlMode mode, boolean isEnabled) {
        robotModeReadOut.setText(mode.toString() + (isEnabled ? " - Enabled" : " - Disabled"));
    }

    public void setCustomReadOut1(String label, String data) {
        customLabel1.setText(label);
        customReadOut1.setText(data);
    }

    public void setCustomReadOut2(String label, String data) {
        customLabel2.setText(label);
        customReadOut2.setText(data);
    }

    public String textOut() {
        while (this.Input == null || this.Input == this.lastInput) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException var2) {

            }
        }

        this.lastInput = this.Input;
        return this.Input;
    }

    public double doubleOut() {
        try {
            return Double.parseDouble(textOut());
        } catch (NumberFormatException var2) {
            System.out.println("That was not a number! Try again!");
            this.doubleOut();
            return 0.0D;
        }
    }

    public int intOut() {
        try {
            return Integer.parseInt(textOut());
        } catch (NumberFormatException var2) {
            System.out.println("That was not a number! Try again!");
            intOut();
            return 0;
        }
    }

    public long longOut() {
        try {
            return Long.parseLong(textOut());
        } catch (NumberFormatException var2) {
            System.out.println("That was not a number! Try again!");
            longOut();
            return 0L;
        }
    }

    public boolean boolOut() {
        try {
            return Boolean.parseBoolean(textOut());
        } catch (NumberFormatException var2) {
            System.out.println("That was not a Boolean! Try again!");
            boolOut();
            return false;
        }
    }

    public char charAt(int n) {
        return textOut().charAt(n);
    }

    public boolean findInInput(String s) {
        return textOut().contains(s);
    }

    public int findLocationInInput(String S) {
        return textOut().indexOf(S);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setEnabled(true);
        consolePanel = new JPanel();
        consolePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        consolePanel.setEnabled(true);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(consolePanel, gbc);
        consoleReadIn = new JTextField();
        consoleReadIn.setEnabled(true);
        consolePanel.add(consoleReadIn, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        consolePanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(500, 300), new Dimension(500, 300), null, 0, false));
        consoleReadOut = new JTextArea();
        consoleReadOut.setEditable(false);
        consoleReadOut.setEnabled(true);
        scrollPane1.setViewportView(consoleReadOut);
        systemPanel = new JPanel();
        systemPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        systemPanel.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(systemPanel, gbc);
        diagnosticsPanel = new JPanel();
        diagnosticsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        diagnosticsPanel.setEnabled(true);
        systemPanel.add(diagnosticsPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        diagnosticLabelsPanel = new JPanel();
        diagnosticLabelsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        diagnosticLabelsPanel.setEnabled(true);
        diagnosticsPanel.add(diagnosticLabelsPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        packetLossLabel = new JLabel();
        packetLossLabel.setEnabled(true);
        packetLossLabel.setText("Packet Loss");
        diagnosticLabelsPanel.add(packetLossLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customLabel2 = new JLabel();
        customLabel2.setEnabled(true);
        customLabel2.setText("Label");
        diagnosticLabelsPanel.add(customLabel2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customLabel1 = new JLabel();
        customLabel1.setEnabled(true);
        customLabel1.setText("Label");
        diagnosticLabelsPanel.add(customLabel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        voltageLabel = new JLabel();
        voltageLabel.setEnabled(true);
        voltageLabel.setText("Voltage");
        diagnosticLabelsPanel.add(voltageLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        diagnosticReadoutPanel = new JPanel();
        diagnosticReadoutPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        diagnosticReadoutPanel.setEnabled(true);
        diagnosticsPanel.add(diagnosticReadoutPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        customReadOut2 = new JTextField();
        customReadOut2.setEditable(false);
        customReadOut2.setEnabled(true);
        diagnosticReadoutPanel.add(customReadOut2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        customReadOut1 = new JTextField();
        customReadOut1.setEditable(false);
        customReadOut1.setEnabled(true);
        diagnosticReadoutPanel.add(customReadOut1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        voltageReadOut = new JTextField();
        voltageReadOut.setEditable(false);
        voltageReadOut.setEnabled(true);
        diagnosticReadoutPanel.add(voltageReadOut, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        packetReadOut = new JTextField();
        packetReadOut.setEditable(false);
        packetReadOut.setEnabled(true);
        diagnosticReadoutPanel.add(packetReadOut, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        systemStatusPanel = new JPanel();
        systemStatusPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        systemStatusPanel.setEnabled(true);
        systemPanel.add(systemStatusPanel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        robotModePanel = new JPanel();
        robotModePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        robotModePanel.setEnabled(true);
        systemStatusPanel.add(robotModePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 50), new Dimension(-1, 50), new Dimension(-1, 50), 0, false));
        robotModeReadOut = new JTextField();
        robotModeReadOut.setEditable(false);
        robotModeReadOut.setEnabled(true);
        robotModePanel.add(robotModeReadOut, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        robotModeLabel = new JLabel();
        robotModeLabel.setEnabled(true);
        robotModeLabel.setText("Robot Mode");
        robotModePanel.add(robotModeLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enableButton = new JButton();
        enableButton.setBackground(new Color(-15908594));
        enableButton.setEnabled(true);
        enableButton.setForeground(new Color(-131073));
        enableButton.setHideActionText(true);
        enableButton.setText("Enable");
        systemStatusPanel.add(enableButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 70), new Dimension(-1, 70), new Dimension(-1, 70), 0, false));
        disableButton = new JButton();
        disableButton.setBackground(new Color(-12515055));
        disableButton.setEnabled(true);
        disableButton.setForeground(new Color(-131073));
        disableButton.setText("Disable");
        systemStatusPanel.add(disableButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 70), new Dimension(-1, 70), new Dimension(-1, 70), 0, false));
        systemDiagnosticsLabel = new JLabel();
        systemDiagnosticsLabel.setEnabled(true);
        systemDiagnosticsLabel.setText("System Diagnostics");
        systemPanel.add(systemDiagnosticsLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        systemStatusLabel = new JLabel();
        systemStatusLabel.setEnabled(true);
        systemStatusLabel.setText("System Status");
        systemPanel.add(systemStatusLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
