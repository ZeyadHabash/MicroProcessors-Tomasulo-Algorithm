import java.util.ArrayList;

public class Tomasulo {
    // make Tomasulo a singleton
    private static Tomasulo instance = null;
    // Latencies of Integer Operations
    final int ADDILatency = 1;
    final int BNEZLatency = 1;
    int currentCycle = 0; // Current clock cycle number
    int pc = 0; // Program counter
    double[] cache = new double[1024]; // Cache memory
    RegisterFile registerFile; // Register file
    ReservationStation addReservationStation;
    ReservationStation mulReservationStation;
    ReservationStation loadBuffer;
    ReservationStation storeBuffer;
    InstructionQueue instructionQueue;
    ArrayList<Instruction> program; // Program instructions
    String fileName = "input.txt"; // Input file name
    // Sizes of the reservation stations and buffers
    int addStationSize = 3;
    int mulStationSize = 2;
    int loadBufferSize = 3;
    int storeBufferSize = 3;
    // Latencies of FP Operations
    int addLatency = 2;
    int subLatency = 4;
    int mulLatency = 10;
    int divLatency = 20;
    int loadLatency = 2;
    int storeLatency = 2;
    int SUBILatency = 1;
    int DADDLatency = 1;
    int DSUBLatency = 1;

    private Tomasulo() {
        // Exists only to defeat instantiation.
    }

    public static Tomasulo getInstance() {
        if (instance == null) {
            instance = new Tomasulo();
        }
        return instance;
    }

    public void init() {
        // TODO: Initialize latencies through user input

        // TODO: Initialize RS and Buffer sizes through user input\

        // Initialize the cache
        for (int i = 0; i < 1024; i++) {
            cache[i] = i * 1.5;
        }

        RegisterFile.initRegisterFile();

        // Initialize the reservation stations and buffers
        addReservationStation = new ReservationStation("add", addStationSize, "A");
        mulReservationStation = new ReservationStation("mul", mulStationSize, "M");
        loadBuffer = new ReservationStation("load", loadBufferSize, "L");
        storeBuffer = new ReservationStation("store", storeBufferSize, "S");

        // Initialize the instruction queue
        instructionQueue = new InstructionQueue();

        // Parse the input file
        program = CodeParser.readFile(fileName);
    }

    public void run() {
        while (pc < program.size()){ // Update the current cycle number
            currentCycle++;

            // get instruction from program
            Instruction instruction = program.get(pc);

            // issue
            issue(instruction);

            // Execute
            execute();

            // Write result
            writeResult();

            // print
            print();

            // increment program counter
            pc++;
        }
    }

    public void issue(Instruction instruction) {
        String operation = instruction.getOperation();
        Register destination = instruction.getDestination();
        Register op1 = instruction.getOperand1();
        Register op2 = instruction.getOperand2();
        Double immediateValue = instruction.getImmediateValue();

        Double Vj = null;
        Double Vk = null;
        String Qj = "";
        String Qk = "";
        int address = 0;

        String tag = "";

        // check if reservation station is full
        // issue to reservation station
        if (operation.equals("L.D")) {
            if (loadBuffer.isFull()) {
                return;
            }

            // cast immediate value to int to get address
            address = (int) Math.round(immediateValue);

            tag = loadBuffer.issueInstruction(operation, Vj, Vk, Qj, Qk, address);
        } else if (operation.equals("S.D")) {
            if (storeBuffer.isFull()) {
                return;
            }

            // check if operand is ready to put in Vj
            if (op1.isReady()) {
                Vj = op1.getValue();
            }
            // if not, put in Qj
            else {
                Qj = op1.getQi();
            }

            // cast immediate value to int to get address
            address = (int) Math.round(immediateValue);

            tag = storeBuffer.issueInstruction(operation, Vj, Vk, Qj, Qk, address);
        } else if (operation.equals("MUL.D") || operation.equals("DIV.D")) {
            if (mulReservationStation.isFull()) {
                return;
            }

            // check if operand is ready to put in Vj
            if (op1.isReady()) {
                Vj = op1.getValue();
            }
            // if not, put in Qj
            else {
                Qj = op1.getQi();
            }

            // check if operand is ready to put in Vk
            if (op2.isReady()) {
                Vk = op2.getValue();
            }
            // if not, put in Qk
            else {
                Qk = op2.getQi();
            }

            tag = mulReservationStation.issueInstruction(operation, Vj, Vk, Qj, Qk, address);
        } else {
            if (addReservationStation.isFull()) {
                return;
            }

            // check if operand is ready to put in Vj
            if (op1.isReady()) {
                Vj = op1.getValue();
            }
            // if not, put in Qj
            else {
                Qj = op1.getQi();
            }

            // check if immediate operation or reigster operation
            if (operation.equals("ADDI") || operation.equals("SUBI") || operation.equals("BNEZ")) {
                Vk = immediateValue;
            } else {
                // check if operand is ready to put in Vk
                if (op2.isReady()) {
                    Vk = op2.getValue();
                }
                // if not, put in Qk
                else {
                    Qk = op2.getQi();
                }
            }

            tag = addReservationStation.issueInstruction(operation, Vj, Vk, Qj, Qk, address);
        }
        // put in instruction queue and set issue cycle
        instruction.setIssue(currentCycle);
        instructionQueue.add(instruction);

        // set Qi of destination register to tag of reservation station row
        if (!(operation.equals("S.D") || operation.equals("BNEZ"))) {
            destination.setQi(tag);
        }

        // increment use count of any reservation station row that this instruction is waiting on
        if (!Qj.equals("")) {
            incrementUseCount(Qj);
        }
        if (!Qk.equals("")) {
            incrementUseCount(Qk);
        }
    }

