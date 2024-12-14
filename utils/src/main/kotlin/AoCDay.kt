package meow.andurian.aoc2024.utils

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.types.path

import java.io.BufferedReader
import java.io.File
import java.nio.file.Path

abstract class AoCDay: CliktCommand() {
    val inputs : List<Path> by argument().path(mustExist = true).multiple()

    override fun run() {
        if(inputs.isEmpty()){
            val filename = testInput()
            echo("No inputs were supplied. Falling back to test input: '$filename'")
            solve(resourceReader(filename))
            return
        }

        var first = true
        for(filename in inputs){
            if(first){
                first = false
            }else{
                echo()
            }
            echo("Solving with '$filename'")
            solve(File(filename.toString()).bufferedReader())

        }
    }

    open fun testInput() : String = "/test_input.txt"

    abstract fun solve(reader : BufferedReader)
}