package meow.andurian.aoc2024.day_01

import kotlin.test.Test
import kotlin.test.assertEquals

import meow.andurian.aoc2024.utils.readResourceAsLines

internal class Day01Test {

    @Test
    fun testTask01() {
        val lines = readResourceAsLines("/test_input.txt")
        assertEquals(task01(lines), 11)
    }

    @Test
    fun testTask02() {
        val lines = readResourceAsLines("/test_input.txt")
        assertEquals(task02(lines), 31)
    }

}