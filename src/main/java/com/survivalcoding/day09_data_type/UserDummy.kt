package com.survivalcoding.day09_data_type

class UserDummy(var name: String, var email: String) {
    // 직렬화
    fun toJson(): String {
        return """
            {
                "name": $name",
                "email": $email"
            }
        """.trimIndent()
    }

    companion object {
        // 역직렬화
        fun fromJson(json: String): User {
            // 간단한 파싱 (실제로는 정규식이나 파서 사용 권장)
            val name = json.substringAfter("\"name\": \"")
                .substringBefore("\"")

            val email = json.substringAfter("\"email\": \"")
                .substringBefore("\"")

            return User(name, email)
        }
    }
}