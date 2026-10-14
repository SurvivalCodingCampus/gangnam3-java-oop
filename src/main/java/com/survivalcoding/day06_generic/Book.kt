package com.survivalcoding.day06_generic

class Book(
    val isbn: String,
    name: String,
    color: String,
    price: Int,
    weight: Double
) : TangibleAsset(name, color, price, weight)
