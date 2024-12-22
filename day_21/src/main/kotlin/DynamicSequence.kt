package meow.andurian.aoc2024.day_21

class DynamicSequence {
    val parts: Map<String, Long>

    constructor(input: String) {
        val ret = mutableMapOf<String, Long>()
        val tokens = input.split('A')
        for (token in tokens.dropLast(1)) {
            ret.merge("${token}A", 1, Long::plus)
        }
        parts = ret
    }

    constructor(parts: Map<String, Long>) {
        this.parts = parts
    }

    fun next(): DynamicSequence {
        val ret = mutableMapOf<String, Long>()
        for ((part, count) in parts) {
            val expanded = nextSequence(part)
            val tokens = expanded.split('A')
            for (token in tokens.dropLast(1)) {
                ret.merge("${token}A", count, Long::plus)
            }
        }
        return DynamicSequence(ret)
    }

    fun totalSize(): Long {
        var ret = 0L
        for ((k, v) in parts) {
            ret += k.length * v
        }
        return ret
    }

    override fun toString(): String {
        var s = "${totalSize()}"
        for ((part, count) in parts) {
            s += "[$part -> $count] "
        }
        return s
    }
}