import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TextAreaOutputStream extends OutputStream {
    private JTextArea textArea;

    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        append(String.valueOf((char)b));
    }

    public void append(String str) {
        SwingUtilities.invokeLater(() -> textArea.append(str));
    }
}