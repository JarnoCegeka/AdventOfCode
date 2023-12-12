package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToLongWithSum
import com.jarnocegeka.utils.whiteSpaceRegex
import java.lang.IllegalStateException

fun adventOfCodeYear2023Day12Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day12.txt")
    val conditionRecords = mapToConditionRecords(lines)
    val arrangementCounts = conditionRecords.map { it.calculateArrangements() }
    println(arrangementCounts.reduceToLongWithSum())
}

fun adventOfCodeYear2023Day12Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day12.txt")
    val conditionRecords = mapToConditionRecords(lines)
    conditionRecords.forEach { it.unfoldConditionRecord() }
    val arrangementCounts = conditionRecords.map { it.calculateArrangements() }
    println(arrangementCounts.reduceToLongWithSum())
}

private fun mapToConditionRecords(lines: List<String>): List<ConditionRecord> {
    return lines.map { mapToConditionRecord(it) }
}

private fun mapToConditionRecord(line: String): ConditionRecord {
    val conditionRecordInfo = line.split(whiteSpaceRegex)

    return ConditionRecord(conditionRecordInfo[0], conditionRecordInfo[1].split(",").map { it.trim().toInt() })
}

private class BlockMapKey(vararg keys: Int)
private class ConditionRecord(var springs: String, var contiguousGroup: List<Int>) {
    fun unfoldConditionRecord() {
        springs = (0 until 5).joinToString("?") { springs }
        contiguousGroup = (0 until 5).flatMap { contiguousGroup }
    }

    fun calculateArrangements(i: Int = 0, j: Int = 0, cur: Int = 0): Long {
        return calculateArrangements(mutableMapOf(), springs, contiguousGroup, i, j, cur)
    }

    private fun calculateArrangements(blockMap: MutableMap<BlockMapKey, Long>, springs: String, contiguousGroup: List<Int>, i: Int, j: Int, cur: Int): Long {
        val key = BlockMapKey(i, j, cur)
        if (blockMap.contains(key)) return blockMap[key] ?: throw IllegalStateException("Couldn't find value for block key!")

        if (i == springs.length) return if ((j == contiguousGroup.size && cur == 0) || (j == contiguousGroup.size - 1 && contiguousGroup[j] == cur)) 1 else 0

        var total = 0L
        val c = springs[i]
        if ((c == '.' || c == '?') && cur == 0) {
            total += calculateArrangements(blockMap, springs, contiguousGroup, i + 1, j, 0)
        } else if ((c == '.' || c == '?') && cur > 0 && j < contiguousGroup.size && contiguousGroup[j] == cur) {
            total += calculateArrangements(blockMap, springs, contiguousGroup, i + 1, j + 1, 0)
        }
        if (c == '#' || c == '?') {
            total += calculateArrangements(blockMap, springs, contiguousGroup, i + 1, j, cur + 1)
        }

        blockMap[key] = total
        return total
    }
}