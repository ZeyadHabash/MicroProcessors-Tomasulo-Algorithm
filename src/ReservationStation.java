public class ReservationStation {
    int size;
    ReservationStationRow[] rows;

    public ReservationStation(int size, String tag) {
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

    public void issueInstruction(String operation, double Vj, double Vk, String Qj, String Qk, int A) {
        for (int i = 0; i < size; i++) {
            if (!rows[i].isBusy()) {
                rows[i].setBusy(true);
                rows[i].setOperation(operation);
                rows[i].setVj(Vj);
                rows[i].setVk(Vk);
                rows[i].setQj(Qj);
                rows[i].setQk(Qk);
                rows[i].setA(A);
                rows[i].incrementUseCount(); // TODO: decrement usecount when writing to register
                return;
            }
        }
    }
}
