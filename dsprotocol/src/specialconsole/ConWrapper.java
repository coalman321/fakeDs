package specialconsole;

import javax.swing.*;

public class ConWrapper {
    private static SpecialConsole con;

    public static void createConsole(boolean bool) {
        con = new SpecialConsole(bool);
        SwingUtilities.invokeLater(() -> {
            con.pack();
            con.setVisible(true);
            con.inputarea.requestFocusInWindow();
        });
    }

    public static SpecialConsole getRef(){
        return con;
    }
}
