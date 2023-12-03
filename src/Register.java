public class Register {
    private String label;
    private String Qi;
    private double value;

    public Register(String label, double value) {
        this.label = label;
        this.Qi = "0";
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getQi() {
        return Qi;
    }

    public void setQi(String qi) {
        Qi = qi;
    }

    public double getValue() {
        return value;
    }

    public void setValue(float value) {
        if (this.label.equals("R0"))
            return;
        this.value = value;
    }

    public boolean isReady() {
        return this.Qi.equals("0");
    }

    public void reset() {
        this.Qi = "0";
    }

    public String toString(){
        return this.label + "\t" + this.Qi + "\t" + this.value;
    }

}
