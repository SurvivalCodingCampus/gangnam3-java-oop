package Jasan;

public abstract class Computer extends TangibleAsset {
    private String makername;


    public String getMakername() {
        return makername;
    }

    public void setMakername(String makername) {
        this.makername = makername;
    }
}
