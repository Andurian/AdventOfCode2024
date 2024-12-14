package meow.andurian.aoc2024.day_10

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day10Test {
    val map = TopologicalMap.fromInput(resourceReader("/test_input.txt").readLines())

    @Test
    fun testTask01() {
        assertEquals(task01(map), 36)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(map), 81)
    }
}