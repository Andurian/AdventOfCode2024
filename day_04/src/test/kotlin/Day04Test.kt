package meow.andurian.aoc2024.day_04

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day04Test {
    private val grid = Grid(resourceReader("/test_input.txt").readLines())

    @Test
    fun testTask01() {
        assertEquals(task01(grid), 18)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(grid), 9)
    }
}