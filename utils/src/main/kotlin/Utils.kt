package meow.andurian.aoc2024.utils

inline fun <E> Iterable<E>.indexesOf(predicate: (E) -> Boolean) =
    mapIndexedNotNull { index, elem -> index.takeIf { predicate(elem) } }