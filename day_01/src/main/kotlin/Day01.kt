package meow.andurian.aoc2024.day_01

import kotlin.math.abs

import meow.andurian.aoc2024.utils.readResourceAsLines

fun toListsOfSortedInts(lines: List<String>) : Pair<List<Int>, List<Int>>{
    val leftList = MutableList<Int>(lines.size) { 0 }
    val rightList = MutableList<Int>(lines.size) { 0 }

    for((i, line) in lines.withIndex()) {
        val tokens = line.split("\\s+".toRegex())
        leftList[i] = tokens[0].toInt()
        rightList[i] = tokens[1].toInt()
    }

    leftList.sort()
    rightList.sort()

    return Pair(leftList, rightList)
}

fun task01(lines: List<String>) : Int {
    val (leftList, rightList) = toListsOfSortedInts(lines)

    var sum = 0
    for(i in leftList.indices) {
        sum += abs(leftList[i] - rightList[i])
    }
    return sum
}

fun task02(lines: List<String>) : Int {
    val (leftList, rightList) = toListsOfSortedInts(lines)

    val counts = mutableMapOf<Int, Int>()
    for(x in rightList){
        counts[x] = 1 + counts.getOrDefault(x, 0)
    }

    var sum = 0
    for(x in leftList){
        sum += x * counts.getOrDefault(x, 0)
    }

    return sum
}


fun main() {
    val lines = readResourceAsLines("/input_mm.txt")
    println("Solution Day 01 Task 1: ${task01(lines)}")
    println("Solution Day 01 Task 2: ${task02(lines)}")

}
