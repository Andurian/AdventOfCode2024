package meow.andurian.aoc2024.day_23

import java.io.BufferedReader

import com.github.ajalt.clikt.core.main

import meow.andurian.aoc2024.utils.AoCDay

fun task01(){

}

fun task02(){

}

class Day23 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()

        println("Day 23 Task 1: ${task01()}")
        println("Day 23 Task 2: ${task02()}")
    }
}

fun main(args : Array<String>) = Day23().main(args)