package com.survivalcoding.day17_result

import com.survivalcoding.day17_result.core.NetworkError
import com.survivalcoding.day17_result.core.Result
import com.survivalcoding.day17_result.data_source.PokeDataSource
import com.survivalcoding.day17_result.data_source.PokeDataSourceImpl
import com.survivalcoding.day17_result.repository.PokeRepository
import com.survivalcoding.day17_result.repository.PokeRepositoryImpl
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val dataSource: PokeDataSource = PokeDataSourceImpl()
    val repository: PokeRepository = PokeRepositoryImpl(dataSource)

    when (val result = repository.getPoke("ditto")) {
        is Result.Success -> {
            println("성공: 포켓몬 이름 = ${result.data}")
        }

        is Result.Error -> {
            // println("Error: ${result.error}")
            when (result.error) {
                NetworkError.NO_INTERNET -> println("[네트워크 오류] 와이파이 연결을 확인해주세요.")
                NetworkError.CLIENT_ERROR -> println("[요청 오류] 존재하지 않는 포켓몬입니다.")
                NetworkError.SERVER_ERROR -> println("[서버 오류] 서버 점검 중입니다.")
                NetworkError.TIMEOUT -> println("[지연 오류] 응답 시간이 초과되었습니다.")
                NetworkError.UNKNOWN -> println("[기타 오류] 알 수 없는 오류가 발생했습니다.")
            }
        }
    }
}
