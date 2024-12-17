package meow.andurian.aoc2024.day_17

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.exp
import java.io.BufferedReader
import kotlin.reflect.KMutableProperty1


class Interpreter(var A: Long, var B: Long, var C: Long) {
    var debug = false
    val stdout = mutableListOf<Long>()
    private var ip: Long = 0

    abstract inner class Operation {
        var ipOffset = 2
        abstract fun exec(op: Long)
        abstract fun getOp(op: Long): Long
    }

    inner class AnyDV(val targetRegister: KMutableProperty1<Interpreter, Long>) : Operation() {
        override fun exec(op: Long) {
            targetRegister.set(this@Interpreter, A / (2L exp combo(op).toInt()))
        }

        override fun toString() = "${targetRegister.name}-DV"
        override fun getOp(op: Long) = combo(op)
    }

    inner class BXL : Operation() {
        override fun exec(op: Long) {
            B = B.xor(op)
        }

        override fun toString() = "BXL"
        override fun getOp(op: Long) = op
    }

    inner class BXC : Operation() {
        override fun exec(op: Long) {
            B = B.xor(C)
        }

        override fun toString() = "BXC"
        override fun getOp(op: Long) = -1L
    }

    inner class BST : Operation() {
        override fun exec(op: Long) {
            B = combo(op) % 8L
        }

        override fun toString() = "BST"
        override fun getOp(op: Long) = combo(op)
    }

    inner class JNZ : Operation() {
        override fun exec(op: Long) {
            if (A != 0L) {
                ip = op
                ipOffset = 0
            }
        }

        override fun toString() = "JNZ"
        override fun getOp(op: Long) = op
    }

    inner class OUT : Operation() {
        override fun exec(op: Long) {
            stdout.add(combo(op) % 8L)
        }

        override fun toString() = "OUT"
        override fun getOp(op: Long) = combo(op)
    }

    private fun combo(operand: Long): Long {
        return when (operand) {
            in 0L..3L -> operand
            4L -> A
            5L -> B
            6L -> C
            else -> throw IllegalArgumentException()
        }
    }

    fun reset(A: Long, B: Long, C: Long) {
        this.A = A
        this.B = B
        this.C = C
        ip = 0
        stdout.clear()
    }

    private fun exec(code: Long, op: Long) {
        val operation: Operation = when (code) {
            0L -> AnyDV(Interpreter::A)
            1L -> BXL()
            2L -> BST()
            3L -> JNZ()
            4L -> BXC()
            5L -> OUT()
            6L -> AnyDV(Interpreter::B)
            7L -> AnyDV(Interpreter::C)
            else -> throw IllegalArgumentException()
        }
        if (debug) println("Executing $operation ${operation.getOp(op)}")
        operation.exec(op)
        ip += operation.ipOffset
        if (debug) println(this)
    }

    fun run(program: List<Long>) {
        while (ip < program.size) {
            exec(program[ip.toInt()], program[(ip + 1).toInt()])
        }
    }

    override fun toString(): String {
        return "A: $A\nB: $B\nC: $C\n\nip: $ip\nout:${stdout.joinToString(",")}\n"
    }

    companion object {
        fun fromLines(lines: List<String>): Interpreter {
            val registerValues = lines.map { """\d+""".toRegex().find(it)!!.value.toLong() }.toList()
            return Interpreter(registerValues[0], registerValues[1], registerValues[2])
        }
    }
}

fun readProgram(line: String): List<Long> {
    return """\d+""".toRegex().findAll(line).map { it.value.toLong() }.toList()
}

fun printInfo(i: Long, output: List<Long>) {
    println(
        "${i.toString().padStart(5)} -> ${
            Integer.toBinaryString(i.toInt()).padStart(10)
        } | ${output.joinToString(",")}"
    )
}

fun task01(interpreter: Interpreter, program: List<Long>): String {
    interpreter.run(program)
    return interpreter.stdout.joinToString(",")
}

fun task02(interpreter: Interpreter, program: List<Long>): Long {
    fun fromFactor(factors: List<Long>): Long {
        var x = 0L
        for (factor in factors) {
            x = x * 8 + factor
        }
        return x
    }

    fun dfs(factors: List<Long>): List<Long>? {
        val start = fromFactor(factors)
        for (i in 0L..7) {
            if (factors.isEmpty() && i == 0L) continue // Prevent infinite recursion if 000 also maps to 0
            interpreter.reset(start * 8L + i, 0, 0)
            interpreter.run(program)
            if (interpreter.stdout == program) return factors + listOf(i)
            if (interpreter.stdout == program.drop(program.size - interpreter.stdout.size)) {
                dfs(factors + listOf(i))?.let { return it }
            }
        }
        return null
    }

    return fromFactor(dfs(emptyList<Long>())!!)
}

class Day17 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()
        val registerLines = lines.takeWhile { it.isNotEmpty() }

        val interpreter = Interpreter.fromLines(registerLines)
        val program = readProgram(lines.drop(registerLines.size + 1).first())

        println("Day 17 Task 1: ${task01(interpreter, program)}")
        println("Day 17 Task 2: ${task02(interpreter, program)}")
    }
}

fun main(args: Array<String>) = Day17().main(args)