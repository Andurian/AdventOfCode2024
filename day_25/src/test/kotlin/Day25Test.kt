package meow.andurian.aoc2024.day_25

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day25Test {
    val lines = resourceReader("/test_input.txt").readLines()

    @Test
    fun testTask01() {
        val (keys, locks) = parseInput(lines)
        assertEquals(task01(keys, locks), 3)
    }
}