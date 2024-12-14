package meow.andurian.aoc2024.day_13

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day13Test {
    val machines = resourceReader("/test_input.txt").readLines().chunked(4).map { Machine.fromLines(it) }

    @Test
    fun testTask01() {
        assertEquals(task01(machines), 480)
        assertEquals(task01Smart(machines), 480)
    }

    // No sample output is given for task 2
}