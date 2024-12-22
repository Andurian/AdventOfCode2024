package meow.andurian.aoc2024.day_21

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.Direction
import meow.andurian.aoc2024.utils.stepN
import java.io.BufferedReader


fun toString(movements: List<List<Direction>>): String {
    var ret = ""
    for (movementInstance in movements) {
        for (dir in movementInstance) {
            ret += dir.toChar()
        }
        ret += 'A'
    }
    return ret
}

fun <T> movementsForKeys(keysToPress: List<Key<T>>, pad: KeyPad<T>): List<List<Direction>> {
    return (1 until keysToPress.size).map { pad.shortestPath(keysToPress[it - 1], keysToPress[it]) }
}

fun sequenceInfo(s: String) {
    println("${s.length}[A = ${s.count { it == 'A' }}, ^ = ${s.count { it == '^' }}, < = ${s.count { it == '<' }}, v = ${s.count { it == 'v' }}, > = ${s.count { it == '>' }}] -> $s")
}

fun nextSequence(s: String): String {
    val arrowKeysToPress = "A$s".map(::toArrowKey)
    val movements = movementsForKeys(arrowKeysToPress, ArrowPad.getInstance())
    return toString(movements)
}

fun iterateSequence(input: String, iterations: Int): String {
    val numKeysToPress = "A$input".map(::toNumberKey)
    val movements = movementsForKeys(numKeysToPress, NumPad.getInstance())

    return stepN(toString(movements), ::nextSequence, iterations)
}

fun iterateDynamicSequence(input: String, iterations: Int): DynamicSequence {
    val numKeysToPress = "A$input".map(::toNumberKey)
    val movements = movementsForKeys(numKeysToPress, NumPad.getInstance())

    return stepN(DynamicSequence(toString(movements)), {it.next()}, iterations)
}

fun processLine(line: String, iterations: Int): Long {
    val movements = iterateSequence(line, iterations)
    val number = line.dropLast(1).toLong()
    return number * movements.length
}

fun processLineDynamic(line: String, iterations: Int): Long {
    val movements = iterateDynamicSequence(line, iterations)
    val number = line.dropLast(1).toInt()
    return number * movements.totalSize()
}

fun task(lines: List<String>, iterations: Int): Long {
    return lines.sumOf { processLine(it, iterations) }
}

fun taskDynamic(lines: List<String>, iterations: Int): Long {
    return lines.sumOf { processLineDynamic(it, iterations) }
}

class Day21 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()

        println("Day 21 Task 1: ${task(lines, 2)}")
        println("Day 21 Task 1: ${taskDynamic(lines, 2)} (dyn)")
        println("Day 21 Task 2: ${taskDynamic(lines, 25)}")
    }
}

fun main(args: Array<String>) = Day21().main(args)