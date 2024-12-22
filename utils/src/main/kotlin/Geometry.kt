package meow.andurian.aoc2024.utils

enum class Direction {
    North {
        override fun nextCW() = East
        override fun nextCCW() = West
        override fun opposite() = South
        override fun toChar() = '^'
    },
    East {
        override fun nextCW() = South
        override fun nextCCW() = North
        override fun opposite() = West
        override fun toChar() = '>'
    },
    South {
        override fun nextCW() = West
        override fun nextCCW() = East
        override fun opposite() = North
        override fun toChar() = 'v'
    },
    West {
        override fun nextCW() = North
        override fun nextCCW() = South
        override fun opposite() = East
        override fun toChar() = '<'
    };

    abstract fun nextCW(): Direction
    abstract fun nextCCW(): Direction
    abstract fun opposite(): Direction
    abstract fun toChar() : Char

    fun distance(other : Direction) : Int{
        if(other == this) return 0
        if(other == opposite()) return 2
        return 1
    }

    companion object{
        fun fromChar(c : Char) : Direction{
            return when(c){
                '^' -> North
                '>' -> East
                'v' -> South
                '<' -> West
                else -> throw IllegalArgumentException()
            }
        }
    }
}

enum class Direction8 {
    North {
        override fun nextCW() = NorthEast
        override fun nextCCW() = NorthWest
    },
    NorthEast {
        override fun nextCW() = East
        override fun nextCCW() = North
    },
    East {
        override fun nextCW() = SouthEast
        override fun nextCCW() = NorthEast
    },
    SouthEast {
        override fun nextCW() = South
        override fun nextCCW() = East
    },
    South {
        override fun nextCW() = SouthWest
        override fun nextCCW() = SouthEast
    },
    SouthWest {
        override fun nextCW() = West
        override fun nextCCW() = South
    },
    West {
        override fun nextCW() = NorthWest
        override fun nextCCW() = SouthWest
    },
    NorthWest {
        override fun nextCW() = North
        override fun nextCCW() = West
    };

    abstract fun nextCW(): Direction8
    abstract fun nextCCW(): Direction8
}

// I would like to make Point generic on T but the limitations on Kotlin generics does not seem worth it.
data class Point(val row: Int, val col: Int) {
    fun neighbor(d: Direction): Point {
        return when (d) {
            Direction.North -> Point(row - 1, col)
            Direction.East -> Point(row, col + 1)
            Direction.South -> Point(row + 1, col)
            Direction.West -> Point(row, col - 1)
        }
    }

    fun neighbor(d: Direction8): Point {
        return when (d) {
            Direction8.North -> Point(row - 1, col)
            Direction8.NorthEast -> Point(row - 1, col + 1)
            Direction8.East -> Point(row, col + 1)
            Direction8.SouthEast -> Point(row + 1, col + 1)
            Direction8.South -> Point(row + 1, col)
            Direction8.SouthWest -> Point(row + 1, col - 1)
            Direction8.West -> Point(row, col - 1)
            Direction8.NorthWest -> Point(row - 1, col - 1)
        }
    }

    fun plus(other : Point) : Point{
        return Point(row + other.row, col + other.col)
    }

    fun Point.minus(other : Point) : Point{
        return Point(row - other.row, col - other.col)
    }

    companion object {
        fun fromString(s : String) : Point{
            val tokens = s.split(',')
            return Point(tokens[1].toInt(), tokens[0].toInt())
        }
    }
}



open class Extent(val rows: Int, val cols: Int) {
    fun contains(p: Point): Boolean {
        return p.row >= 0 && p.row < rows && p.col >= 0 && p.col < cols
    }
}

abstract class Grid<T>(rows: Int, cols: Int) : Extent(rows, cols) {
    abstract fun at(p: Point): T?
    abstract fun set(p: Point, v: T)

    inner class PositionIterator : Iterator<Point>{
        private var row = 0
        private var col = 0

        override fun hasNext() = row < rows

        override fun next(): Point {
            val ret = Point(row, col)
            ++col
            if(col >= cols) {
                col = 0
                ++row
            }
            return ret
        }
    }

    inner class ValueIterator : Iterator<T>{
        private val it = PositionIterator()

        override fun hasNext() = it.hasNext()
        override fun next() = at(it.next())!!
    }

    fun positionIterator() : Iterator<Point> = PositionIterator()
    fun valueIterator() : Iterator<T> = ValueIterator()

    override fun toString() : String{
        return valueIterator().asSequence().map{ it.toString()[0] }.joinToString("").chunked(cols).joinToString("\n")
    }
}

open class SparseGrid<T> : Grid<T> {
    protected val elements: MutableMap<Point, T>

    constructor(rows: Int, cols: Int) : super(rows, cols) {
        elements = mutableMapOf<Point, T>()
    }

    override fun at(p: Point) = elements[p]
    override fun set(p: Point, v: T) {
        if (!contains(p)) throw IndexOutOfBoundsException()
        elements.put(p, v)
    }
}

open class DenseGrid<T> : Grid<T> {
    protected val elements: MutableList<T>

    constructor(lines: List<String>, getter: (Char) -> T) : super(lines.size, lines[0].length) {
        elements = lines.map { line -> line.map { c -> getter(c) } }.flatten().toMutableList()
    }

    constructor(rows : Int, cols : Int, values : List<T>) : super(rows, cols) {
        elements = values.toMutableList()
    }

    private fun toOffset(p: Point) = (p.row * cols + p.col).toInt()

    override fun at(p: Point): T? {
        if (!contains(p)) return null
        return elements[toOffset(p)]
    }

    override fun set(p: Point, v: T) {
        if (!contains(p)) throw IndexOutOfBoundsException()
        elements[toOffset(p)] = v
    }
}

