public class Main {
    int currentCycle = 0; // Current clock cycle number
    double[] cache = new double[1024]; // Cache memory
    Register[] registerFile = new Register[64]; // Register file
    ReservationStation addReservationStation;
    ReservationStation mulReservationStation;
    ReservationStation loadBuffer;
    ReservationStation storeBuffer;
    InstructionQueue instructionQueue;


    // Sizes of the reservation stations and buffers
    int addStationSize = 3;
    int mulStationSize = 2;
    int loadBufferSize = 3;
    int storeBufferSize = 3;

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public void init() {
        // Initialize the cache
        for (int i = 0; i < 1024; i++) {
            cache[i] = i * 1.5;
        }

        // Initialize the register file
        for (int i = 0; i < 32; i++) {
            registerFile[i] = new Register("R" + i, i * 2);
        }
        for (int i = 32; i < 64; i++) {
            registerFile[i] = new Register("F" + (i - 32), i * 2.5);
        }

        // Initialize the reservation stations and buffers
        addReservationStation = new ReservationStation(addStationSize, "A");
        mulReservationStation = new ReservationStation(mulStationSize, "M");
        loadBuffer = new ReservationStation(loadBufferSize, "L");
        storeBuffer = new ReservationStation(storeBufferSize, "S");

        instructionQueue = new InstructionQueue();
    }
}