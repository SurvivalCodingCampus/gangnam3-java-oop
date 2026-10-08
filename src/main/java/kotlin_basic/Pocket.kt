#!/usr/bin/env kotlin
package kotlin_basic

// Pocket<E:Book> E type 이 Book 을 상속받은 놈만 쓰겠다. 그러면 int 은 안된다라는것을 추론 가능하다
enum class KeyType(var x: Int, var y: Int = 0) {
    padlock(1024), button(10000), dial(30000), finger(1000000)
}

class StrongBox<T : KeyType> {
    var innercontent = mutableListOf<T>()
    fun put(x: T) {
        if (innercontent.size < 1) {
            innercontent.add(x)
            println("${x}가 성공적으로 입력되었습니다")
        } else {
            println("이미 금고가 꽉 찼습니다")
        }
    }

    fun get(): T? {
        if (innercontent.size == 0) {
            println("금고가 비었습니다")
            return null
        } else if (innercontent[0].x < 0) {
            println("이미 열린 금고입니다")
            return null
        } else if (innercontent[0].x > 0) {
            innercontent[0].x = innercontent[0].x - 1
            innercontent[0].y = innercontent[0].y + 1;
            println("사용회수는 ${innercontent[0].y}회입니다")
            return null
        } else {
            innercontent[0].x = innercontent[0].x - 1
            println("금고가 열렸습니다")
            return null
        }
        return innercontent[0]
    }

}

fun main() {
    val box = StrongBox<KeyType>()
    box.get()
    box.put(KeyType.padlock)
    box.get()
    box.get()
    box.get()
    repeat(1025) { box.get() }


}//////////