package t;


public abstract class TangibleAssest extends Asset implements Thing {
    double weight;

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
     public void setWeight(double weight) {
        this.weight=weight;
    }


    ;
}
