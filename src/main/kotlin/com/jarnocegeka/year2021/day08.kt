package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day08Part1() {
    val numberOfSegmentsOfEasySevenSegmentNumbers = listOf(2, 3, 4, 7)
    val combinations = readInputFileLines("year2021/InputYear2021Day08.txt")
        .map { line -> line.split(" | ").map { it.split(' ').toTypedArray() }.toTypedArray() }

    var count = 0
    combinations.forEach { combination ->
        count += combination[1].count { numberOfSegmentsOfEasySevenSegmentNumbers.contains(it.length) }
    }

    println("Result: $count")
}

fun adventOfCodeYear2021Day08Part2() {
    val combinations = readInputFileLines("year2021/InputYear2021Day08.txt")
        .map { line -> line.split(" | ").map { it.split(' ').map { code -> sortString(code) }.toTypedArray() }.toTypedArray() }

    val result = combinations.map { combination ->
        val allNumberCombinations = combination[0]
            .map { SevenSegmentNumber(it, deduceInitialNumber(it)) }
            .toMutableList()

        val knownSevenSegmentNumber = allNumberCombinations.filterNot { it.number == -1 }.associateBy { it.number }.toMutableMap()
        val missingNumberCombinations = allNumberCombinations.filter { it.number == -1 }.toMutableList()

        knownSevenSegmentNumber[6] = findSix(missingNumberCombinations, knownSevenSegmentNumber[7]!!)
        missingNumberCombinations.removeIf { it.code == knownSevenSegmentNumber[6]!!.code }

        knownSevenSegmentNumber[9] = findNine(missingNumberCombinations, knownSevenSegmentNumber[4]!!)
        missingNumberCombinations.removeIf { it.code == knownSevenSegmentNumber[9]!!.code }

        knownSevenSegmentNumber[0] = findZero(missingNumberCombinations)
        missingNumberCombinations.removeIf { it.code == knownSevenSegmentNumber[0]!!.code }

        knownSevenSegmentNumber[2] = findTwo(missingNumberCombinations, knownSevenSegmentNumber[9]!!)
        missingNumberCombinations.removeIf { it.code == knownSevenSegmentNumber[2]!!.code }

        knownSevenSegmentNumber[3] = findThree(missingNumberCombinations, knownSevenSegmentNumber[7]!!)
        missingNumberCombinations.removeIf { it.code == knownSevenSegmentNumber[3]!!.code }

        val sevenSegmentNumberFive = missingNumberCombinations[0]
        sevenSegmentNumberFive.number = 5
        knownSevenSegmentNumber[5] = sevenSegmentNumberFive
        missingNumberCombinations.clear()
        val sevenSegmentNumbersPerCode = knownSevenSegmentNumber.map { Pair(it.value.code, it.value) }.toMap()

        combination[1].map { sevenSegmentNumbersPerCode[it]!!.number }.joinToString("") { it.toString() }.toInt()
    }.sum()

    println("Result: $result")
}

private fun sortString(input: String): String {
    val chars = input.toCharArray()
    chars.sort()

    return chars.concatToString()
}

private fun findSix(missingNumberCombinations: List<SevenSegmentNumber>, sevenSegmentNumberSeven: SevenSegmentNumber): SevenSegmentNumber {
    val sevenSegmentNumberSix = missingNumberCombinations
        .filter { it.numberOfSegmentsUsed() == 6 }
        .filterNot { it.usedSegments().containsAll(sevenSegmentNumberSeven.usedSegments()) }[0]
    sevenSegmentNumberSix.number = 6

    return sevenSegmentNumberSix
}

private fun findNine(missingNumberCombinations: List<SevenSegmentNumber>, sevenSegmentNumberFour: SevenSegmentNumber): SevenSegmentNumber {
    val sevenSegmentNumberNine = missingNumberCombinations
        .filter { it.numberOfSegmentsUsed() == 6 }
        .filter { it.usedSegments().containsAll(sevenSegmentNumberFour.usedSegments()) }[0]
    sevenSegmentNumberNine.number = 9

    return sevenSegmentNumberNine
}

private fun findZero(missingNumberCombinations: List<SevenSegmentNumber>): SevenSegmentNumber {
    val sevenSegmentNumberZero = missingNumberCombinations
        .filter { it.numberOfSegmentsUsed() == 6 }[0]
    sevenSegmentNumberZero.number = 0

    return sevenSegmentNumberZero
}

private fun findTwo(missingNumberCombinations: List<SevenSegmentNumber>, sevenSegmentNumberNine: SevenSegmentNumber): SevenSegmentNumber {
    val sevenSegmentNumberTwo = missingNumberCombinations
        .filterNot { sevenSegmentNumberNine.usedSegments().containsAll(it.usedSegments()) }[0]
    sevenSegmentNumberTwo.number = 2

    return sevenSegmentNumberTwo
}

private fun findThree(missingNumberCombinations: List<SevenSegmentNumber>, sevenSegmentNumberSeven: SevenSegmentNumber): SevenSegmentNumber {
    val sevenSegmentNumberThree = missingNumberCombinations
        .filter { it.usedSegments().containsAll(sevenSegmentNumberSeven.usedSegments()) }[0]
    sevenSegmentNumberThree.number = 3

    return sevenSegmentNumberThree
}

private fun deduceInitialNumber(pattern: String): Int {
    return when (pattern.length) {
        2 -> 1
        3 -> 7
        4 -> 4
        7 -> 8
        else -> -1
    }
}

data class SevenSegmentNumber(val code: String, var number: Int = -1) {
    fun usedSegments(): List<Char> {
        return code.toList()
    }

    fun numberOfSegmentsUsed(): Int {
        return code.length
    }
}
