package meow.andurian.aoc2024.day_12

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.DenseGrid
import meow.andurian.aoc2024.utils.Direction
import meow.andurian.aoc2024.utils.Point
import java.io.BufferedReader
import kotlin.math.abs

enum class Orientation {
    Horizontal, Vertical
}

data class Fence(val orientation: Orientation, val on: Int, val between: Pair<Int, Int>) {
    companion object {
        fun betweenPoints(p1: Point, p2: Point): Fence {
            if (p1.row == p2.row) {
                assert(abs(p1.col - p2.col) == 1)
                return Fence(Orientation.Vertical, p1.row, Pair(p1.col, p2.col))
            }
            if (p1.col == p2.col) {
                assert(abs(p1.row - p2.row) == 1)
                return Fence(Orientation.Horizontal, p1.col, Pair(p1.row, p2.row))
            }
            throw IllegalArgumentException("Points must share row or col")
        }
    }
}


class Grid<T> : DenseGrid<T> {

    constructor(lines: List<String>, getter: (Char) -> T) : super(lines, getter) {}

    fun regions(): List<Set<Point>> {
        fun regionFrom(p: Point): Set<Point> {
            val ret = mutableSetOf<Point>()
            val kind = at(p)

            fun dfs(current: Point) {
                ret.add(current)
                for (d in Direction.entries) {
                    val next = current.neighbor(d)
                    if (contains(next) && at(next) == kind && !ret.contains(next)) {
                        dfs(next)
                    }
                }
            }
            dfs(p)
            return ret
        }

        val visited = mutableSetOf<Point>()
        val ret = mutableListOf<Set<Point>>()
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val p = Point(r, c)
                if (!visited.contains(p)) {
                    val region = regionFrom(p)
                    visited.addAll(region)
                    ret.add(region)
                }
            }
        }
        return ret
    }

    fun fence(region: Set<Point>): Set<Fence> {
        val ret = mutableSetOf<Fence>()
        for (p in region) {
            for (d in Direction.entries) {
                val neighbor = p.neighbor(d)
                if (!region.contains(neighbor)) {
                    ret.add(Fence.betweenPoints(p, neighbor))
                }
            }
        }
        return ret
    }
}

fun sides(fences: Set<Fence>): Int {
    data class FenceId(val orientation: Orientation, val between: Pair<Int, Int>) {}

    val segments = fences.groupBy { FenceId(it.orientation, it.between) }
    val sortedSegments = segments.values.map { it.sortedBy { it.on } }

    var numSides = 0
    for (segment in sortedSegments) {
        numSides++
        for (i in segment.indices.drop(1)) {
            if (segment[i].on - segment[i - 1].on > 1) {
                numSides++
            }
        }
    }
    return numSides
}

fun calcTotalPrice(grid: Grid<Char>, f: (Set<Fence>) -> Int): Int {
    val regions = grid.regions()
    val fences = regions.map { grid.fence(it) }

    return (0 until regions.size).sumOf { regions[it].size * f(fences[it]) }
}

fun task01(grid: Grid<Char>): Int {
    return calcTotalPrice(grid) { it.size }
}

fun task02(grid: Grid<Char>): Int {
    return calcTotalPrice(grid) { sides(it) }
}

class Day12 : AoCDay() {
    override fun testInput() = "/test_input_3.txt"
    override fun solve(reader: BufferedReader) {
        val grid = Grid<Char>(reader.readLines()) { it }

        println("Day 12 Task 1: ${task01(grid)}")
        println("Day 12 Task 1: ${task02(grid)}")
    }
}

fun main(args: Array<String>) = Day12().main(args)

