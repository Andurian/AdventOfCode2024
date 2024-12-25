package meow.andurian.aoc2024.day_25

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import java.io.BufferedReader

typealias Key = List<Int>
typealias Lock = List<Int>

fun toKey(lines : List<String>) : Key {
    assert(lines.first().all{it == '.'})
    val ret = mutableListOf<Int>()
    for(col in 0 until lines[0].length){
        var curr = 5
        for(row in 1 until lines.size){
            if(lines[row][col] == '#') break
            curr --
        }
        ret.add(curr)
    }
    return ret
}

fun toLock(lines : List<String>) : Lock{
    assert(lines.last().all{it == '.'})
    val ret = mutableListOf<Int>()
    for(col in 0 until lines[0].length){
        var curr = 0
        for(row in 1 until lines.size){
            if(lines[row][col] == '.') break
            curr++
        }
        ret.add(curr)
    }
    return ret
}

fun parseInput(lines : List<String>): Pair<List<Key>, List<Lock>>{
    val keys = mutableListOf<Key>()
    val locks = mutableListOf<Lock>()

    var inp = lines
    while(inp.isNotEmpty()){
        val chunk = inp.takeWhile{it.isNotEmpty()}
        inp = inp.drop(chunk.size + 1)
        if(chunk.first().all{it == '.'}){
            keys.add(toKey(chunk))
        }else{
            locks.add(toLock(chunk))
        }
    }
    return Pair(keys, locks)
}

fun task01(keys : List<Key>, locks : List<Lock>) : Int {
    var ret = 0
    for(k in keys){
        for (l in locks){
            if((0 until k.size).all{k[it] + l[it] <= 5}) ret++
        }
    }
    return ret

}

class Day25 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()
        val (keys, locks) = parseInput(lines)

        println("Day 25 Task 1: ${task01(keys, locks)}")
        // There is no Task 2 on Day 25
    }
}

fun main(args: Array<String>) = Day25().main(args)