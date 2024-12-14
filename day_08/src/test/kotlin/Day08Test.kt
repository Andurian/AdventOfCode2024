package meow.andurian.aoc2024.day_08

import kotlin.test.Test
import kotlin.test.assertEquals

import meow.andurian.aoc2024.utils.resourceReader

internal class Day08Test {
    val lines3a = resourceReader("/test_input_1.txt") .readLines()
    val lines3T = resourceReader("/test_input_3.txt") .readLines()
    val linesBigSample = resourceReader("/test_input_2.txt").readLines()

    @Test
    fun testTask01() {
        assertEquals(task01(lines3a), 4)
        assertEquals(task01(linesBigSample), 14)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(lines3T), 9)
        assertEquals(task02(linesBigSample), 34)
    }
}