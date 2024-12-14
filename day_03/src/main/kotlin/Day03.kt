package meow.andurian.aoc2024.day_03

import java.io.BufferedReader

import com.github.ajalt.clikt.core.main

import meow.andurian.aoc2024.utils.AoCDay

fun executeMulInstruction(instruction: String): Int {
    val exp = """\d{1,3}""".toRegex()
    val matches = exp.findAll(instruction)
    assert(matches.count() == 2)
    return matches.fold(1, { x, res -> x * res.value.toInt() })
}

fun task01(line: String): Int {
    val exp = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
    val matches = exp.findAll(line)
    return matches.fold(0, { x, res -> x + executeMulInstruction(res.value) })
}

fun task02(line: String): Int {
    val exp = """(mul\(\d{1,3},\d{1,3}\))|(do(n't)?\(\))""".toRegex()
    val matches = exp.findAll(line)
    val activeInstructions = mutableListOf<String>()
    var active = true
    for (match in matches){
        when(match.value){
            "do()" -> active = true
            "don't()" -> active = false
            else -> if (active) activeInstructions.add(match.value)
        }
    }
    return activeInstructions.fold(0) { x, res -> x + executeMulInstruction(res) }
}

class Day03 : AoCDay() {
    override fun testInput() = "/test_input_2.txt"

    override fun solve(reader: BufferedReader) {
        val line = reader.readLines().joinToString("")

        println("Day 03 Task 1: ${task01(line)}")
        println("Day 03 Task 2: ${task02(line)}")
    }
}

fun main(args : Array<String>) = Day03().main(args)
