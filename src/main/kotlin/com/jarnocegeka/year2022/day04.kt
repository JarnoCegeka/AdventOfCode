package com.jarnocegeka.year2022

import com.jarnocegeka.utils.fullyContains
import com.jarnocegeka.utils.overlaps
import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2022Day04Part1() {
    val lines = readInputFileLines("year2022/InputYear2022Day04.txt")
    val elfPairs = mapToElfPairs(lines)

    println(elfPairs.count { it.anyRangeFullyContainsTheOther() })
}

fun adventOfCodeYear2022Day04Part2() {
    val lines = readInputFileLines("year2022/InputYear2022Day04.txt")
    val elfPairs = mapToElfPairs(lines)

    println(elfPairs.count { it.anyRangeOverlapsTheOther() })
}

private fun mapToElfPairs(lines: List<String>): List<ElfPair> {
    return lines.map { mapToElfPair(it) }
}

private fun mapToElfPair(line: String): ElfPair {
    val sectionRanges = line.split(",").map { mapToSectionRange(it) }

    return ElfPair(sectionRanges[0], sectionRanges[1])
}

private fun mapToSectionRange(sectionRange: String): IntRange {
    val sectionRangeValues = sectionRange.trim().split("-")

    return IntRange(sectionRangeValues[0].trim().toInt(), sectionRangeValues[1].trim().toInt())
}

private class ElfPair(val sectionRangeElf1: IntRange, val sectionRangeElf2: IntRange) {
    fun anyRangeFullyContainsTheOther(): Boolean {
        return sectionRangeElf1.fullyContains(sectionRangeElf2) || sectionRangeElf2.fullyContains(sectionRangeElf1)
    }

    fun anyRangeOverlapsTheOther(): Boolean {
        return sectionRangeElf1.overlaps(sectionRangeElf2) || sectionRangeElf2.overlaps(sectionRangeElf1)
    }
}