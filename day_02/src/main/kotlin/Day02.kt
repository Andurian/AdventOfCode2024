package meow.andurian.aoc2024.day_02

import meow.andurian.aoc2024.utils.readResourceAsLines

fun isReportSafe(report : List<Int>) : Boolean {
    val deltas = (1..report.size - 1).map{ report[it] - report[it - 1] }
    return deltas.all { it >= 1 && it <= 3 } || deltas.all { it >= -3 && it <= -1}
}

fun isReportSafeWithDampener(report : List<Int>) : Boolean {
    for(i in report.indices) {
        val patechedReport = report.filterIndexed { index, element -> index != i  }
        if(isReportSafe(patechedReport)){
            return true
        }
    }
    return false
}

fun task01(reports : List<List<Int>>) : Int{
    return reports.count { isReportSafe(it) }
}

fun task02(reports : List<List<Int>>) : Int{
    return reports.count { isReportSafeWithDampener(it) }
}

fun main() {
    val lines = readResourceAsLines("/input_mm.txt")
    val reports = lines.map{ it.split(" ").map{ it.toInt() } }

    println("Day 02 Task 1: ${task01(reports)}")
    println("Day 02 Task 1: ${task02(reports)}")
}