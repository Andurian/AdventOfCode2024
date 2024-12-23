package meow.andurian.aoc2024.day_23

import com.github.ajalt.clikt.core.main
import meow.andurian.aoc2024.utils.AoCDay
import java.io.BufferedReader

class Graph(val structure: Map<String, List<String>>) {
    fun nodes() = structure.keys
    fun neighbors(s: String) = structure[s]!!

    companion object {
        fun fromInput(lines: List<String>): Graph {
            val pairs = lines.map {
                val tokens = it.split('-')
                Pair(tokens[0], tokens[1])
            }

            val nodes = mutableSetOf<String>()
            for (p in pairs) {
                nodes.add(p.first)
                nodes.add(p.second)
            }

            val structure = mutableMapOf<String, MutableList<String>>()
            for (n in nodes) {
                structure[n] = mutableListOf<String>()
            }

            for (p in pairs) {
                structure[p.first]!!.add(p.second)
                structure[p.second]!!.add(p.first)
            }

            return Graph(structure)
        }
    }
}

fun findCyclesFrom(graph: Graph, start: String, cycleLength: Int): List<List<String>> {
    fun dfs(nodesVisited: List<String>): List<List<String>> {
        val ret = mutableListOf<List<String>>()
        for (nextNode in graph.neighbors(nodesVisited.last())) {
            if (nodesVisited.size == cycleLength) {
                if (nextNode == nodesVisited.first()) ret.add(nodesVisited.sorted())
            } else {
                if (!nodesVisited.contains(nextNode)) ret.addAll(dfs(nodesVisited + nextNode))
            }
        }
        return ret
    }
    return dfs(listOf(start))
}

fun findConnectedComponentFrom(graph: Graph, start: String): List<String> {
    fun dfs(nodesVisited: List<String>): List<String> {
        var ret = nodesVisited
        for (candidate in graph.neighbors(nodesVisited.last())) {
            if(ret.contains(candidate)) continue // Some pruning for better performance
            if (nodesVisited.all { graph.neighbors(it).contains(candidate) }) {
                dfs(nodesVisited + candidate).let { if (it.size > ret.size) ret = it }
            }
        }
        return ret
    }
    return dfs(listOf(start))
}

fun task01(graph: Graph): Int {
    val threeCycles = mutableSetOf<List<String>>()
    for (n in graph.nodes()) {
        threeCycles.addAll(findCyclesFrom(graph, n, 3))
    }
    return threeCycles.filter { it.any { node -> node.first() == 't' } }.size

}

fun task02(graph : Graph) : String {
    var connectedComponent = emptyList<String>()
    for(n in graph.nodes()){
        findConnectedComponentFrom(graph, n).let{if(it.size > connectedComponent.size){connectedComponent = it}}
    }
    return connectedComponent.sorted().joinToString(",")
}

class Day23 : AoCDay() {
    override fun solve(reader: BufferedReader) {
        val lines = reader.readLines()
        val graph = Graph.fromInput(lines)

        println("Day 23 Task 1: ${task01(graph)}")
        println("Day 23 Task 2: ${task02(graph)}")
    }
}

fun main(args: Array<String>) = Day23().main(args)