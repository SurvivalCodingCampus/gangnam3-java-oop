package kotlin_basic

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

suspend fun A(i: Int) {
    var ii = i
    while (true) {
        println("꾸욱")
        delay(1000)
        ii++

    }
}

suspend fun B(i: Int) {
    var ii = i
    while (true) {
        println("까욱")
        delay(2000)
    }

}

suspend fun C(i: Int) {
    var ii = i
    while (true) {
        println("쨱쨱")
        delay(3000)
    }
}

suspend fun main() {

    runBlocking {
        withTimeoutOrNull(10000) {
            async { A(0) }
            async { B(0) }
            async { C(0) }
        }
    }
    println("boom")
}
//are you alive