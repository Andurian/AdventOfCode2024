package meow.andurian.aoc2024.day_11

import meow.andurian.aoc2024.utils.counts
import meow.andurian.aoc2024.utils.resourceReader
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day11Test {
    val stones = resourceReader("/test_input.txt").readLine().split(" ").map { it.toLong() }
    val stonesMap = counts(stones)

    @Test
    fun test() {
        // There is no sample output given for task 2
        assertEquals(numStonesAfterIterations(stones, 6), 22)
        assertEquals(numStonesAfterIterations(stonesMap, 6), 22)

        assertEquals(numStonesAfterIterations(stones, 25), 55312)
        assertEquals(numStonesAfterIterations(stonesMap, 25), 55312)
    }
}