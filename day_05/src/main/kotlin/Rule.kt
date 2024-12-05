package meow.andurian.aoc2024.day_05

import meow.andurian.aoc2024.utils.indexesOf

class Rule {
    val before: Int
    val after: Int

    constructor(instruction: String) {
        val tokens = instruction.split("|")
        this.before = tokens[0].toInt()
        this.after = tokens[1].toInt()
    }

    fun check(pageNumbers: List<Int>): Boolean {
        val matchBefore = pageNumbers.indexesOf { it == this.before }
        val matchAfter = pageNumbers.indexesOf { it == this.after }

        if (matchBefore.size > 1 || matchAfter.size > 1) {
            throw IllegalArgumentException("Page numbers should appear only once")
        }

        if (matchBefore.isEmpty() || matchAfter.isEmpty()) {
            return true
        }

        return matchBefore[0] < matchAfter[0]
    }
}