    public void execute() {

    }

    public void writeResult() {

    }


    // Helper functions

    public void decrementUseCount(String tag) {
        if (tag.charAt(0) == 'A') {
            for (int i = 0; i < addStationSize; i++) {
                if (addReservationStation.rows[i].getTag().equals(tag)) {
                    addReservationStation.rows[i].decrementUseCount();
                }
            }
        } else if (tag.charAt(0) == 'M') {
            for (int i = 0; i < mulStationSize; i++) {
                if (mulReservationStation.rows[i].getTag().equals(tag)) {
                    mulReservationStation.rows[i].decrementUseCount();
                }
            }
        } else if (tag.charAt(0) == 'L') {
            for (int i = 0; i < loadBufferSize; i++) {
                if (loadBuffer.rows[i].getTag().equals(tag)) {
                    loadBuffer.rows[i].decrementUseCount();
                }
            }
        } else if (tag.charAt(0) == 'S') {
            for (int i = 0; i < storeBufferSize; i++) {
                if (storeBuffer.rows[i].getTag().equals(tag)) {
                    storeBuffer.rows[i].decrementUseCount();
                }
            }
        }
    }
    public void incrementUseCount(String tag) {
        if (tag.charAt(0) == 'A') {
            for (int i = 0; i < addStationSize; i++) {
                if (addReservationStation.rows[i].getTag().equals(tag)) {
                    addReservationStation.rows[i].incrementUseCount();
                }
            }
        } else if (tag.charAt(0) == 'M') {
            for (int i = 0; i < mulStationSize; i++) {
                if (mulReservationStation.rows[i].getTag().equals(tag)) {
                    mulReservationStation.rows[i].incrementUseCount();
                }
            }
        } else if (tag.charAt(0) == 'L') {
            for (int i = 0; i < loadBufferSize; i++) {
                if (loadBuffer.rows[i].getTag().equals(tag)) {
                    loadBuffer.rows[i].incrementUseCount();
                }
            }
        } else if (tag.charAt(0) == 'S') {
            for (int i = 0; i < storeBufferSize; i++) {
                if (storeBuffer.rows[i].getTag().equals(tag)) {
                    storeBuffer.rows[i].incrementUseCount();
                }
            }
        }
    }

    // Print functions
    public void print() {
        System.out.println("Program: ");
        printProgram();
        System.out.println("Cycle: " + currentCycle);
        System.out.println("PC: " + pc);
        RegisterFile.printRegisterFile();
        System.out.println("Reservation Stations:");
        System.out.println("Add Reservation Station:");
        addReservationStation.print();
        System.out.println("Mul Reservation Station:");
        mulReservationStation.print();
        System.out.println("Load Buffer:");
        loadBuffer.print();
        System.out.println("Store Buffer:");
        storeBuffer.print();
        instructionQueue.print();
        System.out.println();
    }

    public void printProgram() {
        System.out.println("{");
        for (int i = 0; i < program.size(); i++) {
            System.out.println(i + ": " + program.get(i));
        }
        System.out.println("}");
    }
}
