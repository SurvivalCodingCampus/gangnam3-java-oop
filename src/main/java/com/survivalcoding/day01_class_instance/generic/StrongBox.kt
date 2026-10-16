package com.survivalcoding.day01_class_instance.generic

enum class KeyType {
    PADLOCK, BUTTON, DIAL, FINGER
}

class StrongBox<E>(val keyType: KeyType) { //캡슐화 val keyType: KeyType 이라고 할경우 1. 생략 가능
    private var _data: E? = null

    // private val _keyType: KeyType = keyType // 1. 생략 가능
    private var _count: Int = 0

    fun put(data: E) {
        _data = data
    }

    fun get(): E? {
        _count += 1

        val requiredCount = when (keyType) {
            KeyType.PADLOCK -> 1_024
            KeyType.BUTTON -> 10_000
            KeyType.DIAL -> 30_000
            KeyType.FINGER -> 1_000_000
        }

        return if (_count < requiredCount) {
            null
        } else {
            _data
        }
    }
}

//        when (_keyType) {
//            KeyType.PADLOCK -> {
//                if (_count < 1024) {
//                    return null
//                }
//            }
//
//            KeyType.BUTTON -> {
//                if (_count < 10000) {
//                    return null
//                }
//            }
//
//            KeyType.DIAL -> {
//                if (_count < 30000) {
//                    return null
//                }
//            }
//
//            KeyType.FINGER -> {
//                if (_count < 1000000) {
//                    return null
//                }
//            }
//        }
//
//        return _data
//    }
//}

fun main() {
    val box: StrongBox<String> = StrongBox<String>(KeyType.PADLOCK)
    box.put("열림")

    var result: String? = null
    repeat(1025) {
        result = box.get()
    }
    println(result)

    result = box.get()
    println(result)
}