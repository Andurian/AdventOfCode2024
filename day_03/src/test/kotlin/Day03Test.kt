package meow.andurian.aoc2024.day_03

import kotlin.test.Test
import kotlin.test.assertEquals

import meow.andurian.aoc2024.utils.resourceReader

internal class Day31Test {

    @Test
    fun testTask01() {
        val input = resourceReader("/test_input.txt").readLine()
        assertEquals(task01(input), 161)
    }

    @Test
    fun testTask02() {
        val input = resourceReader("/test_input_2.txt").readLine()
        assertEquals(task02(input), 48)
    }
}