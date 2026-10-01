package Jasan;

public class Computer extends TangibleAsset {
    private String makername;


    public String getMakername() {
        return makername;
    }

    public void setMakername(String makername) {
        this.makername = makername;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public void setWeight(double weight) {

    }
}
