package meow.andurian.aoc2024.day_14

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.Point
import java.io.BufferedReader

class Robot(val pos: Point, val velocity: Point) {
    fun moveN(steps: Int, rows: Int, cols: Int): Point {
        val r = (pos.row + steps * velocity.row).mod(rows)
        val c = (pos.col + steps * velocity.col).mod(cols)
        return Point(r, c)
    }

    companion object {
        fun fromString(s: String): Robot {
            val re = """-?\d+""".toRegex()
            val m = re.findAll(s).map { it.value.toInt() }.toList()
            return Robot(Point(m[1], m[0]), Point(m[3], m[2]))
        }
    }
}

fun safetyRating(robots: List<Point>, rows: Int, cols: Int): Long {
    val qRows = (rows - 1) / 2
    val qCols = (cols - 1) / 2

    fun quadrant(p: Point): Int {
        if (p.row == qRows || p.col == qCols) return 0
        if (p.row < qRows) {
            if (p.col < qCols) {
                return 1
            }
            return 2
        }
        if (p.col < qCols) {
            return 3
        }
        return 4
    }

    val robotsPerQuadrant = robots.groupBy { quadrant(it) }.toMutableMap()
    robotsPerQuadrant.remove(0)

    return robotsPerQuadrant.values.fold(1L) { current, points -> current * points.size }
}

fun printGrid(robots: List<Point>, rows: Int, cols: Int) {
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val p = Point(r, c)
            val n = robots.count { it == p }
            if (n == 0) {
                print(".")
            } else {
                print(n)
            }
        }
        println()
    }
}

fun task01(robots: List<Robot>, rows: Int, cols: Int): Long {
    val positions = robots.map { it.moveN(100, rows, cols) }
    return safetyRating(positions, rows, cols)
}

fun task02(robots: List<Robot>, rows: Int, cols: Int): Int {
    var i = 0
    while (true) {
        i++
        val positions = robots.map { it.moveN(i, rows, cols) }
        if (positions.toSet().size == positions.size) {
            return i
        }
    }
}

class Day14 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val robots = reader.readLines().map { Robot.fromString(it) }

        // Values are hardcoded according to task
        val rows = 103
        val cols = 101

        println("Day 14 Task 1: ${task01(robots, rows, cols)}")

        val its = task02(robots, rows, cols)
        if (its == 1) {
            println("Day 14 Task 2: Cannot be solved on test data")
            return
        }

        println("Day 14 Task 2: $its")

        // Basic visualization
        val positions = robots.map { it.moveN(its, rows, cols) }
        printGrid(positions, rows, cols)
    }
}

fun main(args: Array<String>) = Day14().main(args)