package meow.andurian.aoc2024.day_02

import kotlin.test.Test
import kotlin.test.assertEquals

import meow.andurian.aoc2024.utils.resourceReader

internal class Day02Test {

    private val reports = resourceReader("/test_input.txt").readLines().map{ it.split(" ").map{ it.toInt() } }

    @Test
    fun testTask01() {
        assertEquals(task01(reports), 2)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(reports), 4)
    }

}