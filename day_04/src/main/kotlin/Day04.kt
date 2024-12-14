package meow.andurian.aoc2024.day_04

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import java.io.BufferedReader

class Grid(private val lines: List<String>) {

    inner class HorizontalForwardsIterator(private val row: Int) : Iterator<Char> {
        private var col = 0

        override fun hasNext() = col < lines[row].length
        override fun next() = lines[row][col++]
    }

    inner class HorizontalBackwardsIterator(private val row: Int) : Iterator<Char> {
        private var col = lines[row].length - 1

        override fun hasNext() = col >= 0
        override fun next() = lines[row][col--]
    }

    inner class VerticalForwardsIterator(private val col: Int) : Iterator<Char> {
        private var row = 0
        override fun hasNext() = row < lines.size
        override fun next() = lines[row++][col]
    }

    inner class VerticalBackwardsIterator(private val col: Int) : Iterator<Char> {
        private var row = lines.size - 1
        override fun hasNext() = row >= 0
        override fun next() = lines[row--][col]
    }

    inner class DiagonalLRForwardsIterator(private var row: Int, private var col: Int) : Iterator<Char> {
        override fun hasNext() = row < lines.size && col < lines[row].length
        override fun next() = lines[row++][col++]
    }

    inner class DiagonalLRBackwardsIterator(private var row: Int, private var col: Int) : Iterator<Char> {
        override fun hasNext() = row >= 0 && col >= 0
        override fun next() = lines[row--][col--]
    }

    inner class DiagonalRLForwardsIterator(private var row: Int, private var col: Int) : Iterator<Char> {
        override fun hasNext() = row < lines.size && col >= 0
        override fun next() = lines[row++][col--]
    }

    inner class DiagonalRLBackwardsIterator(private var row: Int, private var col: Int) : Iterator<Char> {
        override fun hasNext() = row >= 0 && col < lines[row].length
        override fun next() = lines[row--][col++]
    }

    fun horizontalForwardsIterators() = lines.indices.map { HorizontalForwardsIterator(it) }
    fun horizontalBackwardsIterators() = lines.indices.map { HorizontalBackwardsIterator(it) }
    fun verticalForwardsIterators() = lines[0].indices.map { VerticalForwardsIterator(it) }
    fun verticalBackwardsIterators() = lines[0].indices.map { VerticalBackwardsIterator(it) }

    fun diagonalLRForwardsIterators(): List<DiagonalLRForwardsIterator> {
        return lines.indices.reversed().map { DiagonalLRForwardsIterator(it, 0) } + //
                lines[0].indices.drop(1).map { DiagonalLRForwardsIterator(0, it) }
    }

    fun diagonalLRBackwardsIterators(): List<DiagonalLRBackwardsIterator> {
        return lines.indices.map { DiagonalLRBackwardsIterator(it, lines[it].length - 1) } + //
                lines[0].indices.reversed().drop(1).map { DiagonalLRBackwardsIterator(lines.size - 1, it) }
    }

    fun diagonalRLForwardsIterators(): List<DiagonalRLForwardsIterator> {
        return lines[0].indices.map { DiagonalRLForwardsIterator(0, it) } + //
                lines.indices.drop(1).map { DiagonalRLForwardsIterator(it, lines[it].length - 1) }
    }

    fun diagonalRLBackwardsIterators(): List<DiagonalRLBackwardsIterator> {
        return lines.indices.map { DiagonalRLBackwardsIterator(it, 0) } + //
                lines[0].indices.drop(1).map { DiagonalRLBackwardsIterator(lines.size - 1, it) }
    }

    fun iterators(): List<Iterator<Char>> {
        return horizontalForwardsIterators() +   //
                horizontalBackwardsIterators() + //
                verticalForwardsIterators() +    //
                verticalBackwardsIterators() +   //
                diagonalLRForwardsIterators() +  //
                diagonalLRBackwardsIterators() + //
                diagonalRLForwardsIterators() +  //
                diagonalRLBackwardsIterators()
    }

    fun isXMas(row: Int, col: Int): Boolean {
        if (row <= 0 || col <= 0 || row >= lines.size - 1 || col >= lines[row].length - 1) {
            return false
        }

        if (lines[row][col] != 'A') {
            return false
        }

        val topLeft = lines[row - 1][col - 1]
        val topRight = lines[row - 1][col + 1]
        val bottomLeft = lines[row + 1][col - 1]
        val bottomRight = lines[row + 1][col + 1]

        return ((topLeft == 'M' && bottomRight == 'S') || (topLeft == 'S' && bottomRight == 'M')) && //
                ((topRight == 'M' && bottomLeft == 'S') || (topRight == 'S' && bottomLeft == 'M'))
    }

    fun countXMas(): Int {
        var count = 0
        for (row in 1..lines.size - 1) {
            for (col in 1..lines[row].length - 1) {
                if (lines[row][col] == 'A' && isXMas(row, col)) {
                    count++
                }
            }
        }
        return count
    }
}

fun task01(grid: Grid): Int {
    val strings = grid.iterators().map { it.asSequence().joinToString("") }
    return strings.sumOf { "XMAS".toRegex().findAll(it).count() }
}

fun task02(grid: Grid): Int {
    return grid.countXMas()
}

class Day04 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val grid = Grid(reader.readLines())

        println("Day 04 Task 1: ${task01(grid)}")
        println("Day 04 Task 2: ${task02(grid)}")
    }
}

fun main(args: Array<String>) = Day04().main(args)
