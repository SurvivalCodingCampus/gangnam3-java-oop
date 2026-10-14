package com.survivalcoding.day13

class MyException(message: String): Exception(message) {

}

fun main() {

    throw MyException("Something went wrong")
}