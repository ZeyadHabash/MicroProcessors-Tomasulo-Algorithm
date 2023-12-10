import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

public class TomasuloGUI extends JFrame {
    JButton runButton = new JButton("Run");
    private JTextField addStationSizeField = new JTextField(5);
    private JTextField mulStationSizeField = new JTextField(5);
    private JTextField loadBufferSizeField = new JTextField(5);
    private JTextField storeBufferSizeField = new JTextField(5);
    private JTextField addLatencyField = new JTextField(5);
    private JTextField subLatencyField = new JTextField(5);
    private JTextField mulLatencyField = new JTextField(5);
    private JTextField divLatencyField = new JTextField(5);
    private JTextField loadLatencyField = new JTextField(5);
    private JTextField storeLatencyField = new JTextField(5);
    private JTextField SUBILatencyField = new JTextField(5);
    private JTextField DADDLatencyField = new JTextField(5);

    // Add more fields for other parameters
    private JTextField DSUBLatencyField = new JTextField(5);
    private JTextArea outputArea = new JTextArea(20, 50);
    private JTextField searchBar = new JTextField(20); // Search bar

    public TomasuloGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JPanel sizePanel = new JPanel(new GridBagLayout());
        sizePanel.setBorder(BorderFactory.createTitledBorder("Sizes"));
        GridBagConstraints c = new GridBagConstraints();
        addField(sizePanel, "Add Station Size:", addStationSizeField, 0, c);
        addField(sizePanel, "Mul Station Size:", mulStationSizeField, 1, c);
        addField(sizePanel, "Load Buffer Size:", loadBufferSizeField, 2, c);
        addField(sizePanel, "Store Buffer Size:", storeBufferSizeField, 3, c);

        JPanel latencyPanel = new JPanel(new GridBagLayout());
        latencyPanel.setBorder(BorderFactory.createTitledBorder("Latencies"));
        addField(latencyPanel, "Add Latency:", addLatencyField, 0, c);
        addField(latencyPanel, "Sub Latency:", subLatencyField, 1, c);
        addField(latencyPanel, "Mul Latency:", mulLatencyField, 2, c);
        addField(latencyPanel, "Div Latency:", divLatencyField, 3, c);
        addField(latencyPanel, "Load Latency:", loadLatencyField, 4, c);
        addField(latencyPanel, "Store Latency:", storeLatencyField, 5, c);
        addField(latencyPanel, "SUBI Latency:", SUBILatencyField, 6, c);
        addField(latencyPanel, "DADD Latency:", DADDLatencyField, 7, c);
        addField(latencyPanel, "DSUB Latency:", DSUBLatencyField, 8, c);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(sizePanel, BorderLayout.NORTH);
        inputPanel.add(latencyPanel, BorderLayout.CENTER);

        runButton.addActionListener(new RunAction());
        inputPanel.add(runButton, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(inputPanel, gbc);

        outputArea.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
//        add(new JScrollPane(outputArea), gbc);


        // Create a TextAreaOutputStream and redirect standard output to it
        TextAreaOutputStream textAreaOutputStream = new TextAreaOutputStream(outputArea);
        System.setOut(new PrintStream(textAreaOutputStream));

        // Add the search bar to the top of the output area
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        JLabel searchLabel = new JLabel("Search:"); // Create a label
        outputPanel.add(searchLabel); // Add the label to the panel
        searchBar.setMaximumSize(new Dimension(200, searchBar.getPreferredSize().height)); // Set maximum width
        searchBar.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the search bar
        outputPanel.add(searchBar);
        outputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add a vertical spacer
        outputPanel.add(new JScrollPane(outputArea));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(outputPanel, gbc);

        // Add an action listener to the search bar
        searchBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchBar.getText();
                textAreaOutputStream.search(searchString);
            }
        });

        pack();
        setSize(getHeight() * 4, getHeight() * 2);
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TomasuloGUI::new);
    }

    private void addField(JPanel panel, String labelText, JTextField textField, int row, GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(labelText), c);

        c.gridx = 1;
        panel.add(textField, c);
    }

    private class RunAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int addStationSize = Integer.parseInt(addStationSizeField.getText());
                int mulStationSize = Integer.parseInt(mulStationSizeField.getText());
                int loadBufferSize = Integer.parseInt(loadBufferSizeField.getText());
                int storeBufferSize = Integer.parseInt(storeBufferSizeField.getText());
                int addLatency = Integer.parseInt(addLatencyField.getText());
                int subLatency = Integer.parseInt(subLatencyField.getText());
                int mulLatency = Integer.parseInt(mulLatencyField.getText());
                int divLatency = Integer.parseInt(divLatencyField.getText());
                int loadLatency = Integer.parseInt(loadLatencyField.getText());
                int storeLatency = Integer.parseInt(storeLatencyField.getText());
                int SUBILatency = Integer.parseInt(SUBILatencyField.getText());
                int DADDLatency = Integer.parseInt(DADDLatencyField.getText());
                int DSUBLatency = Integer.parseInt(DSUBLatencyField.getText());
                // Parse more fields

                // Redirect standard output to the text area
                System.setOut(new PrintStream(new TextAreaOutputStream(outputArea)));

                Tomasulo tomasulo = Tomasulo.getInstance(addStationSize, mulStationSize, loadBufferSize, storeBufferSize, addLatency, subLatency, mulLatency, divLatency, loadLatency, storeLatency, SUBILatency, DADDLatency, DSUBLatency); // Create a new instance of the tomasulo simulator
                tomasulo.init();
                tomasulo.run();
                runButton.setEnabled(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(TomasuloGUI.this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}