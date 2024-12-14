package meow.andurian.aoc2024.day_07

import kotlin.test.Test
import kotlin.test.assertEquals

import meow.andurian.aoc2024.utils.resourceReader

internal class Day07Test {
    val equations = resourceReader("/test_input.txt").readLines().map { Equation.fromLine(it) }

    @Test
    fun testTask01() {
        assertEquals(sumValidResults(equations, false), 3749)
    }

    @Test
    fun testTask02() {
        assertEquals(sumValidResults(equations, true), 11387)
    }
}