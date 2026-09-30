package Jasan;

public class Computer extends TangibleAsset {
    private String name;
    private int price;
    private String color;
    private String makername;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMakername() {
        return makername;
    }

    public void setMakername(String makername) {
        this.makername = makername;
    }
}
