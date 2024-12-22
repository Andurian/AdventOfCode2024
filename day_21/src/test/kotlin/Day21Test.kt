package meow.andurian.aoc2024.day_21

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day21Test {
    val lines = resourceReader("/test_input.txt").readLines()

    @Test
    fun testTask() {
        assertEquals(task(lines, 2), 126384)
    }

    @Test
    fun testTaskDynamic() {
        assertEquals(taskDynamic(lines, 2), 126384)
    }
}