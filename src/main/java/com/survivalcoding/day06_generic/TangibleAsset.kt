package com.survivalcoding.day06_generic

abstract class TangibleAsset(
    name: String,
    color: String,
    price: Int,
    weight: Double
) : Asset(name, color, price), Thing {

    init {
        validateWeight(weight)
    }

    override var weight: Double = weight
        set(value) {
            validateWeight(value)
            field = value
        }

    private fun validateWeight(weight: Double) {
        require(!weight.isNaN() && weight >= Thing.MIN_WEIGHT && weight <= Thing.MAX_WEIGHT) {
            "설정할 무게는 ${Thing.MIN_WEIGHT} 이상 ${Thing.MAX_WEIGHT} 이하입니다"
        }
    }
}
