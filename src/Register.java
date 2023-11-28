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

    public void setLabel(String label) {
        this.label = label;
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
        this.value = value;
    }
}
