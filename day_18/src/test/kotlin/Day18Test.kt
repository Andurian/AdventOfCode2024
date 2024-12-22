package meow.andurian.aoc2024.day_18

import meow.andurian.aoc2024.utils.Extent
import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day18Test {
    val lines = resourceReader("/test_input.txt").readLines()

    @Test
    fun testTask01() {
        assertEquals(task01(lines, Extent(7, 7), 12), 22)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(lines, Extent(7, 7), 12), "6,1")
    }
}