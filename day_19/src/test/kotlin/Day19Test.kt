package meow.andurian.aoc2024.day_19

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day19Test {
    val lines = resourceReader("/test_input.txt").readLines()
    val towels = lines[0].split(", ")
    val targets = lines.drop(2)

    @Test
    fun testTask01() {
        assertEquals(task01(targets, towels), 6)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(targets, towels), 16)
    }
}