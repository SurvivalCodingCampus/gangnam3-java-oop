public abstract class Asset {
    String name; 	// 식별가능함 조건1
    int unit;	    // 식별가능한 조건2
    int price;	    // 미래 경제적 효익을 창출

    //getter,setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

