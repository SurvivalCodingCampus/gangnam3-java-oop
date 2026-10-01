public abstract class TangibleAsset extends Asset implements Thing {
    // 자산이며 물리적 실체가 있음
    String color;
    double weight;

    @Override
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
