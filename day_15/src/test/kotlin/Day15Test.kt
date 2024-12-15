package meow.andurian.aoc2024.day_15

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day15Test {
    val lines = resourceReader("/test_input.txt").readLines()
    val linesSmall = resourceReader("/test_input_small.txt").readLines()
    val linesBigBox = resourceReader("/test_input_bigbox.txt").readLines()

    @Test
    fun testTask01() {
        assertEquals(task01(lines), 10092)
        assertEquals(task01(linesSmall), 2028)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(lines), 9021)
        assertEquals(task02(linesSmall), 1751)
        assertEquals(task02(linesBigBox), 618)
    }
}