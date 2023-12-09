import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");

//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter addStationSize:");
//        int addStationSize = scanner.nextInt();
//
//        System.out.println("Enter mulStationSize:");
//        int mulStationSize = scanner.nextInt();
//
//        System.out.println("Enter loadBufferSize:");
//        int loadBufferSize = scanner.nextInt();
//
//        System.out.println("Enter storeBufferSize:");
//        int storeBufferSize = scanner.nextInt();
//
//        System.out.println("Enter addLatency:");
//        int addLatency = scanner.nextInt();
//
//        System.out.println("Enter subLatency:");
//        int subLatency = scanner.nextInt();
//
//        System.out.println("Enter mulLatency:");
//        int mulLatency = scanner.nextInt();
//
//        System.out.println("Enter divLatency:");
//        int divLatency = scanner.nextInt();
//
//        System.out.println("Enter loadLatency:");
//        int loadLatency = scanner.nextInt();
//
//        System.out.println("Enter storeLatency:");
//        int storeLatency = scanner.nextInt();
//
//        System.out.println("Enter SUBILatency:");
//        int SUBILatency = scanner.nextInt();
//
//        System.out.println("Enter DADDLatency:");
//        int DADDLatency = scanner.nextInt();
//
//        System.out.println("Enter DSUBLatency:");
//        int DSUBLatency = scanner.nextInt();

//        Tomasulo tomasulo = Tomasulo.getInstance(addStationSize, mulStationSize, loadBufferSize, storeBufferSize, addLatency, subLatency, mulLatency, divLatency, loadLatency, storeLatency, SUBILatency, DADDLatency, DSUBLatency); // Create a new instance of the tomasulo simulator
        Tomasulo tomasulo = Tomasulo.getInstance(); // Create a new instance of the tomasulo simulator
        tomasulo.init();
        tomasulo.run();

        System.out.println(Arrays.toString(tomasulo.cache));
        RegisterFile.printRegisterFile();
    }


}

