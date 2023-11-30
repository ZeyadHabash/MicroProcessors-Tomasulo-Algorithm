import java.util.ArrayList;

public class Main {
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


    public static void main(String[] args) {
        System.out.println("Hello world!");
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
        addReservationStation = new ReservationStation(addStationSize, "A");
        mulReservationStation = new ReservationStation(mulStationSize, "M");
        loadBuffer = new ReservationStation(loadBufferSize, "L");
        storeBuffer = new ReservationStation(storeBufferSize, "S");

        // Initialize the instruction queue
        instructionQueue = new InstructionQueue();

        // Parse the input file
        program = CodeParser.readFile(fileName);

    }

    public void run() {
        // Update the current cycle number
        currentCycle++;

        // get instruction from program
        Instruction instruction = program.get(pc);

        // issue
        if (instruction.getOperation().equals("L.D")){
            if (loadBuffer.isFull()){
                return;
            }
        }
        else if (instruction.getOperation().equals("S.D")){
            if (storeBuffer.isFull()){
                return;
            }
        }
        else if (instruction.getOperation().equals("MUL.D") || instruction.getOperation().equals("DIV.D")){
            if (mulReservationStation.isFull()){
                return;
            }
        }
        else {
            if (addReservationStation.isFull()){
                return;
            }
        }
        issue(instruction);



        // Read from text file


        // Execute


    }

    public void issue(Instruction instruction) {

    }

}

