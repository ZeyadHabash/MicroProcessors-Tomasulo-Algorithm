public class RegisterFile {
    private static final int NUM_REGISTERS = 64;
    private static Register[] registerFile = new Register[64]; // Register file

    public static void initRegisterFile() {
        // Initialize the register file
        for (int i = 0; i < 32; i++) {
            registerFile[i] = new Register("R" + i, (double) (i * 2));
        }
        for (int i = 32; i < 64; i++) {
            registerFile[i] = new Register("F" + (i - 32), i * 2.5);
        }
    }

    public static Register getRegister(String label) {
        for (int i = 0; i < registerFile.length; i++) {
            if (registerFile[i].getLabel().equals(label)) {
                return registerFile[i];
            }
        }
        return null;
    }

    public static Register getRegister(int index) {
        return registerFile[index];
    }

    public static void printRegisterFile() {
        System.out.println("Register File:");
        System.out.println("Register\tQi\tValue");
        for (int i = 0; i < registerFile.length; i++) {
            System.out.println(registerFile[i].toString());
        }
    }

    public static int getNumRegisters() {
        return NUM_REGISTERS;
    }
}
