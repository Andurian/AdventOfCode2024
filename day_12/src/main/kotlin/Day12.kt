package meow.andurian.aoc2024.day_12

import meow.andurian.aoc2024.utils.readResourceAsLines
import kotlin.math.abs

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

enum class Orientation{
    Horizontal, Vertical
}

data class Fence(val orientation: Orientation, val on : Int, val between : Pair<Int, Int>){
    companion object {
        fun betweenPoints(p1 : Point, p2: Point) : Fence{
            if(p1.row == p2.row){
                assert(abs(p1.col - p2.col) == 1)
                return Fence(Orientation.Vertical, p1.row, Pair(p1.col, p2.col))
            }
            if(p1.col == p2.col){
                assert(abs(p1.row - p2.row) == 1)
                return Fence(Orientation.Horizontal, p1.col, Pair(p1.row, p2.row))
            }
            throw IllegalArgumentException("Points must share row or col")
        }
    }
}


class Grid<T>(val rows : Int, val cols : Int) {
    private val content: MutableMap<Point, T> = mutableMapOf<Point, T>()

    constructor(lines: List<String>, getter : (Char) -> (T)) : this(lines.size, lines[0].length) {
        for(r in 0 until rows){
            for(c in 0 until cols){
                content.put(Point(r, c), getter(lines[r][c]))
            }
        }
    }

    fun contains(p: Point): Boolean {
        return p.row >= 0 && p.row < rows && p.col >= 0 && p.col < cols
    }

    fun at(p: Point): T{
        return content.get(p)!!
    }

    fun regions() : List<Set<Point>>{
        fun regionFrom(p: Point) : Set<Point>{
            val ret = mutableSetOf<Point>()
            val kind = at(p)

            fun dfs(current: Point){
                ret.add(current)
                for(d in Direction.entries){
                    val next = current.next(d)
                    if(contains(next) && at(next) == kind && !ret.contains(next)){
                        dfs(next)
                    }
                }
            }
            dfs(p)
            return ret
        }

        val visited = mutableSetOf<Point>()
        val ret = mutableListOf<Set<Point>>()
        for(r in 0 until rows){
            for(c in 0 until cols){
                val p = Point(r, c)
                if(!visited.contains(p)){
                    val region = regionFrom(p)
                    visited.addAll(region)
                    ret.add(region)
                }
            }
        }
        return ret
    }

    fun fence(region:Set<Point>) : Set<Fence>{
        val ret = mutableSetOf<Fence>()
        for(p in region){
            for(d in Direction.entries){
                val neighbor = p.next(d)
                if(!region.contains(neighbor)){
                    ret.add(Fence.betweenPoints(p, neighbor))
                }
            }
        }
        return ret
    }
}

fun sides(fences : Set<Fence>) : Int{
    data class FenceId(val orientation: Orientation, val between : Pair<Int, Int>){}
    val segments = fences.groupBy { FenceId(it.orientation, it.between) }
    val sortedSegments = segments.values.map{it.sortedBy{it.on}}

    var numSides = 0
    for(segment in sortedSegments){
        numSides++
        for(i in segment.indices.drop(1)){
            if(segment[i].on - segment[i-1].on > 1){
                numSides++
            }
        }
    }
    return numSides
}

fun calcTotalPrice(grid: Grid<Char>, f : (Set<Fence>) -> Int) : Int{
    val regions = grid.regions()
    val fences = regions.map{grid.fence(it)}

    return (0 until regions.size).sumOf { regions[it].size * f(fences[it]) }
}

fun task01(grid: Grid<Char>):Int{
    return calcTotalPrice(grid, {it.size})
}

fun task02(grid: Grid<Char>):Int{
    return calcTotalPrice(grid, {sides(it)})
}

fun main() {
    val lines = readResourceAsLines("/test_input_3.txt")
    val grid = Grid<Char>(lines) { it }

    println("Day 12 Task 1: ${task01(grid)}")
    println("Day 12 Task 1: ${task02(grid)}")
}