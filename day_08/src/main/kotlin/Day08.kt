package meow.andurian.aoc2024.day_08

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.Point
import java.io.BufferedReader

fun findAntennas(lines: List<String>): Map<Char, List<Point>> {
    val antennas = mutableMapOf<Char, MutableList<Point>>()

    for (row in lines.indices) {
        for (col in lines[row].indices) {
            val c = lines[row][col]
            if (c == '.') continue
            antennas.getOrPut(c) { mutableListOf() }.add(Point(row, col))
        }
    }

    return antennas
}

fun antinodeLocations(antennaLocations: List<Point>, maxRow: Int, maxCol: Int): Set<Point> {
    val ret = mutableSetOf<Point>()
    for (i in antennaLocations.indices) {
        val p1 = antennaLocations[i]
        for (j in antennaLocations.indices) {
            val p2 = antennaLocations[j]
            if (i == j) continue
            val p = Point(p2.row + p2.row - p1.row, p2.col + p2.col - p1.col)
            if (p.row >= 0 && p.row <= maxRow && p.col >= 0 && p.col <= maxCol) ret.add(p)
        }
    }
    return ret
}

fun resonantAntinodeLocations(antennaLocations: List<Point>, maxRow: Int, maxCol: Int): Set<Point> {
    val ret = mutableSetOf<Point>()

    fun addAll(p1: Point, p2: Point) {
        val dRow = p2.row - p1.row
        val dCol = p2.col - p1.col

        var current = Point(p2.row + dRow, p2.col + dCol)
        while (current.row >= 0 && current.col >= 0 && current.row <= maxRow && current.col <= maxCol) {
            ret.add(current)
            current = Point(current.row + dRow, current.col + dCol)
        }
    }

    for (i in antennaLocations.indices) {
        val p1 = antennaLocations[i]
        ret.add(p1)
        for (j in antennaLocations.indices) {
            val p2 = antennaLocations[j]
            if (i == j) continue
            addAll(p1, p2)
        }
    }
    return ret
}

fun printBoard(antennas: Map<Char, List<Point>>, antinodes: Set<Point>, rows: Int, cols: Int) {
    for (row in 0..rows) {
        loop@ for (col in 0..cols) {
            val p = Point(row, col)
            for ((k, v) in antennas) {
                if (v.contains(p)) {
                    print(k)
                    continue@loop
                }
            }
            if (antinodes.contains(p)) {
                print('#')
                continue
            }
            print('.')
        }
        print("\n")
    }
}

fun allUniqueAntinodeLocations(lines: List<String>, antinodeFun: (List<Point>, Int, Int) -> Set<Point>): Int {
    val rows = lines.size
    val cols = lines[0].length

    val antennas = findAntennas(lines)
    val ret = mutableSetOf<Point>()
    for ((_, antennaLocations) in antennas) {
        ret += antinodeFun(antennaLocations, rows, cols)
    }

    return ret.filter { it.row >= 0 && it.row < rows && it.col >= 0 && it.col < cols }.size
}

fun task01(lines: List<String>): Int {
    return allUniqueAntinodeLocations(lines, ::antinodeLocations)
}

fun task02(lines: List<String>): Int {
    return allUniqueAntinodeLocations(lines, ::resonantAntinodeLocations)
}

class Day08 : AoCDay() {
    override fun testInput() = "/test_input_2.txt"
    override fun solve(reader: BufferedReader) {
        var lines = reader.readLines()

        println("Day 08 Task 1: ${task01(lines)}")
        println("Day 08 Task 2: ${task02(lines)}")
    }
}

fun main(args: Array<String>) = Day08().main(args)
