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
    int storeLatency = 5;
    int SUBILatency = 1;
    int DADDLatency = 1;
    int DSUBLatency = 1;

    boolean branchStall = false; // Stall due to branch
    boolean rsStall = false; // Stall due to reservation station being full

    // Constructors
    private Tomasulo(int addStationSize, int mulStationSize, int loadBufferSize, int storeBufferSize, int addLatency, int subLatency, int mulLatency, int divLatency, int loadLatency, int storeLatency, int SUBILatency, int DADDLatency, int DSUBLatency) {
        this.addStationSize = addStationSize;
        this.mulStationSize = mulStationSize;
        this.loadBufferSize = loadBufferSize;
        this.storeBufferSize = storeBufferSize;
        this.addLatency = addLatency;
        this.subLatency = subLatency;
        this.mulLatency = mulLatency;
        this.divLatency = divLatency;
        this.loadLatency = loadLatency;
        this.storeLatency = storeLatency;
        this.SUBILatency = SUBILatency;
        this.DADDLatency = DADDLatency;
        this.DSUBLatency = DSUBLatency;
    }

    private Tomasulo() {
        // Exists only to defeat instantiation.
    }

    // Singleton functions
    public static Tomasulo getInstance(int addStationSize, int mulStationSize, int loadBufferSize, int storeBufferSize, int addLatency, int subLatency, int mulLatency, int divLatency, int loadLatency, int storeLatency, int SUBILatency, int DADDLatency, int DSUBLatency) {
        if (instance == null) {
            instance = new Tomasulo(addStationSize, mulStationSize, loadBufferSize, storeBufferSize, addLatency, subLatency, mulLatency, divLatency, loadLatency, storeLatency, SUBILatency, DADDLatency, DSUBLatency);
        }
        return instance;
    }

    public static Tomasulo getInstance() {
        if (instance == null) {
            instance = new Tomasulo();
        }
        return instance;
    }

    // initialization function
    public void init() {
        // TODO: Initialize latencies through user input

        // TODO: Initialize RS and Buffer sizes through user input\

        // Initialize the cache
        for (int i = 0; i < 1024; i++) {
//            cache[i] = i * 1.5;
            cache[i] = 0;
        }
        cache[20] = 5;

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

    // main function
    public void run() {
        // TODO: change later to handle stalls and branches
        while (pc < program.size() || isExecuting()) {
//        while (currentCycle < 25) {
            // Update the current cycle number
            currentCycle++;


            // if not branch stalling then:
            // - get instruction from program
            // - issue instruction
            if (!branchStall && pc < program.size()) {
                // get instruction from program
                Instruction instruction = program.get(pc).clone();

                // issue
                issue(instruction);
            }

            // TODO: change later to handle stalls and branches
            // increment program counter if not rs stalling or branch stalling
            if (!rsStall && !branchStall) pc++;

            // Execute
            execute();

            // Write result
            writeResult();

            // print
            print();

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
                rsStall = true;
                return;
            }

            // cast immediate value to int to get address
            address = (int) Math.round(immediateValue);

            tag = loadBuffer.issueInstruction(operation, Vj, Vk, Qj, Qk, address, instruction);
        } else if (operation.equals("S.D")) {
            if (storeBuffer.isFull()) {
                rsStall = true;
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

            tag = storeBuffer.issueInstruction(operation, Vj, Vk, Qj, Qk, address, instruction);
        } else if (operation.equals("MUL.D") || operation.equals("DIV.D")) {
            if (mulReservationStation.isFull()) {
                rsStall = true;
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

            tag = mulReservationStation.issueInstruction(operation, Vj, Vk, Qj, Qk, address, instruction);
        } else {
            if (addReservationStation.isFull()) {
                rsStall = true;
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

            tag = addReservationStation.issueInstruction(operation, Vj, Vk, Qj, Qk, address, instruction);
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

        // set branch stall to true if BNEZ
        if (operation.equals("BNEZ")) {
            branchStall = true;
        }
    }

    public void execute() {
        // check if any instruction is ready to execute
        // if so, execute
        // if not, return

        // loop through reservation stations and check if any are ready to execute
        // TODO: check priorities to know which to execute first in each reservation station
        for (int i = 0; i < addStationSize; i++) {
            if (addReservationStation.rows[i].isReadyToExecute()) {
                execute(addReservationStation.rows[i]);
            }
        }
        for (int i = 0; i < mulStationSize; i++) {
            if (mulReservationStation.rows[i].isReadyToExecute()) {
                execute(mulReservationStation.rows[i]);
            }
        }
        for (int i = 0; i < loadBufferSize; i++) {
            if (loadBuffer.rows[i].isReadyToExecute()) {
                execute(loadBuffer.rows[i]);
            }
        }
        for (int i = 0; i < storeBufferSize; i++) {
            if (storeBuffer.rows[i].isReadyToExecute()) {
                execute(storeBuffer.rows[i]);
            }
        }
    }

    public void writeResult() {
        ReservationStationRow row = getRowToWrite();
        if (row == null) {
            return;
        }

        row.getInstruction().setWriteResult(currentCycle);
        writeResult(row);
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

    public boolean isExecuting() {
        // check if all reservation stations and buffers are empty
        return !(addReservationStation.isEmpty() && mulReservationStation.isEmpty() && loadBuffer.isEmpty() && storeBuffer.isEmpty());
    }

    public void execute(ReservationStationRow row) {
        // check the operation of the instruction
        String operation = row.getOperation();
        Double Vj = row.getVj();
        Double Vk = row.getVk();
        int instructionLatency = 0;

        // if instruction was just issued this cycle return
        if (row.getInstruction().getIssue() == currentCycle)
            return;

        // add the execute cycle to the instruction
        if (row.getInstruction().getExecutionStart() == -1) {
            row.getInstruction().setExecutionStart(currentCycle);

            // set the execution end cycle
            switch (operation) {
                case "ADD.D":
                    instructionLatency = addLatency;
                    break;
                case "DADD":
                    instructionLatency = DADDLatency;
                    break;
                case "SUB.D":
                    instructionLatency = subLatency;
                    break;
                case "DSUB":
                    instructionLatency = DSUBLatency;
                    break;
                case "MUL.D":
                    instructionLatency = mulLatency;
                    break;
                case "DIV.D":
                    instructionLatency = divLatency;
                    break;
                case "L.D":
                    instructionLatency = loadLatency;
                    break;
                case "S.D":
                    instructionLatency = storeLatency;
                    break;
                case "ADDI":
                    instructionLatency = ADDILatency;
                    break;
                case "SUBI":
                    instructionLatency = SUBILatency;
                    break;
                case "BNEZ":
                    instructionLatency = BNEZLatency;
                    break;
                default:
                    break;
            }
            row.getInstruction().setExecutionEnd(currentCycle + instructionLatency - 1);
        }
        // execute the instruction if end cycle is reached
        if (currentCycle == row.getInstruction().getExecutionEnd()) {
            // check the operation of the instruction
            switch (operation) {
                case "ADD.D":
                case "DADD":
                case "ADDI":
                    row.setResult(Vj + Vk);
                    break;
                case "SUB.D":
                case "DSUB":
                case "SUBI":
                    row.setResult(Vj - Vk);
                    break;
                case "MUL.D":
                    row.setResult(Vj * Vk);
                    break;
                case "DIV.D":
                    row.setResult(Vj / Vk);
                    break;
                case "L.D":
                    row.setResult(cache[row.getA()]);
                    break;
//                case "S.D":
//                    cache[row.getA()] = Vj;
//                    break;
                case "BNEZ":
                    if (Vj != null && Vj != 0) {
                        pc = (int) Math.round(Vk);
                    } else {
                        pc++;
                    }
                    branchStall = false;
                    break;
                default:
                    break;
            }
        }
    }

    public void writeResult(ReservationStationRow row) {
        // loop through reservation stations and check if any are ready to write result


        //update the value of the destination register
        // loop over reservation stations and check if any are waiting on this tag
        // if so, decrement use count ?? todo
        //  add it to vj or vk and set qj or qk to empty
        // clear the reservation station row

        String tag = row.getTag();

        if (tag.charAt(0) == 'S') {
            //storing the value in the cache
            cache[row.getA()] = row.getVj();
        } else {
            //updating value in register file
            for (int i = 0; i < RegisterFile.getNumRegisters(); i++) {
                if (RegisterFile.getRegister(i).getQi().equals(tag)) {
                    RegisterFile.getRegister(i).setValue(row.getResult());
                    RegisterFile.getRegister(i).setQi("0");
                }
            }

            //updating reservation stations
            for (int i = 0; i < addStationSize; i++) {
                if (addReservationStation.rows[i].getQj().equals(tag)) {
                    addReservationStation.rows[i].setVj(row.getResult());
                    addReservationStation.rows[i].setQj("");
                }
                if (addReservationStation.rows[i].getQk().equals(tag)) {
                    addReservationStation.rows[i].setVk(row.getResult());
                    addReservationStation.rows[i].setQk("");
                }
            }
            for (int i = 0; i < mulStationSize; i++) {
                if (mulReservationStation.rows[i].getQj().equals(tag)) {
                    mulReservationStation.rows[i].setVj(row.getResult());
                    mulReservationStation.rows[i].setQj("");
                }
                if (mulReservationStation.rows[i].getQk().equals(tag)) {
                    mulReservationStation.rows[i].setVk(row.getResult());
                    mulReservationStation.rows[i].setQk("");
                }
            }
            for (int i = 0; i < storeBufferSize; i++) {
                if (storeBuffer.rows[i].getQj().equals(tag)) {
                    storeBuffer.rows[i].setVj(row.getResult());
                    storeBuffer.rows[i].setQj("");
                }
                if (storeBuffer.rows[i].getQk().equals(tag)) {
                    storeBuffer.rows[i].setVk(row.getResult());
                    storeBuffer.rows[i].setQk("");
                }
            }
        }
        //clearing the reservation station row
        row.clear();
    }

    public ReservationStationRow getRowToWrite() {
        //get the row with the highest priority to write back
        //write res everywhere and clear reseravation station
        int maxPriority = -1;
        Integer priority = null;
        ReservationStationRow rowToWrite = null;

        for (int i = 0; i < addStationSize; i++) {
            // if the instruction is BNEZ, and it has finished executing, write result and clear
            if (addReservationStation.rows[i].isBusy() &&
                    addReservationStation.rows[i].getOperation().equals("BNEZ") &&
                    addReservationStation.rows[i].getInstruction().getExecutionEnd() < currentCycle &&
                    addReservationStation.rows[i].getInstruction().getExecutionEnd() >= 0) {
                addReservationStation.rows[i].getInstruction().setWriteResult(currentCycle);
                addReservationStation.rows[i].clear();
                continue; // do not check priority as BNEZ does not write to bus
            }
            priority = addReservationStation.rows[i].isReadyToWriteRes();
            if (priority != null && addReservationStation.rows[i].getInstruction().getExecutionEnd() < currentCycle) {
                if (priority > maxPriority) {
                    rowToWrite = addReservationStation.rows[i];
                    maxPriority = addReservationStation.rows[i].isReadyToWriteRes();
                } else if (priority == maxPriority && rowToWrite != null &&
                        rowToWrite.getInstruction().getIssue() > addReservationStation.rows[i].getInstruction().getIssue()) {
                    rowToWrite = addReservationStation.rows[i];
                    maxPriority = addReservationStation.rows[i].isReadyToWriteRes();
                }
            }
        }

        for (int i = 0; i < mulStationSize; i++) {
            priority = mulReservationStation.rows[i].isReadyToWriteRes();
            if (priority != null && mulReservationStation.rows[i].getInstruction().getExecutionEnd() < currentCycle) {
                if (priority > maxPriority) {
                    rowToWrite = mulReservationStation.rows[i];
                    maxPriority = mulReservationStation.rows[i].isReadyToWriteRes();
                } else if (priority == maxPriority && rowToWrite != null &&
                        rowToWrite.getInstruction().getIssue() > mulReservationStation.rows[i].getInstruction().getIssue()) {
                    rowToWrite = mulReservationStation.rows[i];
                    maxPriority = mulReservationStation.rows[i].isReadyToWriteRes();
                }
            }
        }
        for (int i = 0; i < loadBufferSize; i++) {
            priority = loadBuffer.rows[i].isReadyToWriteRes();
            if (priority != null && loadBuffer.rows[i].getInstruction().getExecutionEnd() < currentCycle) {
                if (priority > maxPriority) {
                    rowToWrite = loadBuffer.rows[i];
                    maxPriority = loadBuffer.rows[i].isReadyToWriteRes();
                } else if (priority == maxPriority && rowToWrite != null &&
                        rowToWrite.getInstruction().getIssue() > loadBuffer.rows[i].getInstruction().getIssue()) {
                    rowToWrite = loadBuffer.rows[i];
                    maxPriority = loadBuffer.rows[i].isReadyToWriteRes();
                }
            }
        }
        for (int i = 0; i < storeBufferSize; i++) {
            priority = storeBuffer.rows[i].isReadyToWriteRes();
            if (priority != null && storeBuffer.rows[i].getInstruction().getExecutionEnd() < currentCycle) {
                if (priority > maxPriority) {
                    rowToWrite = storeBuffer.rows[i];
                    maxPriority = storeBuffer.rows[i].isReadyToWriteRes();
                } else if (priority == maxPriority && rowToWrite != null &&
                        rowToWrite.getInstruction().getIssue() > storeBuffer.rows[i].getInstruction().getIssue()) {
                    rowToWrite = storeBuffer.rows[i];
                    maxPriority = storeBuffer.rows[i].isReadyToWriteRes();
                }
            }
        }

//        // clear any store buffer row that has an instruction that has finished executing
//        for (int i = 0; i < storeBufferSize; i++) {
//            if (storeBuffer.rows[i].isBusy() &&
//                    storeBuffer.rows[i].getInstruction().getExecutionEnd() > 0 &&
//                    storeBuffer.rows[i].getInstruction().getExecutionEnd() < currentCycle) {
//                //storing the value in the cache
//                cache[storeBuffer.rows[i].getA()] = storeBuffer.rows[i].getVj();
//
//                storeBuffer.rows[i].getInstruction().setWriteResult(currentCycle);
//
//                storeBuffer.rows[i].clear();
//            }
//        }

        //setting maxPriority to 0 again for writing back in the next cycle
        return rowToWrite;

    }

    // Print functions
    public void print() {
//        System.out.println("Program: ");
//        printProgram();
        System.out.println("Stalls:");
        System.out.println("Branch Stall: " + branchStall);
        System.out.println("RS Stall: " + rsStall);
        System.out.println("Cycle: " + currentCycle);
        System.out.println("PC: " + pc);
//        RegisterFile.printRegisterFile();
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
