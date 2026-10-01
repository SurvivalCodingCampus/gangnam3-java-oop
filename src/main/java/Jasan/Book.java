package Jasan;

public class Book extends TangibleAsset {
    private String isbn;

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

