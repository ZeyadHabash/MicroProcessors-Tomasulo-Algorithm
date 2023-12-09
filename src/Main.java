import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Tomasulo tomasulo = Tomasulo.getInstance(); // Create a new instance of the tomasulo simulator
        tomasulo.init();
        tomasulo.run();

        System.out.println(Arrays.toString(tomasulo.cache));
        RegisterFile.printRegisterFile();
    }


}

