package com.survivalcoding.day14

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.readText



@Serializable
data class CollectionChartData(
    val collectionChartDataList: List<CollectionChart>
)

@Serializable
data class CollectionChart(
    val collectionName: String,
    val collectionSalePrice: List<Collection> = emptyList()
)

// Kotlinx Serialization에서 LocalDateTime을 문자열(String)과 상호 변환하기 위한 커스텀 시리얼라이저
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    // "2023-03-26T08:21:40" 형식(ISO-8601 로컬 날짜/시간)을 처리하기 위한 포맷터 정의
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // 이 데이터가 직렬화될 때 어떤 타입과 이름으로 다뤄질지 정의 (JSON상에서 String으로 취급됨)
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    // 코틀린의 LocalDateTime 객체를 받아 지정된 포맷의 문자열로 변환하여 인코더에 전달
    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    // JSON에 기록된 문자열을 읽어와서 지정된 포맷에 맞춰 코틀린 LocalDateTime 객체로 파싱 후 반환
    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}

@Serializable
data class Collection(
    val price: Double,
    @Serializable(with = LocalDateTimeSerializer::class)
    val cvtDatetime: LocalDateTime
)

fun main() {
    val dataPath = Path("src/main/resources/chart_data.json")
    val dataStr = dataPath.readText()

    val collectionChartData
        = Json.decodeFromString<CollectionChartData>(dataStr)

    // Exception in thread "main" kotlinx.serialization.MissingFieldException: Field 'collectionSalePrice' is required
    // for type with serial name 'com.survivalcoding.day14.CollectionChart'
    // , but it was missing at path: $.collectionChartDataList[67]

    // collectionSalePrice 필드가 필수값(반드시 존재해야 함)으로 지정되어 있는데 들어온 JSON에 이 필드가 없다
    // 에러가 발생한 코틀린 데이터 클래스의 이름이 CollectionChart
    // collectionChartDataList 인덱스[67] 배열 요소의 collectionChartDataList 안쪽 데이터를 파싱(역직렬화)하던 중에 문제가 발생

    // JSON collectionSalePrice 필드가 없음
    // 기본값을 emptyList()를 통해 해결 nullable이 좋을까요?
    // val collectionSalePrice: List<Collection> = emptyList()
    // {
    //     "collectionName": "collection68"
    // },


    collectionChartData.collectionChartDataList.forEach(::println)
}