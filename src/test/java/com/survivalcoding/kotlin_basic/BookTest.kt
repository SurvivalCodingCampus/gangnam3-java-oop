@file:Suppress("NonAsciiCharacters")

package com.survivalcoding.kotlin_basic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BookTest {

    @Test
    fun `Book 은 TangibleAsset 이다`() {
        // ctrl + space
        val book: TangibleAsset = Book(
            name = "생존코딩",
            price = 1000,
            color = "red",
            isbn = "1111",
            weight = 30.0,
        )

        assertEquals("생존코딩", book.name)
    }
}