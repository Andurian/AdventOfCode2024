package meow.andurian.aoc2024.day_23

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day23Test {
    val lines = resourceReader("/test_input.txt").readLines()
    val graph = Graph.fromInput(lines)

    @Test
    fun testTask01() {
        assertEquals(task01(graph), 7)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(graph), "co,de,ka,ta")
    }
}