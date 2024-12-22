package meow.andurian.aoc2024.day_19

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import java.io.BufferedReader

fun canMake(target: String, towels: List<String>): Boolean {
    fun dfs(current: List<String>): Boolean {
        val currentString = current.joinToString("")
        if (currentString == target) {
            return true
        }

        for (s in towels) {
            val nextString = currentString + s
            if (nextString == target.take(nextString.length)) {
                if (dfs(current + s)) return true
            }
        }
        return false
    }
    return dfs(emptyList<String>())
}

fun waysToMakeDyn(target: String, towels: List<String>): Long {
    val waysToMake = MutableList<Long>(target.length + 1) { 0 }

    fun dfs(subtarget: String): Long {
        var ret = 0L
        for (s in towels) {
            if (s != subtarget.take(s.length)) continue
            val remaining = subtarget.drop(s.length)
            if (remaining.isEmpty()) {
                ret += 1
            } else {
                ret += waysToMake[remaining.length]
            }

        }
        return ret
    }

    for (l in 1..target.length) {
        val subtarget = target.drop(target.length - l)
        waysToMake[l] = dfs(subtarget)
    }
    return waysToMake.last()
}

fun task01(targets: List<String>, towels: List<String>) = targets.count { canMake(it, towels) }

fun task02(targets: List<String>, towels: List<String>) = targets.sumOf { waysToMakeDyn(it, towels) }

class Day19 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()
        val towels = lines[0].split(", ")
        val targets = lines.drop(2)

        println("Day 19 Task 1: ${task01(targets, towels)}")
        println("Day 19 Task 2: ${task02(targets, towels)}")
    }
}

fun main(args: Array<String>) = Day19().main(args)