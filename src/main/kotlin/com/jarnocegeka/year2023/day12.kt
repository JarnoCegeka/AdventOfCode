package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToLongWithSum
import com.jarnocegeka.utils.whiteSpaceRegex

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
    println(conditionRecords.map { it.calculateArrangements() }.reduceToLongWithSum())
}

private fun mapToConditionRecords(lines: List<String>): List<ConditionRecord> {
    return lines.map { mapToConditionRecord(it) }
}

private fun mapToConditionRecord(line: String): ConditionRecord {
    val conditionRecordInfo = line.split(whiteSpaceRegex)

    return ConditionRecord(conditionRecordInfo[0], conditionRecordInfo[1].split(",").map { it.trim().toInt() })
}

private class ConditionRecord(var springs: String, var contiguousGroup: List<Int>) {

    fun unfoldConditionRecord() {
        springs = (0 until 5).joinToString("?") { springs }
        contiguousGroup = (0 until 5).flatMap { contiguousGroup }
    }

    fun calculateArrangements(): Long {
        val cache = hashMapOf<Pair<String, List<Int>>, Long>()

        return calculateArrangements(springs, contiguousGroup, cache)
    }

    private fun calculateArrangements(springs: String, contiguousGroup: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long>): Long {
        if (contiguousGroup.isEmpty()) return if ("#" in springs) 0 else 1
        if (springs.isEmpty()) return 0

        return cache.getOrPut(springs to contiguousGroup) {
            var result = 0L
            if (springs.first() in ".?")
                result += calculateArrangements(springs.drop(1), contiguousGroup, cache)
            if (springs.first() in "#?" && contiguousGroup.first() <= springs.length && "." !in springs.take(contiguousGroup.first()) && (contiguousGroup.first() == springs.length || springs[contiguousGroup.first()] != '#'))
                result += calculateArrangements(springs.drop(contiguousGroup.first() + 1), contiguousGroup.drop(1), cache)
            result
        }
    }
}
