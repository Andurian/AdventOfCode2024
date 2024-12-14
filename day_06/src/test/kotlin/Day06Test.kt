package meow.andurian.aoc2024.day_06

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day06Test {
    val lines = resourceReader("/test_input.txt").readLines()
    val grid = Grid(lines)
    val guard = Guard(lines)

    @Test
    fun testTask01() {
        assertEquals(task01(grid, guard), 41)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(grid, guard), 6)
    }
}