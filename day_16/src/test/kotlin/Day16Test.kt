package meow.andurian.aoc2024.day_16

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day16Test {
    val paths1 = Maze(resourceReader("/test_input_1.txt").readLines()).shortestPaths()
    val paths2 = Maze(resourceReader("/test_input_2.txt").readLines()).shortestPaths()

    @Test
    fun testTask01() {
        assertEquals(task01(paths1), 7036)
        assertEquals(task01(paths2), 11048)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(paths1), 45)
        assertEquals(task02(paths2), 64)
    }
}