package com.survivalcoding.kotlin_basic

// equals, hashcode, toString, 얕은 copy
class Book(
    name: String,
    price: Int,
    color: String,
    var isbn: String,
    weight: Double,
) : TangibleAsset(name, price, color, weight)

class Computer(
    name: String,
    price: Int,
    color: String,
    var makerName: String,
    weight: Double,
) : TangibleAsset(name, price, color, weight)

abstract class TangibleAsset(
    name: String,
    price: Int,
    var color: String,
    override var weight: Double,
) : Asset(name, price), Thing

abstract class Asset(
    var name: String,
    var price: Int,
)

interface Thing {
    var weight: Double
}