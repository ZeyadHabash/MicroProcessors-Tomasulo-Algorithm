import java.util.ArrayList;

public class InstructionQueue {
    private ArrayList<Instruction> instructions;

    public InstructionQueue() {
        this.instructions = new ArrayList<Instruction>();
    }

    public void add(Instruction instruction) {
        this.instructions.add(instruction);
    }

    public Instruction get(int index) {
        return this.instructions.get(index);
    }

    public int size() {
        return this.instructions.size();
    }


    public void print() {
        System.out.println("Instruction Queue:");
        System.out.println("Operation\tDestination\tj\tk\tIssue\tExecution\t\tWrite Result");
        for (Instruction instruction : this.instructions) {
            System.out.println(instruction.toString());
        };
    }
}
