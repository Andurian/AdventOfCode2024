package meow.andurian.aoc2024.day_24

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day24Test {
    val linesSmall = resourceReader("/test_input_small.txt").readLines()
    val linesLarge = resourceReader("/test_input_large.txt").readLines()

    @Test
    fun testTask01() {
        assertEquals(task01(linesSmall), 4)
        assertEquals(task01(linesLarge), 2024)
    }

}