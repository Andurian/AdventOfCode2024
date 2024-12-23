package meow.andurian.aoc2024.utils

import kotlin.math.pow

inline fun <E> Iterable<E>.indexesOf(predicate: (E) -> Boolean) =
    mapIndexedNotNull { index, elem -> index.takeIf { predicate(elem) } }

fun <T> stepN(initial: T, f: (T) -> T, n: Int): T {
    var current = initial
    repeat(n) { current = f(current) }
    return current
}

fun <T> counts(values: List<T>): Map<T, Long> {
    val ret = mutableMapOf<T, Long>()
    for (s in values) {
        ret.merge(s, 1, Long::plus)
    }
    return ret
}

fun <T> toOverlappingPairs(input: List<T>): List<Pair<T, T>> {
    val ret = mutableListOf<Pair<T,T>>()
    for (i in 1 until input.size) {
        ret.add(Pair(input[i-1], input[i]))
    }
    return ret
}

infix fun Long.exp(exponent: Int): Long = toDouble().pow(exponent).toLong()
infix fun Int.exp(exponent: Int): Int = toDouble().pow(exponent).toInt()