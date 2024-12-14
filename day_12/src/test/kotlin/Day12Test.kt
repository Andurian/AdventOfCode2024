package meow.andurian.aoc2024.day_12

import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day12Test {
    val grid4x4 = Grid<Char>(resourceReader("/test_input_1.txt").readLines()) { it }
    val gridOXO = Grid<Char>(resourceReader("/test_input_2.txt").readLines()) { it }
    val gridBig = Grid<Char>(resourceReader("/test_input_3.txt").readLines()) { it }
    val gridEEX = Grid<Char>(resourceReader("/test_input_4.txt").readLines()) { it }
    val gridABA = Grid<Char>(resourceReader("/test_input_5.txt").readLines()) { it }


    @Test
    fun testTask01() {
        assertEquals(task01(grid4x4), 140)
        assertEquals(task01(gridOXO), 772)
        assertEquals(task01(gridBig), 1930)
    }

    @Test
    fun testTask02() {
        assertEquals(task02(grid4x4), 80)
        assertEquals(task02(gridOXO), 436)
        assertEquals(task02(gridEEX), 236)
        assertEquals(task02(gridABA), 368)
        assertEquals(task02(gridBig), 1206)
    }
}