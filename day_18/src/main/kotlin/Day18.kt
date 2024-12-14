package meow.andurian.aoc2024.day_18

import java.io.BufferedReader

import com.github.ajalt.clikt.core.main

import meow.andurian.aoc2024.utils.AoCDay

fun task01(){

}

fun task02(){

}

class Day18 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()

        println("Day 18 Task 1: ${task01()}")
        println("Day 18 Task 2: ${task02()}")
    }
}

fun main(args : Array<String>) = Day18().main(args)