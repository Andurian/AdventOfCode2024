package meow.andurian.aoc2024.day_20

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.DenseGrid
import meow.andurian.aoc2024.utils.Direction
import meow.andurian.aoc2024.utils.Point
import java.io.BufferedReader
import kotlin.math.abs

enum class Tile {
    Wall {
        override fun toString() = "#"
    },
    Empty {
        override fun toString() = "."
    };

    companion object {
        fun fromChar(c: Char): Tile {
            return when (c) {
                '#' -> Wall
                else -> Empty
            }
        }
    }
}

fun locationOf(c: Char, lines: List<String>): Point {
    for (row in lines.indices) {
        for (col in lines[row].indices) {
            if (c == lines[row][col]) return Point(row, col)
        }
    }
    throw RuntimeException()
}

class Maze : DenseGrid<Tile> {
    val start: Point
    val end: Point

    constructor(lines: List<String>) : super(lines, Tile::fromChar) {
        start = locationOf('S', lines)
        end = locationOf('E', lines)
    }

    fun path(): List<Point> {
        val ret = mutableListOf<Point>(start)
        while (ret.last() != end) {
            for (d in Direction.entries) {
                val next = ret.last().neighbor(d)
                val nextTile = at(next)
                if (nextTile == Tile.Empty && (ret.size < 2 || next != ret[ret.size - 2])) {
                    ret.add(next)
                    break
                }

            }
        }
        return ret
    }

}

data class Cheat(val start: Point, val end: Point) {
    val turns = abs(end.row - start.row) + abs(end.col - start.col)
}

fun task01(maze: Maze, minimumTimeSave: Int): Int {
    val pathMap = maze.path().mapIndexed { i, p -> p to i }.toMap()
    val cheats = mutableListOf<Cheat>()
    for ((p, i) in pathMap) {
        for (d in Direction.entries) {
            val p2 = p.neighbor(d).neighbor(d)
            if (!maze.contains(p2) || maze.at(p2) != Tile.Empty) continue
            cheats.add(Cheat(p, p2))
        }
    }

    return cheats.map { pathMap[it.end]!! - pathMap[it.start]!! - it.turns }.filter { it >= minimumTimeSave }.size
}


fun task02(maze: Maze, minimumTimeSave: Int): Int {
    val offsets = mutableListOf<Point>()
    val maxTurns = 20
    for (row in -maxTurns..maxTurns) {
        for (col in -maxTurns..maxTurns) {
            if (abs(row) + abs(col) <= maxTurns && (row != 0 || col != 0)) {
                offsets.add(Point(row, col))
            }
        }
    }

    val pathMap = maze.path().mapIndexed { i, p -> p to i }.toMap()
    val cheats = mutableListOf<Cheat>()
    for (p in pathMap.keys) {
        for (o in offsets) {
            val p2 = Point(p.row + o.row, p.col + o.col)
            if (!maze.contains(p2) || maze.at(p2) != Tile.Empty) continue
            cheats.add(Cheat(p, p2))
        }
    }

    return cheats.map { pathMap[it.end]!! - pathMap[it.start]!! - it.turns }.filter { it >= minimumTimeSave }.size
}

class Day20 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()
        val maze = Maze(lines)

        // Distinguish test from real input
        val minimumTimeSave1 = if (maze.rows < 20) 2 else 100
        val minimumTimeSave2 = if (maze.rows < 20) 50 else 100

        println("Day 20 Task 1: ${task01(maze, minimumTimeSave1)}")
        println("Day 20 Task 2: ${task02(maze, minimumTimeSave2)}")
    }
}

fun main(args: Array<String>) = Day20().main(args)