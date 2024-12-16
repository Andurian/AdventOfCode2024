package meow.andurian.aoc2024.day_16

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.DenseGrid
import meow.andurian.aoc2024.utils.Direction
import meow.andurian.aoc2024.utils.Point
import java.io.BufferedReader
import java.util.*

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

data class Position(val p: Point, val d: Direction)

data class Path(val moves: List<Position>) {
    val score: Int

    init {
        if (moves.isEmpty()) {
            score = Int.MAX_VALUE
        } else {
            var sum = 0
            for (i in moves.indices.drop(1)) {
                sum += moves[i - 1].d.distance(moves[i].d) * 1000 + 1
            }
            score = sum
        }
    }

    val end: Position
        get() = moves.last()
}


class Maze : DenseGrid<Tile> {
    val start: Position = Position(Point(rows - 2, 1), Direction.East)
    val end: Point = Point(1, cols - 2)

    constructor(lines: List<String>) : super(lines, Tile::fromChar)

    fun shortestPaths(): List<Path> {

        val bestDistances = mutableMapOf<Position, List<Path>>().withDefault { emptyList<Path>() }
        val priorityQueue = PriorityQueue<Path>(compareBy { it.score })

        val startingPath = Path(listOf(start))
        priorityQueue.add(startingPath)
        bestDistances[start] = listOf(startingPath)

        while (priorityQueue.isNotEmpty()) {
            val current = priorityQueue.poll()
            for (d in Direction.entries) {
                val next = Path(current.moves + Position(current.end.p.neighbor(d), d))
                val neighbor = at(next.end.p)!!
                if (neighbor == Tile.Wall) continue
                val currentBest = bestDistances.getValue(next.end)
                if (currentBest.isEmpty()) {
                    bestDistances[next.end] = listOf(next)
                    priorityQueue.add(next)
                } else if (next.score < currentBest.first().score) {
                    bestDistances[next.end] = listOf(next)
                    priorityQueue.add(next)
                } else if (next.score == currentBest.first().score) {
                    bestDistances[next.end] = bestDistances[next.end]!! + listOf(next)
                    priorityQueue.add(next)
                }
            }
        }

        return bestDistances.getValue(Position(end, Direction.North))
    }
}

fun task01(shortestPaths: List<Path>): Int {
    return shortestPaths.first().score
}

fun task02(shortestPaths: List<Path>): Int {
    return shortestPaths.map { path -> path.moves.map { it.p } }.flatten().toSet().size
}

class Day16 : AoCDay() {
    override fun testInput() = "/test_input_1.txt"
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()
        val maze = Maze(lines)
        val paths = maze.shortestPaths()

        println("Day 16 Task 1: ${task01(paths)}")
        println("Day 16 Task 2: ${task02(paths)}")
    }
}

fun main(args: Array<String>) = Day16().main(args)