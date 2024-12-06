package meow.andurian.aoc2024.day_06

import meow.andurian.aoc2024.utils.indexesOf
import meow.andurian.aoc2024.utils.readResourceAsLines

enum class Direction {
    North {
        override fun next() = East
    },
    East {
        override fun next() = South
    },
    South {
        override fun next() = West
    },
    West {
        override fun next() = North
    };

    abstract fun next(): Direction
}

data class Point(val row: Int, val col: Int) {
    fun next(d: Direction): Point {
        return when (d) {
            Direction.North -> Point(row - 1, col)
            Direction.East -> Point(row, col + 1)
            Direction.South -> Point(row + 1, col)
            Direction.West -> Point(row, col - 1)
        }
    }
}

class Grid {
    val rows: Int
    val cols: Int
    private val obstacles: Set<Point>

    private constructor(grid: Grid, p: Point) {
        rows = grid.rows
        cols = grid.cols
        obstacles = grid.obstacles.union(setOf(p))
    }

    constructor(lines: List<String>) {
        rows = lines.size
        cols = lines[0].length
        this.obstacles = lines.mapIndexed { row, line ->
            line.mapIndexedNotNull { col, c ->
                if (c == '#') Point(row, col) else null
            }
        }.flatten().toSet()
    }

    fun contains(p: Point): Boolean {
        return p.row >= 0 && p.row < rows && p.col >= 0 && p.col < cols
    }

    fun isObstacle(p: Point): Boolean {
        return obstacles.contains(p)
    }

    fun withAddedObstacle(p: Point): Grid {
        return Grid(this, p)
    }
}

data class Guard(val pos: Point, val dir: Direction) {

    constructor(lines: List<String>) : this({
        val row = lines.indexesOf { line -> line.contains('^') }.first()
        val col = lines[row].indexOf('^')
        Point(row, col)
    }(), Direction.North) {
    }

    fun move(grid: Grid): Guard {
        val startingDir = dir

        var nextDir = dir
        var nextPos = pos.next(nextDir)

        while (grid.isObstacle(nextPos)) {
            nextDir = nextDir.next()
            if (nextDir == startingDir)
                return this
            nextPos = pos.next(nextDir)
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
    return candidates.map{mapRoute(grid.withAddedObstacle(it), guard).second}.count{it == RouteType.Loop}
}

fun main() {
    val lines = readResourceAsLines("/test_input.txt")
    val grid = Grid(lines)
    val guard = Guard(lines)

    println("Day 06 Task 1: ${task01(grid, guard)}")
    println("Day 06 Task 1: ${task02(grid, guard)}")
}