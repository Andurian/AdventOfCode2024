package meow.andurian.aoc2024.day_17

import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day17Test {

    @Test
    fun testTask01() {
        val interpreter = Interpreter(729, 0, 0)
        val program = listOf<Long>(0, 1, 5, 4, 3, 0)

        assertEquals(task01(interpreter, program), "4,6,3,5,6,3,5,2,1,0")
    }

    @Test
    fun testTask02() {
        val interpreter = Interpreter(0, 0, 0)
        val program = listOf<Long>(0, 3, 5, 4, 3, 0)

        assertEquals(task02(interpreter, program), 117440)
    }
}