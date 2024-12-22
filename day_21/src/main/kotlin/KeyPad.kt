package meow.andurian.aoc2024.day_21

import meow.andurian.aoc2024.utils.DenseGrid
import meow.andurian.aoc2024.utils.Direction
import meow.andurian.aoc2024.utils.Point

open class KeyPad<T> : DenseGrid<Key<T>> {
    val keyPositions: Map<Key<T>, Point>
    val shortestPathMap: Map<Point, Map<Point, List<Direction>>>

    constructor(
        rows: Int,
        cols: Int,
        values: List<Key<T>>,
        findShortestPath: (Point, Point) -> List<Direction>
    ) : super(rows, cols, values) {
        val _positionsOf = mutableMapOf<Key<T>, Point>()
        val _shortestPaths = mutableMapOf<Point, Map<Point, List<Direction>>>()

        for (rowStart in 0 until rows) {
            for (colStart in 0 until cols) {
                val pStart = Point(rowStart, colStart)
                val atStart = at(pStart)!!
                _positionsOf[atStart] = pStart
                if (atStart is Gap<T>) continue
                val shortestPathsFrom = mutableMapOf<Point, List<Direction>>()
                for (rowEnd in 0 until rows) {
                    for (colEnd in 0 until cols) {
                        val pEnd = Point(rowEnd, colEnd)
                        val atEnd = at(pEnd)!!
                        if (atEnd is Gap<T>) continue
                        shortestPathsFrom[pEnd] = findShortestPath(pStart, pEnd)
                    }
                }
                _shortestPaths[pStart] = shortestPathsFrom
            }
        }

        keyPositions = _positionsOf
        shortestPathMap = _shortestPaths
    }

    fun shortestPath(from: Key<T>, to: Key<T>): List<Direction> {
        val pFrom = keyPositions[from]!!
        val pTo = keyPositions[to]!!
        return shortestPathMap[pFrom]!![pTo]!!
    }
}

fun horizontalMovement(d: Int): List<Direction> {
    if (d < 0) return List<Direction>(-d) { Direction.West }
    if (d > 0) return List<Direction>(d) { Direction.East }
    return emptyList<Direction>()
}

fun verticalMovement(d: Int): List<Direction> {
    if (d < 0) return List<Direction>(-d) { Direction.North }
    if (d > 0) return List<Direction>(d) { Direction.South }
    return emptyList<Direction>()
}


fun shortestPathNumpad(start: Point, end: Point): List<Direction> {
    val dRow = end.row - start.row
    val dCol = end.col - start.col

    val h = horizontalMovement(dCol)
    val v = verticalMovement(dRow)

    if (start.col == 0 && end.row == 3) { // Avoid Gap
        return h + v
    } else if (start.row == 3 && end.col == 0) { // Avoid Gap
        return v + h
    } else {
        if (dRow == 0) return h
        if (dCol == 0) return v

        // Always press the leftmost button first, never mix directions
        if (dCol > 0) return v + h
        return h + v
    }
}

class NumPad : KeyPad<Int>( //
    4, 3, //
    listOf<Key<Int>>( //
        NumberKey(7), NumberKey(8), NumberKey(9), //
        NumberKey(4), NumberKey(5), NumberKey(6), //
        NumberKey(1), NumberKey(2), NumberKey(3), //
        Gap<Int>(), NumberKey(0), ActivateKey<Int>() //
    ),
    ::shortestPathNumpad
) {
    companion object {
        @Volatile
        private var instance: NumPad? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: NumPad().also { instance = it }
            }
    }
}

fun shortestPathArrowPad(start: Point, end: Point): List<Direction> {
    val dRow = end.row - start.row
    val dCol = end.col - start.col

    val h = horizontalMovement(dCol)
    val v = verticalMovement(dRow)

    if (start.col == 0 && end.row == 0) {
        return h + v
    } else if (start.row == 0 && end.col == 0) {
        return v + h
    } else {
        if (dRow == 0) return h
        if (dCol == 0) return v

        // Always press the leftmost button first, never mix directions
        if (dCol > 0) return v + h
        return h + v
    }
}

class ArrowPad : KeyPad<Direction>( //
    2, 3, //
    listOf<Key<Direction>>( //
        Gap<Direction>(), DirectionKey(Direction.North), ActivateKey<Direction>(), //
        DirectionKey(Direction.West), DirectionKey(Direction.South), DirectionKey(Direction.East) //
    ),
    ::shortestPathArrowPad
) {
    companion object {
        @Volatile
        private var instance: ArrowPad? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ArrowPad().also { instance = it }
            }
    }
}