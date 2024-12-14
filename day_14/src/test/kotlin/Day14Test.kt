package meow.andurian.aoc2024.day_14

import kotlin.test.Test
import kotlin.test.assertEquals

import meow.andurian.aoc2024.utils.resourceReader

internal class Day14Test {
    val robots = resourceReader("/test_input.txt").readLines().map { Robot.fromString(it) }

    @Test
    fun testTask01() {
        assertEquals(task01(robots, 7, 11), 12)
    }

    // Task 2 cannot be solved on sample input
}