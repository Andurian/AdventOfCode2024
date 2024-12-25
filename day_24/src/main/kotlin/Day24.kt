package meow.andurian.aoc2024.day_24

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import java.io.BufferedReader
import java.io.File

enum class OP {
    AND {
        override fun f(a: Boolean, b: Boolean) = a && b
    },
    OR {
        override fun f(a: Boolean, b: Boolean) = a || b
    },
    XOR {
        override fun f(a: Boolean, b: Boolean) = a xor b
    };

    abstract fun f(a: Boolean, b: Boolean): Boolean

    companion object {
        fun fromString(s: String): OP {
            return when (s) {
                "AND" -> AND
                "OR" -> OR
                "XOR" -> XOR
                else -> throw IllegalArgumentException()
            }
        }
    }
}

class Wire(val name: String, var value: Boolean? = null) {
    val next = mutableListOf<Gate>()

    fun setValue(v: Boolean) {
        if (value != null) throw RuntimeException("input is already set")
        value = v
        for (n in next) n.setInput(v)
    }

}

class Gate {
    val op: OP
    val id: Int
    private var input1: Boolean? = null
    private var input2: Boolean? = null
    var next: Wire? = null


    constructor(op: OP) {
        this.op = op
        this.id = instanceCounter++
    }

    fun setInput(v: Boolean) {
        if (input1 == null) {
            input1 = v
            return
        }

        if (input2 == null) {
            input2 = v
            next?.setValue(op.f(input1!!, input2!!))

            return
        }

        throw RuntimeException("Node already received two inputs")
    }

    companion object {
        var instanceCounter = 0
    }
}


class AdderBlock(val inputX: Wire, val inputY: Wire) {
    val wires = mutableSetOf<Wire>()
    val gates = mutableSetOf<Gate>()

    var inputCarry: Wire? = null
    var inputCarryDbl: Wire? = null

    var outputZ: Wire? = null
    var outputCarry: Wire? = null

    constructor(inputX: Wire, inputY: Wire, inputCarry: Wire?) : this(inputX, inputY) {
        if(inputCarry == null) return
        this.inputCarry = inputCarry
        inputCarryDbl = Wire(inputCarry.name + "_dbl") // used for graphviz
    }

    init {
        wires.add(inputX)
        wires.add(inputY)

        val toVisit = mutableSetOf<Gate>()
        toVisit.addAll(inputX.next)
        toVisit.addAll(inputY.next)

        if(inputCarry != null){
            wires.add(inputCarry!!)
            toVisit.addAll(inputCarry!!.next)
        }

        while (toVisit.isNotEmpty()) {
            val currentGate = toVisit.first()
            toVisit.remove(currentGate)
            gates.add(currentGate)

            if(currentGate.next == null) continue
            val nextWire = currentGate.next!!
            wires.add(nextWire)

            if(currentGate.op ==  OP.OR){
                outputCarry = nextWire
                continue
            }

            // First Block has output of AND as carry
            if(currentGate.op == OP.AND && inputX.name.drop(1) == "00"){
                wires.add(nextWire)
                outputCarry = nextWire
                continue
            }

            if(nextWire.name.startsWith('z')){
                outputZ = nextWire
                continue
            }

            for(nextGate in nextWire.next){
                toVisit.add(nextGate)
            }
        }
    }

    fun toGraphviz(): String {
        var ret = "subgraph cluster_${inputX.name}_${inputY.name} {\n"
        ret += "style=filled;\n" + "color=lightgrey;\n"

        for (g in gates) {
            ret += "${g.id} [label=\"${g.op.name}\"];\n"
        }

        for (w in wires) {
            if(w == outputCarry) continue
            for (n in w.next) {
                ret += "${w.name} -> ${n.id};\n"
            }
        }

        for (g in gates) {
            ret += "${g.id} -> ${g.next!!.name};\n"
        }

        if(inputCarry != null) {
            ret += "${inputCarry!!.name} -> ${inputCarryDbl!!.name};\n"
            for(g in inputCarry!!.next) {
                ret += "${inputCarryDbl!!.name} -> ${g.id};\n"
            }
        }

        ret += "}\n"
        return ret
    }
}

