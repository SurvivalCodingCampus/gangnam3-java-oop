package kotlin_basic

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

suspend fun A(i: Int) {
    var ii = i
    if (ii < 5) {
        println("꾸욱")
        delay(1000)
        ii++

    }
}

suspend fun B(i: Int) {
    var ii = i
    if (ii < 5) println("까욱")
    delay(2000)


}

suspend fun C(i: Int) {
    var ii = i
    if (ii < 5) println("쨱쨱")
    delay(3000)
}

suspend fun main() {
    runBlocking {
        A(0)
        B(0)
        C(0)
    }
}