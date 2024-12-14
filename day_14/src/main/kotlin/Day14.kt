package meow.andurian.aoc2024.day_14

import meow.andurian.aoc2024.utils.readResourceAsLines

data class Point(val row: Int, val col: Int)

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

fun printGrid(robots : List<Point>, rows : Int, cols : Int) {
    for(r in 0 until rows){
        for(c in 0 until cols){
            val p = Point(r, c)
            val n = robots.count{it == p}
            if(n == 0){
                print(".")
            }else{
                print(n)
            }
        }
        println()
    }
}

fun task01(robots : List<Robot>, rows : Int, cols: Int) : Long{
    val positions = robots.map{it.moveN(100,rows, cols)}
    return safetyRating(positions, rows, cols)
}

fun task02(robots : List<Robot>, rows : Int, cols: Int) : Int{
    var i = 0
    while(true) {
        i++
        val positions = robots.map { it.moveN(i, rows, cols) }
        if (positions.toSet().size == positions.size) {
            printGrid(positions, rows, cols)
            return i
        }
    }
}

fun main() {
    val lines = readResourceAsLines("/test_input.txt")
    val robots = lines.map { Robot.fromString(it) }

    println("Day 14 Task 1: ${task01(robots, 103, 101)}")
    println("Day 14 Task 2: ${task02(robots, 103, 101)}")
}