package meow.andurian.aoc2024.day_20

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day20Test {
    val lines = resourceReader("/test_input.txt").readLines()
    val maze = Maze(lines)

    @Test
    fun testTask01() {
        assertEquals(task01(maze, 2), 44)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(maze, 50), 285)
    }
}