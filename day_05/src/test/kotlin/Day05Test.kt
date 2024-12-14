package meow.andurian.aoc2024.day_05

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day05Test {
    val lines = resourceReader("/test_input.txt").readLines()
    val rules = lines.takeWhile { it.isNotBlank() }.map { Rule(it) }
    var pageNumbers = lines.takeLastWhile { it.isNotBlank() }.map { it.split(",").map { it.toInt() } }

    @Test
    fun testTask01() {
        assertEquals(task01(rules, pageNumbers), 143)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(rules, pageNumbers), 123)
    }
}