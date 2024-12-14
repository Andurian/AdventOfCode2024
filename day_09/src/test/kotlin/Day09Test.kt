package meow.andurian.aoc2024.day_09

import kotlin.test.Test
import kotlin.test.assertEquals

import meow.andurian.aoc2024.utils.resourceReader

internal class Day09Test {
    val blocksSimple = parseData(resourceReader("/test_input_1.txt").readLine())
    val blocks = parseData(resourceReader("/test_input_2.txt").readLine())

    @Test
    fun testTask01() {
        assertEquals(task01(blocksSimple), 60)
        assertEquals(task01(blocks), 1928)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(blocksSimple), 132)
        assertEquals(task02(blocks), 2858)
    }
}