import java.util.ArrayList;

public class InstructionQueue {
    private ArrayList<Instruction> instructions;

    public InstructionQueue() {
        this.instructions = new ArrayList<Instruction>();
    }

    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
    }

    public Instruction getInstruction(int index) {
        return this.instructions.get(index);
    }

    public int size() {
        return this.instructions.size();
    }

    public String toString() {
        String result = "";
        for (Instruction instruction : this.instructions) {
            result += instruction.toString() + "\n";
        }
        return result;
    }
}
