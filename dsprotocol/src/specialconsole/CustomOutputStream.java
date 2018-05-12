package specialconsole;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class CustomOutputStream extends OutputStream {
    private JTextArea textarea;

    public CustomOutputStream(JTextArea textarea) {
        this.textarea = textarea;
    }

    public void write(int b) throws IOException {
        this.textarea.append(String.valueOf((char)b));
        this.textarea.setCaretPosition(this.textarea.getDocument().getLength());
    }
}
