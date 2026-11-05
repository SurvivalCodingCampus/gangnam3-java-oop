package com.survivalcoding.day17_result.respository

import com.survivalcoding.day17_result.core.NetworkError
import com.survivalcoding.day17_result.core.Result
import com.survivalcoding.day17_result.data_source.ErrorPokeDataSource
import com.survivalcoding.day17_result.data_source.FakePokeDataSource
import com.survivalcoding.day17_result.dto.PokeDto
import com.survivalcoding.day17_result.repository.PokeRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.net.SocketException

class PokeRepositoryImplTest {

    @Test
    fun `포켓몬 검색 성공 시 Success와 데이터가 반환된다`() = runTest {
        // given
        val repository = PokeRepositoryImpl(FakePokeDataSource(statusCode = 200, body = PokeDto(name = "ditto")))

        // when
        val result = repository.getPoke("ditto")

        // then
        assertTrue(result is Result.Success)
        assertEquals("ditto", (result as Result.Success).data.name)
    }

    @Test
    fun `네트워크 연결 실패 시 Error와 NO_INTERNET이 반환된다`() = runTest {
        // given
        val repository = PokeRepositoryImpl(ErrorPokeDataSource(SocketException()))

        // when
        val result = repository.getPoke("ditto")

        // then
        assertTrue(result is Result.Error)
        assertEquals(NetworkError.NO_INTERNET, (result as Result.Error).error)
    }

    @Test
    fun `존재하지 않는 포켓몬 검색 시 CLIENT_ERROR가 반환된다`() = runTest {
        // given
        val repository = PokeRepositoryImpl(FakePokeDataSource(statusCode = 404, body = null))

        // when
        val result = repository.getPoke("dittooo")

        // then
        assertTrue(result is Result.Error)
        assertEquals(NetworkError.CLIENT_ERROR, (result as Result.Error).error)
    }
}