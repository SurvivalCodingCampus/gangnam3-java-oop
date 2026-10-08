package com.survivalcoding.generic_enum_string

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time
import kotlin.system.measureTimeMillis

fun main() {
    var name: String = "홍길동"
    name += "만세"

    // for문 44s
    var time = measureTimeMillis {
        repeat(1_000_000) {
            name += "!"
        }
    }

    // 599ms
//    time = measureTimeMillis {
//        val sb = StringBuilder(name)
//        repeat(1_000_000) {
//            sb.append("!")
//        }
//
//        println(sb.toString())
//    }

    println(time)
}