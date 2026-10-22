package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.datasource.FakeItemDataSource
import com.survivalcoding.day13_model_repository.model.Item
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ItemRepositoryImplTest {

    private lateinit var fakeDataSource: FakeItemDataSource
    private lateinit var repository: ItemRepository

    @BeforeEach
    fun setUp() {
        fakeDataSource = FakeItemDataSource()
        fakeDataSource.saveItems(
            listOf(
                Item(id = 1L, name = "기계식 키보드", price = 120_000),
                Item(id = 2L, name = "무선 마우스", price = 89_000),
                Item(id = 3L, name = "27인치 모니터", price = 350_000),
            )
        )

        repository = ItemRepositoryImpl(fakeDataSource)
    }

    @Test
    fun `지정한 금액 이하의 아이템만 조회된다`() {
        val affordableItems = repository.getItemsUnderPrice(100_000)

        assertEquals(1, affordableItems.size)
        assertEquals("무선 마우스", affordableItems.first().name)
    }
}