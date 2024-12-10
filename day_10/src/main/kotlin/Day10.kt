package meow.andurian.aoc2024.day_10

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

enum class Score {
    Target, Path
}

class TopologicalMap(val rows: Int, val cols: Int, val heights: Map<Point, Int>) {

    fun trailHeads(): List<Point> {
        val ret = mutableListOf<Point>()
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val p = Point(row, col)
                if (heights[p] == 0) {
                    ret.add(p)
                }
            }
        }
        return ret
    }

    fun contains(p: Point): Boolean {
        return heights.containsKey(p)
    }

    fun trailheadScore(head: Point, score: Score): Int {
        if (heights[head] != 0) return 0

        val targets = mutableListOf<Point>()

        fun dfs(current: Point, visited: Set<Point>) {
            if (heights[current] == 9) {
                targets.add(current)
                return
            }
            for (d in Direction.entries) {
                val next = current.next(d)
                if (contains(next) && heights[next]!! - heights[current]!! == 1 && !visited.contains(next)) {
                    dfs(next, visited + setOf(next))
                }
            }
        }
        dfs(head, setOf(head))

        return when (score) {
            Score.Target -> targets.toSet().size
            Score.Path -> targets.size
        }
    }

    companion object {
        fun fromInput(lines: List<String>): TopologicalMap {
            val rows = lines.size
            val cols = lines[0].length
            val heights = mutableMapOf<Point, Int>()

            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    val p = Point(row, col)
                    heights[p] = lines[row][col].code - '0'.code
                }
            }

            return TopologicalMap(rows, cols, heights)
        }
    }
}

fun task01(map: TopologicalMap): Int {
    return map.trailHeads().sumOf { map.trailheadScore(it, Score.Target) }
}

fun task02(map: TopologicalMap): Int {
    return map.trailHeads().sumOf { map.trailheadScore(it, Score.Path) }
}

fun main() {
    val lines = readResourceAsLines("/input_mm.txt")
    val map = TopologicalMap.fromInput(lines)

    println("Day 10 Task 1: ${task01(map)}")
    println("Day 10 Task 2: ${task02(map)}")
}