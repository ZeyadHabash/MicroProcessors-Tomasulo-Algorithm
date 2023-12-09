public class Register{
    private String label;
    private String Qi;
    private Double value;

    public Register(String label, Double value) {
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
        // if there was already a tag in Qi then decrement the use count of that reservation station row
        if (!this.isReady()) {
            Tomasulo tomasulo = Tomasulo.getInstance();
            tomasulo.decrementUseCount(this.Qi);
        }
        Qi = qi;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
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
