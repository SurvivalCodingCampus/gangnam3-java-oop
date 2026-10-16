package com.survivalcoding.day13_file

class MyException(message: String): Exception(message) {

}

fun main() {

    throw MyException("Something went wrong")
}