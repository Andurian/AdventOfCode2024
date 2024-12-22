package meow.andurian.aoc2024.day_22

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.stepN
import java.io.BufferedReader

fun mixAndPrune(secretNumber: Long, x: Long): Long {
    return (secretNumber xor x) % 16777216
}

fun nextSecretNumber(secretNumberIn: Long): Long {
    var secretNumber = secretNumberIn
    secretNumber = mixAndPrune(secretNumber, secretNumber * 64L)
    secretNumber = mixAndPrune(secretNumber, secretNumber / 32L)
    secretNumber = mixAndPrune(secretNumber, secretNumber * 2048)
    return secretNumber
}

class Monkey {
    val secretNumbers: List<Long>
    val offers: List<Int>
    val diffs: List<Int>
    val changesToPrice: Map<List<Int>, Int>

    constructor(secretNumber: Long) {
        val secretNumbers = mutableListOf<Long>()
        val offers = mutableListOf<Int>()
        val diffs = mutableListOf<Int>()
        val changesToPrice = mutableMapOf<List<Int>, Int>()

        secretNumbers.add(secretNumber)
        offers.add((secretNumber % 10).toInt())
        diffs.add(Int.MAX_VALUE)

        for (i in 1 until 2000) {
            secretNumbers.add(nextSecretNumber(secretNumbers.last()))
            offers.add((secretNumbers.last() % 10).toInt())
            diffs.add(offers[i] - offers[i - 1])

            if (i >= 4) {
                val changes = diffs.takeLast(4)
                if (!changesToPrice.containsKey(changes)) {
                    changesToPrice[changes] = offers.last()
                }
            }

        }

        this.secretNumbers = secretNumbers
        this.offers = offers
        this.diffs = diffs
        this.changesToPrice = changesToPrice
    }
}

fun generateSequences(): List<List<Int>> {
    val ret = mutableListOf<List<Int>>()
    for (i in -9..9) {
        for (j in -9..9) {
            for (k in -9..9) {
                for (l in -9..9) {
                    ret.add(listOf(i, j, k, l))
                }
            }
        }
    }
    return ret
}

fun task01(lines: List<String>): Long {
    return lines.sumOf { stepN(it.toLong(), ::nextSecretNumber, 2000) }
}

fun task02(lines: List<String>): Int {
    val sequences = generateSequences()
    val monkeys = lines.map { Monkey(it.toLong()) }

    fun bananas(sequence: List<Int>) = monkeys.sumOf{it.changesToPrice.getOrElse(sequence){0} }

    return sequences.maxOf(::bananas)
}

class Day22 : AoCDay() {
    override fun testInput() = "/test_input_2.txt"
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()

        println("Day 22 Task 1: ${task01(lines)}")
        println("Day 22 Task 2: ${task02(lines)}")
    }
}

fun main(args: Array<String>) = Day22().main(args)