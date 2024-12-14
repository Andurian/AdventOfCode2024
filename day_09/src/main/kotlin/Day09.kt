package meow.andurian.aoc2024.day_09

import java.io.BufferedReader

import com.github.ajalt.clikt.core.main

import meow.andurian.aoc2024.utils.AoCDay
import meow.andurian.aoc2024.utils.indexesOf

open class Block(open val start: Int, open val len: Int) {
    fun last() = start + len - 1
}

data class MemBlock(override val start: Int, override val len: Int, val id: Int) : Block(start, len) {
    fun checksum(): Long = (start..last()).sum().toLong() * id
}

data class FreeBlock(override val start: Int, override val len: Int, val after: Int) : Block(start, len)
fun parseData(line: String): List<MemBlock> {
    val ret = mutableListOf<MemBlock>()
    var isBlock = true
    var id = 0
    var start = 0
    for (c in line) {
        val len = c.code - '0'.code
        if (isBlock) {
            ret.add(MemBlock(start, len, id++))
        }
        isBlock = !isBlock
        start += len
    }
    return ret
}

fun printData(data: List<MemBlock>) {
    var current = 0
    for (b in data) {
        while (current < b.start) {
            print('.')
            current++
        }
        while (current <= b.last()) {
            print(b.id)
            current++
        }
    }
}


fun firstFreeBlock(blocks: List<Block>, startIndex: Int = 0): FreeBlock? {
    for (i in startIndex..blocks.size - 2) {
        if (blocks[i].last() + 1 < blocks[i + 1].start) {
            return FreeBlock(blocks[i].last() + 1, blocks[i + 1].start - blocks[i].last() - 1, i)
        }
    }
    return null
}

fun reorder(blocks: List<MemBlock>): List<MemBlock> {
    val ret = blocks.toMutableList()
    var freeRange = firstFreeBlock(ret)
    while (freeRange != null) {
        val toMove = ret.last()
        ret.removeLast()
        if (freeRange.len > toMove.len) {
            ret.add(freeRange.after + 1, MemBlock(freeRange.start, toMove.len, toMove.id))
        } else {
            ret.add(freeRange.after + 1, MemBlock(freeRange.start, freeRange.len, toMove.id))
            ret.add(MemBlock(toMove.start, toMove.len - freeRange.len, toMove.id))
        }
        freeRange = firstFreeBlock(ret)
    }
    return ret
}

fun allFreeBlocks(blocks: List<Block>): List<FreeBlock> {
    val ret = mutableListOf<FreeBlock>()
    for (i in 0..blocks.size - 2) {
        if (blocks[i].last() + 1 < blocks[i + 1].start) {
            ret.add(FreeBlock(blocks[i].last() + 1, blocks[i + 1].start - blocks[i].last() - 1, i))
        }
    }
    return ret
}

fun reorderChunked(blocksIn: List<MemBlock>): List<MemBlock> {
    val blocks = blocksIn.toMutableList()
    var moveId = blocks.last().id
    while(moveId > 0){
        val idxToMove = blocks.indexesOf{it.id == moveId}.first()
        val blockToMove = blocks[idxToMove]

        val freeBlocks = allFreeBlocks(blocks)
        for(idxFree in freeBlocks.indices){
            val blockFree = freeBlocks[idxFree]
            if(blockFree.len >= blockToMove.len && blockFree.start < blockToMove.start){
                blocks.removeAt(idxToMove)
                blocks.add(blockFree.after + 1, MemBlock(blockFree.start, blockToMove.len, blockToMove.id))
                break
            }
        }
        moveId--
    }
    return blocks
}

fun task01(blocks: List<MemBlock>): Long {
    return reorder(blocks).sumOf { it.checksum() }
}

fun task02(blocks: List<MemBlock>): Long {
    return reorderChunked(blocks).sumOf { it.checksum() }
}

class Day09 : AoCDay() {
    override fun testInput() = "/test_input_2.txt"
    override fun solve(reader: BufferedReader) {
        val blocks = parseData(reader.readLine())

        println("Day 09 Task 1: ${task01(blocks)}")
        println("Day 09 Task 1: ${task02(blocks)}")
    }
}

fun main(args : Array<String>) = Day09().main(args)
