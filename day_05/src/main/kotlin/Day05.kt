package meow.andurian.aoc2024.day_05

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import java.io.BufferedReader

enum class State {
    Valid, Invalid
}

fun filterUpdates(rules: List<Rule>, updates: List<List<Int>>, state: State): List<List<Int>> {
    return when (state) {
        State.Valid -> updates.filter { update -> rules.all { rule -> rule.check(update) } }
        State.Invalid -> updates.filter { update -> rules.any { rule -> !rule.check(update) } }
    }
}

fun rebuildSequence(rules: List<Rule>, pageNumbers: List<Int>): List<Int> {

    // Inner function to avoid carrying the rules around
    fun buildRecursive(masterSequence: List<Int>, numbersToAdd: List<Int>): List<Int>? {
        if (numbersToAdd.isEmpty()) {
            return masterSequence
        }

        var sequence = masterSequence.toMutableList()
        val number = numbersToAdd.first()

        for (i in 0..masterSequence.size) {
            sequence.add(i, number)
            if (rules.all { it.check(sequence) }) {
                val completeSequence = buildRecursive(sequence, numbersToAdd.drop(1))
                if (completeSequence != null) {
                    return completeSequence
                }
            }
            sequence.removeAt(i)
        }
        return null
    }

    return buildRecursive(pageNumbers.take(1), pageNumbers.drop(1))!!
}

fun task01(rules: List<Rule>, updates: List<List<Int>>): Int {
    return filterUpdates(rules, updates, State.Valid).sumOf { it[it.size / 2] }
}

fun task02(rules: List<Rule>, updates: List<List<Int>>): Int {
    return filterUpdates(rules, updates, State.Invalid).map { rebuildSequence(rules, it) }.sumOf { it[it.size / 2] }
}

class Day05 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()
        val rules = lines.takeWhile { it.isNotBlank() }.map { Rule(it) }
        var pageNumbers = lines.takeLastWhile { it.isNotBlank() }.map { it.split(",").map { it.toInt() } }

        println("Day 05 Task 1: ${task01(rules, pageNumbers)}")
        println("Day 05 Task 2: ${task02(rules, pageNumbers)}")
    }
}

fun main(args: Array<String>) = Day05().main(args)
