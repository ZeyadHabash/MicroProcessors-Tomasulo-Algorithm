import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.*;

public class TextAreaOutputStream extends OutputStream {
    private JTextArea textArea;

    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        append(String.valueOf((char)b));
    }

    public void append(String str) {
        SwingUtilities.invokeLater(() -> textArea.append(str));
    }

    public void search(String searchString) {
        String textAreaContent = textArea.getText();
        int index = textAreaContent.indexOf(searchString);
        if (index >= 0) { // String found
            textArea.setCaretPosition(index);
            textArea.moveCaretPosition(index + searchString.length());
            textArea.getCaret().setSelectionVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Search string not found", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}