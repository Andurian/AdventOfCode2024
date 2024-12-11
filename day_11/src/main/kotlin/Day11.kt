package meow.andurian.aoc2024.day_11

import meow.andurian.aoc2024.utils.readResourceAsLines

fun applyRule(stone : Long) : List<Long>{
    if(stone == 0L) return listOf(1)
    val s = stone.toString()
    if (s.length % 2 == 0){
        return listOf(s.take(s.length/2).toLong(), s.drop(s.length/2).toLong())
    }
    return listOf(stone * 2024)
}

fun step(stones : List<Long>) : List<Long>{
    val ret = mutableListOf<Long>()
    for(i in stones){
        ret.addAll(applyRule(i))
    }
    return ret
}

fun step(stones : Map<Long, Long>) : Map<Long, Long>{
    val ret = mutableMapOf<Long, Long>()
    for((k, v) in stones){
        for(s in applyRule(k)){
            ret.merge(s, v, Long::plus)
        }
    }
    return ret
}

fun numStonesAfterIterations(initialStones : List<Long>, iterations : Int) : Int{
    var stones = initialStones
    for(i in 0 until iterations){
        stones = step(stones)
    }
    return stones.size
}

fun numStonesAfterIterations(initialStones : Map<Long, Long>, iterations : Int) : Long{
    var stones = initialStones
    for(i in 0 until iterations){
        stones = step(stones)
    }
    return stones.values.sum()
}


fun toMap(stones : List<Long>) : Map<Long, Long>{
    val ret = mutableMapOf<Long, Long>()
    for(s in stones){
        ret.merge(s, 1, Long::plus)
    }
    return ret
}

fun main() {
    val lines = readResourceAsLines("/test_input.txt")
    val stones = lines[0].split(" ").map { it.toLong() }
    val stonesMap = toMap(stones)

    println("Day 11 Task 1: ${numStonesAfterIterations(stones, 25)} (slow)")
    println("Day 11 Task 1: ${numStonesAfterIterations(stonesMap, 25)} (fast)")
    println("Day 11 Task 2: ${numStonesAfterIterations(stonesMap, 75)}")
}