package com.survivalcoding.day15

import com.survivalcoding.day15.model.StockListing
import kotlin.io.path.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.fileSize
import kotlin.io.path.readLines
import kotlin.io.path.useLines
import kotlin.system.measureTimeMillis

//useLines 실행 시간 (100회 총합): 12ms
//while 문 실행 시간 (100회 총합): 13ms
//buildList 실행 시간 (100회 총합): 9ms
//----------------------------------------
//useLines 메모리 사용량: 5149 KB
//while 문 메모리 사용량: 2911 KB
//buildList 메모리 사용량: 2839 KB

fun main() {
    try {
        val iterations = 100 // 100번 반복 측정

        // 1. JIT 컴파일러 예열 (Warm-up)
        repeat(10) {
            loadUsingUseLines()
            loadUsingWhileLoop()
            loadUsingBuildList()
        }

        // 2. useLines (Sequence 기반) 측정
        val useLinesMemory = measureMemoryUsage {
            val useLinesTime = measureTimeMillis {
                repeat(iterations) {
                    loadUsingUseLines()
                }
            }
            println("useLines 실행 시간 (${iterations}회 총합): ${useLinesTime}ms")
        }

        // 3. while 문 (초기 용량 + 원시 제어) 측정
        val whileLoopMemory = measureMemoryUsage {
            val whileLoopTime = measureTimeMillis {
                repeat(iterations) {
                    loadUsingWhileLoop()
                }
            }
            println("while 문 실행 시간 (${iterations}회 총합): ${whileLoopTime}ms")
        }

        // 4. readLines + buildList 방식 측정
        val buildListMemory = measureMemoryUsage {
            val buildListTime = measureTimeMillis {
                repeat(iterations) {
                    loadUsingBuildList()
                }
            }
            println("buildList 실행 시간 (${iterations}회 총합): ${buildListTime}ms")
        }

        println("----------------------------------------")
        println("useLines 메모리 사용량: ${useLinesMemory} KB")
        println("while 문 메모리 사용량: ${whileLoopMemory} KB")
        println("buildList 메모리 사용량: ${buildListMemory} KB")

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// --- 비교 대상 1: useLines 방식 ---
private fun loadUsingUseLines(): List<StockListing> {
    val path = Path("src/main/resources/todo_data.json")
    return path.useLines { lines ->
        lines.drop(1)
            .filter { it.isNotBlank() }
            .map { row ->
                val fields = row.split(',')
                StockListing(
                    symbol = fields[0],
                    name = fields[1],
                    exchange = fields[2],
                    assetType = fields[3],
                    ipoData = fields[4],
                    status = fields[5],
                )
            }
            .toList()
    }
}

// --- 비교 대상 2: while 문 + 초기 용량 방식 ---
private fun loadUsingWhileLoop(): List<StockListing> {
    val path = Path("src/main/resources/todo_data.json")
    val fileSize = path.fileSize()
    if (fileSize == 0L) return emptyList()

    val estimatedSize = (fileSize / 64).toInt().coerceAtLeast(10)
    val stockListings = ArrayList<StockListing>(estimatedSize)

    path.bufferedReader().use { reader ->
        val firstLine = reader.readLine() ?: return emptyList()

        while (true) {
            val line = reader.readLine() ?: break
            if (line.isBlank()) continue

            val fields = line.split(',')

            stockListings.add(
                StockListing(
                    symbol = fields[0],
                    name = fields[1],
                    exchange = fields[2],
                    assetType = fields[3],
                    ipoData = fields[4],
                    status = fields[5],
                )
            )
        }
    }

    return stockListings
}

// --- 비교 대상 3: readLines + buildList 방식 ---
private fun loadUsingBuildList(): List<StockListing> {
    val path = Path("src/main/resources/todo_data.json")
    val rawRows = path.readLines()
    val rows = rawRows.drop(1).dropLast(1)

    return buildList(rows.size) {
        rows.forEach { row ->
            val fields = row.split(',')

            add(
                StockListing(
                    symbol = fields[0],
                    name = fields[1],
                    exchange = fields[2],
                    assetType = fields[3],
                    ipoData = fields[4],
                    status = fields[5],
                )
            )
        }
    }
}

private fun measureMemoryUsage(block: () -> Unit): Long {
    val runtime = Runtime.getRuntime()
    System.gc()
    val before = runtime.totalMemory() - runtime.freeMemory()
    block()
    val after = runtime.totalMemory() - runtime.freeMemory()
    return (after - before) / 1024
}