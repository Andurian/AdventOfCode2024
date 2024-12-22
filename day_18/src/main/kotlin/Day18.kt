package meow.andurian.aoc2024.day_18

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.*
import java.io.BufferedReader
import java.util.*

enum class Tile {
    Free,
    Byte
}

class Memory(rows: Int, cols: Int) : SparseGrid<Tile>(rows, cols) {
    val start = Point(0, 0)
    val end = Point(rows - 1, cols - 1)

    fun shortestPath(): List<Point>? {
        val distances = mutableMapOf<Point, List<Point>>().withDefault { emptyList<Point>() }
        val priorityQueue = PriorityQueue<List<Point>>(compareBy { it.size })

        distances[start] = listOf(start)
        priorityQueue.add(listOf(start))

        while (priorityQueue.isNotEmpty()) {
            val currentPath = priorityQueue.poll()
            val currentEnd = currentPath.last()
            for (d in Direction.entries) {
                val nextPos = currentEnd.neighbor(d)
                if (!contains(nextPos)) continue
                val nextTile = at(nextPos)
                if (nextTile == Tile.Byte) continue
                val nextPath = currentPath + nextPos
                val shortestPath = distances.getValue(nextPos)
                if (shortestPath.isEmpty() || nextPath.size < shortestPath.size) {
                    distances[nextPos] = nextPath
                    priorityQueue.add(nextPath)
                }
            }
        }
        return distances[end]
    }
}

fun task01(lines: List<String>, memorySize: Extent, numDroppedBytes: Int): Int {
    val bytes = lines.map(Point::fromString)

    val memory = Memory(memorySize.rows, memorySize.cols)
    for (b in bytes.take(numDroppedBytes)) {
        memory.set(b, Tile.Byte)
    }

    return memory.shortestPath()!!.size - 1
}

fun task02(lines: List<String>, memorySize: Extent, numDroppedBytes: Int): String {
    val bytes = lines.map(Point::fromString)

    val memory = Memory(memorySize.rows, memorySize.cols)
    for (b in bytes.take(numDroppedBytes)) {
        memory.set(b, Tile.Byte)
    }

    for (b in bytes.drop(numDroppedBytes)) {
        memory.set(b, Tile.Byte)
        if (memory.shortestPath() == null) return "${b.col},${b.row}"
    }

    throw IllegalArgumentException()
}

class Day18 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()

        var memorySize = Extent(71, 71)
        var numDroppedBytes = 1024

        if (lines.size < 100) { // We are working on test input
            memorySize = Extent(7, 7)
            numDroppedBytes = 12
        }

        println("Day 18 Task 1: ${task01(lines, memorySize, numDroppedBytes)}")
        println("Day 18 Task 2: ${task02(lines, memorySize, numDroppedBytes)}")
    }
}

fun main(args: Array<String>) = Day18().main(args)