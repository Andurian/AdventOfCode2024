package meow.andurian.aoc2024.day_02

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import java.io.BufferedReader

fun isReportSafe(report: List<Int>): Boolean {
    val deltas = (1..report.size - 1).map { report[it] - report[it - 1] }
    return deltas.all { it >= 1 && it <= 3 } || deltas.all { it >= -3 && it <= -1 }
}

fun isReportSafeWithDampener(report: List<Int>): Boolean {
    for (i in report.indices) {
        val patechedReport = report.filterIndexed { index, element -> index != i }
        if (isReportSafe(patechedReport)) {
            return true
        }
    }
    return false
}

fun task01(reports: List<List<Int>>): Int {
    return reports.count { isReportSafe(it) }
}

fun task02(reports: List<List<Int>>): Int {
    return reports.count { isReportSafeWithDampener(it) }
}

class Day02 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val reports = reader.readLines().map { it.split(" ").map { it.toInt() } }

        println("Day 02 Task 1: ${task01(reports)}")
        println("Day 02 Task 1: ${task02(reports)}")
    }
}

fun main(args: Array<String>) = Day02().main(args)
