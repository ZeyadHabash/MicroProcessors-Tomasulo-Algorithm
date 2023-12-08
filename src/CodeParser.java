import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CodeParser {
    public static ArrayList<Instruction> readFile(String filename) {
        ArrayList<Instruction> res = new ArrayList<>();
        Map<String, Integer> Labels = new HashMap<String, Integer>();
        int hasLabel = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line = br.readLine();
            int i = 0;
            while (line != null) {
                String[] tokens = line.split(" ");
                Instruction instruction = null;
                if (tokens[hasLabel].equals("ADD.D") || tokens[hasLabel].equals("SUB.D") || tokens[hasLabel].equals("MUL.D") || tokens[hasLabel].equals("DIV.D") || tokens[hasLabel].equals("DADD") || tokens[hasLabel].equals("DSUB")) {
                    Register destination = RegisterFile.getRegister(tokens[hasLabel + 1]);
                    Register operand1 = RegisterFile.getRegister(tokens[hasLabel + 2]);
                    Register operand2 = RegisterFile.getRegister(tokens[hasLabel + 3]);
                    instruction = new Instruction(tokens[hasLabel], destination, operand1, operand2);
                    hasLabel = 0;
                } else if (tokens[hasLabel].equals("L.D")) {
                    Register destination = RegisterFile.getRegister(tokens[hasLabel + 1]);
                    Double immediateValue = Double.parseDouble(tokens[hasLabel + 2]);
                    instruction = new Instruction(tokens[hasLabel], destination, immediateValue);
                    hasLabel = 0;
                } else if (tokens[hasLabel].equals("S.D")) {
                    Register operand1 = RegisterFile.getRegister(tokens[hasLabel + 1]);
                    Double immediateValue = Double.parseDouble(tokens[hasLabel + 2]);
                    instruction = new Instruction(tokens[hasLabel], immediateValue, operand1);
                    hasLabel = 0;
                } else if (tokens[hasLabel].equals("ADDI") || tokens[hasLabel].equals("SUBI")) {
                    Register destination = RegisterFile.getRegister(tokens[hasLabel + 1]);
                    Register operand1 = RegisterFile.getRegister(tokens[hasLabel + 2]);
                    Double immediateValue = Double.parseDouble(tokens[hasLabel + 3]);
                    instruction = new Instruction(tokens[hasLabel], destination, operand1, immediateValue);
                    hasLabel = 0;
                } else if (tokens[hasLabel].equals("BNEZ")) {
                    Register operand1 = RegisterFile.getRegister(tokens[hasLabel + 1]);
                    AtomicReference<Integer> immediateValue = new AtomicReference<>(0);
                    int finalHasLabel = hasLabel;
                    Labels.forEach((key, value) -> {
                        if (key.equals(tokens[finalHasLabel + 2])) {
                            immediateValue.set(value);
                        }
                    });
                    instruction = new Instruction(tokens[hasLabel], Double.valueOf(immediateValue.get()), operand1);
                    hasLabel = 0;
                } else {
                    // if we have a label add it to the map and stay in this line
                    Labels.put(tokens[0], i);
                    hasLabel = 1;
                }
                if (hasLabel == 0) {
                    res.add(instruction);
                    line = br.readLine();
                    i++;
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
        return res;
    }

    public static void writeFile(String filename, String content) {
        try {
            File file = new File(filename);
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }


}
