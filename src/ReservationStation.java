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
}
