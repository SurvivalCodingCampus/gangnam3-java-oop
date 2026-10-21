package com.survivalcoding.`15day_dataSource`

import java.io.File

// 1. 주식 상장 정보를 담는 데이터 클래스
data class StockListing(
    val symbol: String,     // 티커 심볼 (예: AAPL)[cite: 3]
    val name: String,       // 회사명 (예: Apple Inc)[cite: 3]
    val exchange: String,   // 거래소 (예: NASDAQ, NYSE)[cite: 3]
    val assetType: String,  // 자산 종류 (예: Stock, ETF)[cite: 3]
    val ipoDate: String,    // 상장일 (예: 1980-12-12)[cite: 3]
    val status: String      // 상태 (예: Active)[cite: 3]
)

// 2. 주식 데이터를 불러오는 인터페이스
interface StockDataSource {
    fun loadStockListings(): List<StockListing>
}

// 3. CSV 파일을 읽어서 파싱하는 실제 구현체 클래스
class CsvStockDataSource : StockDataSource {

    // 읽어올 CSV 파일 지정 (루트에 저장된 listing_status.csv)[cite: 3]
    private val file = File("listing_status.csv")

    override fun loadStockListings(): List<StockListing> {
        // 파일이 존재하지 않으면 빈 리스트 반환
        if (!file.exists()) return emptyList()

        // 파일의 모든 줄을 읽어와서 리스트로 변환 후 데이터 가공
        return file.readLines()
            .drop(1)                    // 첫 번째 줄(컬럼 이름표/헤더)은 데이터가 아니므로 쿨하게 제외[cite: 3]
            .filter { it.isNotBlank() } // 마지막 줄 등에 들어있는 빈 줄(공백)은 안전하게 걸러냄[cite: 3]
            .map { line ->
                // 콤마(,)를 기준으로 글자를 쪼개서 각 칸의 데이터 추출
                val columns = line.split(",")

                // 쪼갠 순서(인덱스)대로 StockListing 상자에 예쁘게 담아줌
                StockListing(
                    symbol = columns.getOrElse(0) { "" },
                    name = columns.getOrElse(1) { "" },
                    exchange = columns.getOrElse(2) { "" },
                    assetType = columns.getOrElse(3) { "" },
                    ipoDate = columns.getOrElse(4) { "" },
                    status = columns.getOrElse(5) { "" }
                )
            }
    }
}

// 4. 실행 테스트를 위한 메인 함수
fun main() {
    // 구현체 인스턴스 생성
    val dataSource: StockDataSource = CsvStockDataSource()

    // 상장 종목 데이터 불러오기
    val stockList = dataSource.loadStockListings()
    println("총 로드된 상장 종목 수: ${stockList.size}개")

    // 상위 5개 종목 샘플 출력
    println("\n=== 상위 5개 종목 샘플 ===")
    stockList.take(5).forEach { stock ->
        println("[${stock.exchange}] ${stock.symbol} - ${stock.name} (${stock.assetType})")
    }
}