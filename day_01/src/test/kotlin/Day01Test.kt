package meow.andurian.aoc2024.day_01

import kotlin.test.Test
import kotlin.test.assertEquals

import meow.andurian.aoc2024.utils.resourceReader

internal class Day01Test {
    private val lines  = resourceReader("/test_input.txt").readLines()

    @Test
    fun testTask01() {
        assertEquals(task01(lines), 11)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(lines), 31)
    }
}