class Graph(val wires: Map<String, Wire>, val gates: Set<Gate>) {
    val outputNodes: List<Wire>
    val inputNodesX: List<Wire>
    val inputNodesY: List<Wire>

    val adderBlocks = mutableMapOf<Int, AdderBlock>()

    init {
        outputNodes = wires.keys.filter { it.startsWith("z") }.sorted().reversed().map { wires[it]!! }
        inputNodesX = wires.keys.filter { it.startsWith("x") }.sorted().map { wires[it]!! }
        inputNodesY = wires.keys.filter { it.startsWith("y") }.sorted().map { wires[it]!! }


        for (i in inputNodesX.indices) {
            if(i > 0){
                adderBlocks[i] = AdderBlock(inputNodesX[i], inputNodesY[i], adderBlocks[i-1]!!.outputCarry)
            }else {
                adderBlocks[i] = AdderBlock(inputNodesX[i], inputNodesY[i])
            }
        }
    }

    fun compute(inputLines: List<String>): Long {
        for (l in inputLines) {
            val tokens = l.split(": ")
            wires[tokens[0]]!!.setValue(tokens[1] == "1")
        }
        var out = 0L
        for (n in outputNodes) {
            out *= 2
            if (n.value!! == true) {
                out += 1
            }
        }
        return out
    }

    fun toGraphvizNoBlocks(): String {
        var ret = "digraph {\n"
        for (g in gates) {
            ret += "${g.id} [label=\"${g.op.name}\"];\n"
        }
        for (w in wires.values) {
            for (n in w.next) {
                ret += "${w.name} -> ${n.id};\n"
            }
        }
        for (g in gates) {
            ret += "${g.id} -> ${g.next!!.name};\n"
        }
        ret += "}\n"
        return ret
    }

    fun toGraphvizWithBlocks(): String {
        var ret = "digraph {\n"
        for(b in adderBlocks.values) {
            ret += b.toGraphviz() + "\n"
        }
        ret += "}\n"
        return ret
    }

    companion object {
        fun fromInput(inputLines: List<String>, structureLines: List<String>): Graph {
            var wires = mutableMapOf<String, Wire>()
            val gates = mutableSetOf<Gate>()

            val inputWires = inputLines.map { Wire(it.split(':').first()) }
            for (w in inputWires) {
                wires[w.name] = w
            }

            for (line in structureLines) {
                val tokens = line.split(' ')

                val wire1 = wires.getOrPut(tokens[0]) { Wire(tokens[0]) }
                val wire2 = wires.getOrPut(tokens[2]) { Wire(tokens[2]) }
                val wireOut = wires.getOrPut(tokens[4]) { Wire(tokens[4]) }

                val gate = Gate(OP.fromString(tokens[1]))

                wire1.next.add(gate)
                wire2.next.add(gate)
                gate.next = wireOut

                gates.add(gate)
            }

            return Graph(wires, gates)
        }
    }
}

fun task01(lines: List<String>): Long {
    val inputLines = lines.takeWhile { it.isNotEmpty() }
    val structureLines = lines.drop(inputLines.size + 1)

    val graph = Graph.fromInput(inputLines, structureLines)
    return graph.compute(inputLines)
}

class Day24 : AoCDay() {
    var counter = 0
    override fun testInput() = "/test_input_large.txt"
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()

        val inputLines = lines.takeWhile { it.isNotEmpty() }
        val structureLines = lines.drop(inputLines.size + 1)

        val graph = Graph.fromInput(inputLines, structureLines)

        println("Day 24 Task 1: ${task01(lines)}")

        // Task 2 is solved manually based on more or less clear graphviz output
        // One error class breaks visualization with adder blocks so both outputs are rendered
        // Also small testcases don't work with block visualization
        File("graph_${counter}.txt").printWriter().use { it.println(graph.toGraphvizWithBlocks()) }
        File("graph_noblocks_${counter++}.txt").printWriter().use { it.println(graph.toGraphvizNoBlocks()) }
    }
}

fun main(args: Array<String>) = Day24().main(args)