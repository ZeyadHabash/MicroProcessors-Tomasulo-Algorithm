public class ReservationStationRow {
    private String tag;
    boolean busy;
    private String operation;
    private float Vj;
    private float Vk;
    private String Qj;
    private String Qk;
    private int A; // Address to load/store
    private int useCount; // counts how many other instructions are waiting on this result (used to determine write back priority)

    public ReservationStationRow(String tag) {
        this.tag = tag; // if add then A[num], if mul then M1, if load then L1, if store then S1, etc.
        this.busy = false;
        this.operation = null;
        this.Vj = 0;
        this.Vk = 0;
        this.Qj = "";
        this.Qk = "";
        this.A = 0;
        this.useCount = 0;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public float getVj() {
        return Vj;
    }

    public void setVj(float vj) {
        Vj = vj;
    }

    public float getVk() {
        return Vk;
    }

    public void setVk(float vk) {
        Vk = vk;
    }

    public String getQj() {
        return Qj;
    }

    public void setQj(String qj) {
        Qj = qj;
    }

    public String getQk() {
        return Qk;
    }

    public void setQk(String qk) {
        Qk = qk;
    }

    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }

    public int getUseCount() {
        return useCount;
    }

    public void incrementUseCount() {
        this.useCount++;
    }
}
