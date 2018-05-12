package specialconsole;

import com.org.ConsoleLibs.CustomOutputStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.Scanner;

public class SpecialConsole extends JFrame implements ActionListener {
    private JTextArea textarea;
    public JTextField inputarea;
    private PrintStream standardOut;
    private JFrame frame;
    private String Input;
    private String lastInput;
    private boolean enableCmd;

    public SpecialConsole(boolean bool) {
        super("Console");
        this.enableCmd = bool;
        this.frame = new JFrame("Console");
        this.textarea = new JTextArea(30, 80);
        this.inputarea = new JTextField(30);
        this.textarea.setEditable(false);
        this.inputarea.setEditable(true);
        this.inputarea.addActionListener(this);
        this.frame.setDefaultCloseOperation(3);
        PrintStream printStream = new PrintStream(new CustomOutputStream(this.textarea));
        this.standardOut = System.out;
        System.setOut(printStream);
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 200);
        constraints.anchor = 17;
        constraints.gridx = 2;
        constraints.gridy = 2;
        this.add(this.inputarea, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = 1;
        constraints.weightx = 1.0D;
        constraints.weighty = 1.0D;
        this.add(new JScrollPane(this.textarea), constraints);
        this.frame.setDefaultCloseOperation(3);
        this.frame.setSize(700, 320);
        this.frame.setLocationRelativeTo((Component)null);
        this.inputarea.requestFocusInWindow();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent evt) {
        this.Input = this.inputarea.getText();
        this.cmd();
        this.inputarea.setText((String)null);
        this.textarea.append(this.Input + "\n");
        this.textarea.setCaretPosition(this.textarea.getDocument().getLength());

        try {
            Thread.sleep(1L);
        } catch (InterruptedException var3) {
            ;
        }

    }

    public String textOut() {
        while(this.Input == null || this.Input == this.lastInput) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException var2) {
                ;
            }
        }

        this.lastInput = this.Input;
        return this.Input;
    }

    public void cmd() {
        if (this.enableCmd) {
            try {
                if (this.Input.startsWith("-pt")) {
                    System.out.println(this.Input.substring(3, this.Input.length() - 1));
                } else if (!this.Input.startsWith("-cmd")) {
                    if (this.Input.startsWith("-help")) {
                        System.out.println("only current commands are \n-exit exits with system status 0. \n-pt prints everything after the call.");
                    } else if (this.Input.startsWith("-exit")) {
                        System.exit(0);
                    }
                }
            } catch (StringIndexOutOfBoundsException var2) {
                var2.printStackTrace();
            }
        }

    }

    public double doubleOut() {
        try {
            return Double.parseDouble(this.textOut());
        } catch (NumberFormatException var2) {
            System.out.println("That was not a number! Try again!");
            this.doubleOut();
            return 0.0D;
        }
    }

    public int intOut() {
        try {
            return Integer.parseInt(this.textOut());
        } catch (NumberFormatException var2) {
            System.out.println("That was not a number! Try again!");
            this.intOut();
            return 0;
        }
    }

    public long longOut() {
        try {
            return Long.parseLong(this.textOut());
        } catch (NumberFormatException var2) {
            System.out.println("That was not a number! Try again!");
            this.longOut();
            return 0L;
        }
    }

    public boolean boolOut() {
        try {
            return Boolean.parseBoolean(this.textOut());
        } catch (NumberFormatException var2) {
            System.out.println("That was not a Boolean! Try again!");
            this.boolOut();
            return false;
        }
    }

    public char charAt(int n) {
        String in = this.textOut();
        return in.charAt(n);
    }

    public boolean findInInput(String s) {
        Scanner in = new Scanner(this.textOut());
        if (s == in.findInLine(s)) {
            in.close();
            return true;
        } else {
            in.close();
            return false;
        }
    }

    public int findLocationInInput(String S) {
        return this.textOut().indexOf(S);
    }

    public void windowClosing(WindowEvent e) {
        this.frame.dispose();
        System.exit(0);
    }

    /** @deprecated */
    public void exit() {
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

        System.exit(0);
    }

}

