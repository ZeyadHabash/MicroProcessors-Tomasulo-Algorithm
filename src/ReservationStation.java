public class ReservationStation {
    String name;
    int size;
    ReservationStationRow[] rows;

    public ReservationStation(String name, int size, String tag) {
        this.name = name;
        this.size = size;
        this.rows = new ReservationStationRow[size];
        for (int i = 0; i < size; i++) {
            this.rows[i] = new ReservationStationRow(tag + i);
        }
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            if (!rows[i].isBusy()) {
                return false;
            }
        }
        return true;
    }

    public String issueInstruction(String operation, Double Vj, Double Vk, String Qj, String Qk, int A, Instruction instruction) {
        for (int i = 0; i < size; i++) {
            if (!rows[i].isBusy()) {
                rows[i].setBusy(true);
                rows[i].setOperation(operation);
                rows[i].setVj(Vj);
                rows[i].setVk(Vk);
                rows[i].setQj(Qj);
                rows[i].setQk(Qk);
                rows[i].setA(A);
                rows[i].setInstruction(instruction);
                // increment the use count if instruction has a destination register
                if (!(operation.equals("S.D") || operation.equals("BNEZ")))
                    rows[i].incrementUseCount();
                return rows[i].getTag();
            }
        }
        return "";
    }

    public void print() {
        if (name.equals("load")) {
            System.out.println("Tag\tBusy\tAddress\tResult");
        } else if (name.equals("store")) {
            System.out.println("Tag\tBusy\tV\tQ\tA\tResult");
        } else
            System.out.println("Tag\tBusy\tOp\tVj\tVk\tQj\tQk\tA\tResult");
        for (int i = 0; i < size; i++) {
            System.out.println(rows[i].toString());
        }
    }
}
