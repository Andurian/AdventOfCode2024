package meow.andurian.aoc2024.utils

import java.io.BufferedReader

fun resourceReader(filename : String) : BufferedReader{
    return object {}.javaClass.getResourceAsStream(filename)!!.bufferedReader()
}

fun readResourceAsLines(filename: String): List<String> {
    return resourceReader(filename).readLines()
}
