package meow.andurian.aoc2024.day_11

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.counts
import meow.andurian.aoc2024.utils.stepN
import java.io.BufferedReader

fun applyRule(stone: Long): List<Long> {
    if (stone == 0L) return listOf(1)
    val s = stone.toString()
    if (s.length % 2 == 0) {
        return listOf(s.take(s.length / 2).toLong(), s.drop(s.length / 2).toLong())
    }
    return listOf(stone * 2024)
}

fun step(stones: List<Long>): List<Long> {
    val ret = mutableListOf<Long>()
    for (i in stones) {
        ret.addAll(applyRule(i))
    }
    return ret
}

fun step(stones: Map<Long, Long>): Map<Long, Long> {
    val ret = mutableMapOf<Long, Long>()
    for ((k, v) in stones) {
        for (s in applyRule(k)) {
            ret.merge(s, v, Long::plus)
        }
    }
    return ret
}

fun numStonesAfterIterations(stones: List<Long>, iterations: Int): Int {
    return stepN(stones, ::step, iterations).size
}

fun numStonesAfterIterations(stones: Map<Long, Long>, iterations: Int): Long {
    return stepN(stones, ::step, iterations).values.sum()
}

class Day11 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val stones = reader.readLine().split(" ").map { it.toLong() }
        val stonesMap = counts(stones)

        println("Day 11 Task 1: ${numStonesAfterIterations(stones, 25)} (slow)")
        println("Day 11 Task 1: ${numStonesAfterIterations(stonesMap, 25)} (fast)")
        println("Day 11 Task 2: ${numStonesAfterIterations(stonesMap, 75)}")
    }
}

fun main(args: Array<String>) = Day11().main(args)