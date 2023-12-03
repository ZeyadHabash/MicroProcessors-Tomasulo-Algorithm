public class ReservationStationRow {
    boolean busy;
    private String tag;
    private String operation;
    private double Vj;
    private double Vk;
    private String Qj;
    private String Qk;
    private int A; // Address to load/store
    private int useCount; // counts how many other instructions are waiting on this result (used to determine write back priority)

    public ReservationStationRow(String tag) {
        this.tag = tag; // if add then A[num], if mul then M1, if load then L1, if store then S1, etc.
        this.busy = false;
        this.operation = "";
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

    public double getVj() {
        return Vj;
    }

    public void setVj(double vj) {
        Vj = vj;
    }

    public double getVk() {
        return Vk;
    }

    public void setVk(double vk) {
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

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public void incrementUseCount() {
        this.useCount++;
    }

    public void decrementUseCount() {
        this.useCount--;
    }

    public String toString() {
        String result = "";
//        result += "Tag: " + this.tag + " | ";
//        result += "Busy: " + this.busy + " | ";
//        result += "Operation: " + this.operation + " | ";
//        if (!(this.operation.equals("L.D") || this.operation.equals("S.D"))) {
//            result += "Vj: " + this.Vj + " | ";
//            if (!(this.operation.equals("S.D"))) result += "Vk: " + this.Vk + " | ";
//            result += "Qj: " + this.Qj + " | ";
//            if (!(this.operation.equals("S.D"))) result += "Qk: " + this.Qk + " | ";
//        }
//        result += "A: " + this.A + " | ";
//        result += "Use Count: " + this.useCount + " | ";
        result += this.tag + "\t";
        result += this.busy + "\t";
        if (!(this.tag.charAt(0) == 'L')) {
            if (!(this.tag.charAt(0) == 'S')) result += this.operation + "\t";
            result += this.Vj + "\t";
            if (!(this.tag.charAt(0) == 'S')) result += this.Vk + "\t";
            result += this.Qj + "\t";
            if (!(this.tag.charAt(0) == 'S')) result += this.Qk + "\t";
        }
        result += this.A + "\t";
        return result;
    }
}
