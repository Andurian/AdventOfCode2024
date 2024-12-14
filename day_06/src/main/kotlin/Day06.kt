package meow.andurian.aoc2024.day_06

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.*
import java.io.BufferedReader

class Grid(
    rows: Int,
    cols: Int,
    val obstacles: Set<Point>
) : Extent(rows, cols) {

    constructor(lines: List<String>) : this(
        lines.size, lines[0].length,
        lines.mapIndexed { row, line ->
            line.mapIndexedNotNull { col, c ->
                if (c == '#') Point(row, col) else null
            }
        }.flatten().toSet()
    )

    fun isObstacle(p: Point): Boolean {
        return obstacles.contains(p)
    }

    fun withAddedObstacle(p: Point): Grid {
        return Grid(rows, cols, obstacles.union(setOf(p)))
    }
}

data class Guard(val pos: Point, val dir: Direction) {

    constructor(lines: List<String>) : this({
        val row = lines.indexesOf { line -> line.contains('^') }.first()
        val col = lines[row].indexOf('^')
        Point(row, col)
    }(), Direction.North)

    fun move(grid: Grid): Guard {
        val startingDir = dir

        var nextDir = dir
        var nextPos = pos.neighbor(nextDir)

        while (grid.isObstacle(nextPos)) {
            nextDir = nextDir.nextCW()
            if (nextDir == startingDir)
                return this
            nextPos = pos.neighbor(nextDir)
        }
        return Guard(nextPos, nextDir)
    }
}

enum class RouteType {
    Path, Loop
}

fun mapRoute(grid: Grid, guardIn: Guard): Pair<Set<Guard>, RouteType> {
    val visited = mutableSetOf<Guard>(guardIn)

    var guard = guardIn
    while (true) {
        guard = guard.move(grid)
        if (!grid.contains(guard.pos)) {
            return Pair(visited, RouteType.Path)
        }
        if (visited.contains(guard)) {
            return Pair(visited, RouteType.Loop)
        }
        visited.add(guard)
    }
}

fun task01(grid: Grid, guard: Guard): Int {
    val (visited, _) = mapRoute(grid, guard)
    return visited.map { it.pos }.toSet().size
}

fun task02(grid: Grid, guard: Guard): Int {
    val candidates = mapRoute(grid, guard).first.map { it.pos }.toSet().subtract(setOf(guard.pos))
    return candidates.map { mapRoute(grid.withAddedObstacle(it), guard).second }.count { it == RouteType.Loop }
}

class Day06 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()
        val grid = Grid(lines)
        val guard = Guard(lines)

        println("Day 06 Task 1: ${task01(grid, guard)}")
        println("Day 06 Task 1: ${task02(grid, guard)}")
    }
}

fun main(args: Array<String>) = Day06().main(args)