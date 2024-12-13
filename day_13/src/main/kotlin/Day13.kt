package meow.andurian.aoc2024.day_13

import meow.andurian.aoc2024.utils.readResourceAsLines

data class Machine(val xa : Long, val ya : Long, val xb : Long, val yb : Long, val xp : Long, val yp : Long) {
    fun toCorrectedMachine() : Machine{
        return Machine(xa, ya, xb, yb, xp + 10000000000000, yp + 10000000000000)
    }

    companion object {
        fun fromLines(lines: List<String>) :Machine  {
            val re = """\d+""".toRegex()
            val n1 = re.findAll(lines[0]).map{it.value.toLong()}
            val n2 = re.findAll(lines[1]).map{it.value.toLong()}
            val n3 = re.findAll(lines[2]).map{it.value.toLong()}
            return Machine(n1.first(), n1.last(), n2.first(), n2.last(), n3.first(), n3.last())
        }
    }

    fun waysToWinNaive(pushLimit : Long) : List<Pair<Long, Long>>{
        val ret = mutableListOf<Pair<Long, Long>>()
        for(aPushes in 0..pushLimit){
            for(bPushes in 0..pushLimit){
                val resX = aPushes * xa + bPushes * xb
                val resY = aPushes * ya + bPushes * yb
                if(resX == xp && resY == yp){
                    ret.add(Pair(aPushes, bPushes))
                }
            }
        }
        return ret
    }

    fun winAt() : Pair<Long, Long>?{
        val v1 = yp * xa - xp * ya
        val v2 = yb * xa - xb * ya

        if(v1 % v2 != 0L) return null
        val bPushes = v1 / v2

        val v3 = xp - bPushes * xb
        if(v3 % xa != 0L) return null
        val aPushes = v3 / xa
        return Pair(aPushes, bPushes)
    }
}

fun tokens(pushes : Pair<Long, Long>) = 3 * pushes.first + pushes.second

fun task01(machines : List<Machine>) : Long{
    fun f(m : Machine) = m.waysToWinNaive(100).minOfOrNull { tokens(it) } ?: 0

    return machines.sumOf{ f(it)}
}

fun task01Smart(machines : List<Machine>) : Long{
    fun f(m : Machine) : Long {
        m.winAt()?.let{return tokens(it)}
        return 0
    }

    return machines.sumOf{ f(it)}
}

fun task02(machines : List<Machine>) : Long{
    fun f(m : Machine) : Long {
        m.toCorrectedMachine().winAt()?.let{return tokens(it)}
        return 0
    }

    return machines.sumOf{ f(it)}
}

fun main() {
    val lines = readResourceAsLines("/test_input.txt")
    val machines = lines.chunked(4).map { Machine.fromLines(it) }

    println("Day 13 Task 1: ${task01(machines)}")
    println("Day 13 Task 1: ${task01Smart(machines)} (fast)")
    println("Day 13 Task 2: ${task02(machines)}")
}