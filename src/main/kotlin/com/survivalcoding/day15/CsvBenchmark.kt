package com.survivalcoding.day15

import com.sun.management.ThreadMXBean
import com.survivalcoding.day15.model.StockListing
import java.lang.management.ManagementFactory
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.system.measureTimeMillis

//readLines + map 실행 시간 (100회 총합): 10ms
//readLines + for 루프 실행 시간 (100회 총합): 9ms
//readLines + buildList 실행 시간 (100회 총합): 8ms
//----------------------------------------
//readLines + map 메모리 사용량: 2892 KB
//readLines + for 루프 메모리 사용량: 2680 KB
//readLines + buildList 메모리 사용량: 2683 KB
fun main() {
    try {
        val iterations = 100 // 100번 반복 측정

        // 1. JIT 컴파일러 예열 (Warm-up)
        repeat(10) {
            loadUsingReadLinesMap()
            loadUsingReadLinesForLoop()
            loadUsingBuildList()
        }

        // 2. readLines + map 방식 측정
        val mapMemory = measureMemoryUsage {
            val mapTime = measureTimeMillis {
                repeat(iterations) {
                    loadUsingReadLinesMap()
                }
            }
            println("readLines + map 실행 시간 (${iterations}회 총합): ${mapTime}ms")
        }

        // 3. readLines + for 루프 (초기 용량 ArrayList) 방식 측정
        val forLoopMemory = measureMemoryUsage {
            val forLoopTime = measureTimeMillis {
                repeat(iterations) {
                    loadUsingReadLinesForLoop()
                }
            }
            println("readLines + for 루프 실행 시간 (${iterations}회 총합): ${forLoopTime}ms")
        }

        // 4. readLines + buildList 방식 측정
        val buildListMemory = measureMemoryUsage {
            val buildListTime = measureTimeMillis {
                repeat(iterations) {
                    loadUsingBuildList()
                }
            }
            println("readLines + buildList 실행 시간 (${iterations}회 총합): ${buildListTime}ms")
        }

        println("----------------------------------------")
        println("readLines + map 메모리 사용량: ${mapMemory} KB")
        println("readLines + for 루프 메모리 사용량: ${forLoopMemory} KB")
        println("readLines + buildList 메모리 사용량: ${buildListMemory} KB")

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// --- 비교 대상 1: readLines + map 방식 ---
private fun loadUsingReadLinesMap(): List<StockListing> {
    val path = Path("src/main/resources/todo_data.json")
    val rows = path.readLines().drop(1)

    return rows.map { row ->
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
}

// --- 비교 대상 2: readLines + for 루프 (초기 용량 지정) 방식 ---
private fun loadUsingReadLinesForLoop(): List<StockListing> {
    val path = Path("src/main/resources/todo_data.json")
    val rawRows = path.readLines()
    if (rawRows.isEmpty()) return emptyList()

    val rows = rawRows.drop(1)
    val stockListings = ArrayList<StockListing>(rows.size)

    for (row in rows) {
        if (row.isBlank()) continue
        val fields = row.split(',')

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

    return stockListings
}

// --- 비교 대상 3: readLines + buildList 방식 ---
private fun loadUsingBuildList(): List<StockListing> {
    val path = Path("src/main/resources/todo_data.json")
    val rawRows = path.readLines()
    val rows = rawRows.drop(1)

    return buildList(rows.size) {
        rows.forEach { row ->
            if (row.isBlank()) return@forEach
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
    val threadMXBean = ManagementFactory.getThreadMXBean() as ThreadMXBean

    if (!threadMXBean.isThreadAllocatedMemorySupported) {
        println("Thread allocated memory tracking is not supported.")
        return -1
    }

    System.gc()
    Thread.sleep(50)

    val before = threadMXBean.getCurrentThreadAllocatedBytes()
    block()
    val after = threadMXBean.getCurrentThreadAllocatedBytes()

    return (after - before) / 1024
}