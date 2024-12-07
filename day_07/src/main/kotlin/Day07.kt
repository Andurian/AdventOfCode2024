package meow.andurian.aoc2024.day_07

import meow.andurian.aoc2024.utils.readResourceAsLines
import kotlin.math.log10
import kotlin.math.floor
import kotlin.math.pow

enum class Operator {
    Add {
        override fun calc(a: Long, b: Long) = a + b
    },
    Mul {
        override fun calc(a: Long, b: Long) = a * b
    },
    Cat {
        override fun calc(a: Long, b: Long) = a * 10.0.pow(floor(log10(b.toDouble())) + 1).toLong() + b
    };

    abstract fun calc(a: Long, b: Long): Long
}

fun operatorLists(len: Int, withCat: Boolean) = sequence {
    suspend fun SequenceScope<List<Operator>>.make(ops: List<Operator> = emptyList<Operator>()) {
        if (ops.size >= len) {
            yield(ops)
        } else {
            make(ops + listOf(Operator.Add))
            make(ops + listOf(Operator.Mul))
            if (withCat) {
                make(ops + listOf(Operator.Cat))
            }
        }
    }
    make()
}

fun eval(components: List<Long>, ops: List<Operator>): Long {
    return components.drop(1).foldIndexed(components.first(), { i, current, next -> ops[i].calc(current, next) })
}

class Equation(val result: Long, val components: List<Long>) {
    fun waysToSolve(withCat: Boolean): List<List<Operator>> {
        val ret = mutableListOf<List<Operator>>()

        for (ops in operatorLists(components.size - 1, withCat)) {
            if (eval(components, ops) == result) {
                ret.add(ops)
            }
        }
        return ret
    }

    companion object {
        fun fromLine(line: String): Equation {
            val tokens = line.split(" ")
            val result = tokens[0].dropLast(1).toLong()
            val components = tokens.drop(1).map { it.toLong() }
            return Equation(result, components)
        }
    }
}

fun sumValidResults(equations: List<Equation>, withCat: Boolean): Long {
    return equations.filter { it.waysToSolve(withCat).isNotEmpty() }.sumOf { it.result }
}

fun main() {
    val lines = readResourceAsLines("/test_input.txt")
    val equations = lines.map { Equation.fromLine(it) }

    println("Day 07 Task 1: ${sumValidResults(equations, false)}")
    println("Day 07 Task 2: ${sumValidResults(equations, true)}")
}