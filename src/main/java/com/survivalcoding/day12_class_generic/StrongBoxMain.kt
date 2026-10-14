package com.survivalcoding.day12_class_generic

fun main() {
    for (keyType in KeyType.entries) {
        val box = StrongBox<String>(keyType)
        box.put("보물") // 보물?? 이겠다...

        val requiredAttempts = keyType.requiredAttempts
        println("=== $keyType: ${requiredAttempts}회에 잠금 해제 ===")

        for (attempt in 1..requiredAttempts) {
            // 반복할 때마다 실제로 get() 호출
            val result: String? = box.get()

            // 모든 시도를 수행하고 처음, 해제 직전, 해제 시점만 계속 출력하기!
            if (attempt == 1 || attempt == requiredAttempts - 1 || attempt == requiredAttempts) {
                println("${attempt}번째 시도: $result")
            }
        }

        // 잠금이 해제된 금고는 계속 열린 채로 두면될까?
        println("잠금 해제 후 다시 꺼내기: ${box.get()}")
        println()
    }
}
