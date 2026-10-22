package com.survivalcoding.day15.datasourceImpl

import com.survivalcoding.day15.datasource.StockDataSource
import com.survivalcoding.day15.model.StockListing
import kotlin.io.path.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.fileSize

class CsvStockDataSource : StockDataSource {
    private val path = Path("src/main/resources/listing_status.csv")

    /**
     * 파일 경로에서 CSV 데이터를 읽어와 주식 목록([StockListing]) 리스트로 변환한다
     * 항상 첫 번째 줄(헤더)을 제외한다
     *
     * @return 파싱된 [StockListing] 객체들의 불변 리스트 (파일이 비어있을 경우 빈 리스트 반환)
     */
    override fun loadStockListings(): List<StockListing> {

        val fileSize = path.fileSize()

        // 파일 크기를 바탕으로 대략적인 행 개수 추정 (최소 10개)
        val estimatedSize = (fileSize / 64).toInt().coerceAtLeast(10)

        // 초기 용량을 미리 할당한 ArrayList 생성
        val stockListings: MutableList<StockListing> = ArrayList(estimatedSize)

//        ver 1
//
//        val rows = if (rawRows.last().isBlank()) {
//            rawRows.drop(1).dropLast(1)
//        } else {
//            rawRows.drop(1)
//        }
//        return buildList(rows.size) {
//            rows.forEach { row ->
//                val fields = row.split(',')
//
//                add(
//                    StockListing(
//                        symbol = fields[0],
//                        name = fields[1],
//                        exchange = fields[2],
//                        assetType = fields[3],
//                        ipoData = fields[4],
//                        status = fields[5],
//                    )
//                )
//            }
//        }

//        ver 2
//
//        path.useLines { lines ->
//            lines.drop(1)
//                .filter { it.isNotBlank() }
//                .map { row ->
//                    val fields = row.split(',')
//
//                    StockListing(
//                        symbol = fields[0],
//                        name = fields[1],
//                        exchange = fields[2],
//                        assetType = fields[3],
//                        ipoData = fields[4],
//                        status = fields[5],
//                    )
//                }
//                .toList()
//        }

        path.bufferedReader().use { reader ->
            val firstLine = reader.readLine()

            // 빈파일이다
            if (firstLine == null) {
                return emptyList()
            }

            while (true) {
                val line = reader.readLine()

                // EOF
                if (line == null) {
                    break
                }

                val fields = line.split(',')

                stockListings.add(
                    StockListing(
                        symbol = fields[0],
                        name = fields[1],
                        exchange = fields[2],
                        assetType = fields[3],
                        ipoData = fields[4],
                        delistingDate = fields[5],
                        status = fields[6],
                    )
                )
            }
        }

        return stockListings
    }
}