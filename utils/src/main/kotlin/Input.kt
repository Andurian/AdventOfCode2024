package meow.andurian.aoc2024.utils

fun readResourceAsLines(fileName: String): List<String> {
    return object {}.javaClass.getResourceAsStream(fileName)?.bufferedReader()?.readLines()!!
}
