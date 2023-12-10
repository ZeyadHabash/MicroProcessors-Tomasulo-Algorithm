import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public class TextAreaOutputStream extends OutputStream {
    private JTextArea textArea;

    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        append(String.valueOf((char)b));
    }

    public void append(String str) {
        SwingUtilities.invokeLater(() -> textArea.append(str));
    }

    private int lastIndex = 0; // Add this field to keep track of the last index

    public void search(String searchString) {
        String textAreaContent = textArea.getText();
        int index = textAreaContent.indexOf(searchString, lastIndex);
        if (index >= 0) { // String found
            textArea.setCaretPosition(index);
            textArea.moveCaretPosition(index + searchString.length());
            textArea.getCaret().setSelectionVisible(true);
            lastIndex = index + searchString.length();
        } else {
            if (lastIndex > 0) { // If end of text is reached, start from the beginning
                lastIndex = 0;
                search(searchString);
            } else {
                JOptionPane.showMessageDialog(null, "Search string not found", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}