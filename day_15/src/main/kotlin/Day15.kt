package meow.andurian.aoc2024.day_15

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.DenseGrid
import meow.andurian.aoc2024.utils.Direction
import meow.andurian.aoc2024.utils.Point
import java.io.BufferedReader

enum class Tile {
    Wall {
        override fun toString() = "#"
    },
    Box {
        override fun toString() = "O"
    },
    BigBoxLeft {
        override fun toString() = "["
    },
    BigBoxRight {
        override fun toString() = "]"
    },
    Robot {
        override fun toString() = "@"
    },
    Empty {
        override fun toString() = "."
    };

    companion object {
        fun fromChar(c: Char): Tile {
            return when (c) {
                '#' -> Wall
                'O' -> Box
                '[' -> BigBoxLeft
                ']' -> BigBoxRight
                '@' -> Robot
                else -> Empty
            }
        }
    }
}

class Grid : DenseGrid<Tile> {
    var robotPos: Point

    constructor(lines: List<String>) : super(lines, Tile::fromChar) {
        robotPos = positionIterator().asSequence().find { at(it) == Tile.Robot }!!
    }

    fun moveRobot(d: Direction) {
        robotPos = move(d, robotPos)
    }

    private fun swap(p1: Point, p2: Point) {
        val v1 = at(p1)!!
        val v2 = at(p2)!!
        set(p1, v2)
        set(p2, v1)
    }

    private fun moveBigBoxVertical(d: Direction, p: Point): Point {
        val boxesToMove = mutableSetOf(p)
        if (at(p)!! == Tile.BigBoxLeft) boxesToMove.add(p.neighbor(Direction.East))
        if (at(p)!! == Tile.BigBoxRight) boxesToMove.add(p.neighbor(Direction.West))

        var lookAt = boxesToMove // The boxes that were added in the previous step
        while (true) {
            val newBoxes = mutableSetOf<Point>()
            for (pBox in lookAt) {
                val pNeighbor = pBox.neighbor(d)
                val neighbor = at(pNeighbor)!!
                if (neighbor == Tile.Wall) return p // Movement is blocked by wall
                if (neighbor == Tile.BigBoxLeft) {
                    newBoxes.add(pNeighbor)
                    newBoxes.add(pNeighbor.neighbor(Direction.East))
                }
                if (neighbor == Tile.BigBoxRight) {
                    newBoxes.add(pNeighbor)
                    newBoxes.add(pNeighbor.neighbor(Direction.West))
                }
            }
            if (newBoxes.isEmpty()) break
            boxesToMove.addAll(newBoxes)
            lookAt = newBoxes
        }

        // Sort boxes in reverse row order depending on direction so we can swap them row by row with empty space
        val orderedBoxes = boxesToMove.toMutableList()
        orderedBoxes.sortByDescending { it.row }
        if (d == Direction.North) orderedBoxes.reverse()

        for (box in orderedBoxes) {
            swap(box, box.neighbor(d))
        }
        return p.neighbor(d)
    }

    private fun move(d: Direction, p: Point): Point {
        val moveTo = p.neighbor(d)
        when (at(moveTo)!!) {
            Tile.Wall -> return p
            Tile.Robot -> throw RuntimeException("Multiple robots not allowed")
            Tile.Empty -> {
                swap(p, moveTo)
                return moveTo
            }

            Tile.Box -> {
                val boxMovedTo = move(d, moveTo)
                if (boxMovedTo == moveTo) return p
                swap(p, moveTo)
                return moveTo
            }

            Tile.BigBoxLeft, Tile.BigBoxRight -> {
                if (d == Direction.East || d == Direction.West) {
                    val boxMovedTo = move(d, moveTo)
                    if (boxMovedTo == moveTo) return p
                    swap(p, moveTo)
                    return moveTo
                }
                // Only moving ig boxes vertically cannot be done recursively. So do it chunked.
                return moveBigBoxVertical(d, p)
            }
        }
    }

    fun gpsCoordinates(): Int {
        return positionIterator().asSequence().filter { at(it) == Tile.Box || at(it) == Tile.BigBoxLeft }
            .sumOf { it.row * 100 + it.col }
    }
}

fun enlargeGrid(lines: List<String>): List<String> {
    fun transform(c: Char): String {
        return when (c) {
            '#' -> "##"
            'O' -> "[]"
            '.' -> ".."
            '@' -> "@."
            else -> throw IllegalArgumentException("Illegal character: $c")
        }
    }
    return lines.map { line -> line.map(::transform).joinToString("") }
}

fun solve(lines: List<String>, enlarge: Boolean = false): Int {
    val gridLines = lines.takeWhile { !it.isEmpty() }

    val grid = Grid(if (enlarge) enlargeGrid(gridLines) else gridLines)
    val instructions = lines.drop(grid.rows + 1).joinToString("").map(Direction::fromChar)

    for (instruction in instructions) {
        grid.moveRobot(instruction)
    }
    return grid.gpsCoordinates()
}

fun task01(lines: List<String>) = solve(lines)
fun task02(lines: List<String>) = solve(lines, true)

class Day15 : AoCDay() {
    override fun testInput() = "/test_input.txt"

    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()

        println("Day 15 Task 1: ${task01(lines)}")
        println("Day 15 Task 2: ${task02(lines)}")
    }
}

fun main(args: Array<String>) = Day15().main(args)