package meow.andurian.aoc2024.day_22

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day22Test {
    val lines1 = resourceReader("/test_input_1.txt").readLines()
    val lines2 = resourceReader("/test_input_2.txt").readLines()

    @Test
    fun testTask01() {
        assertEquals(task01(lines1), 37327623)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(lines2), 23)
    }
}