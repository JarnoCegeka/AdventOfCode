package com.jarnocegeka.utils

import java.io.File

const val FILE_PREFIX = "src/main/resources/"
val whiteSpaceRegex = Regex("\\s+")

fun readInputFile(fileName: String): File {
    return File("$FILE_PREFIX$fileName")
}

fun readInputFileLines(fileName: String): List<String> {
    return readInputFile(fileName).readLines()
}

fun List<Int>.reduceToIntWithSum(): Int {
    return this.reduce { accumulator, value -> accumulator + value }
}

fun List<Long>.reduceToLongWithSum(): Long {
    return this.reduce { accumulator, value -> accumulator + value }
}

fun List<Double>.reduceToDoubleWithSum(): Double {
    return this.reduce { accumulator, value -> accumulator + value }
}

fun IntRange.fullyContains(other: IntRange): Boolean {
    return this.contains(other.first) && this.contains(other.last)
}

fun IntRange.overlaps(other: IntRange): Boolean {
    return this.contains(other.first) || this.contains(other.last)
}

fun gcd(input: List<Long>): Long {
    return input.reduce { acc, a -> gcd(acc, a) }
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a

    return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b));
}

fun lcm(input: List<Long>): Long {
    var result = input[0]
    (1 until input.size).forEach { result = lcm(result, input[it]) }
    return result
}

fun calculateAmountOfPairs(n: Int): Int {
    return n * (n - 1) / 2
}

fun binaryToDecimal(number: String): Long {
    var result: Long = 0
    var bit = 0
    var n: Int = number.length - 1
    while (n >= 0) {
        if (number[n] == '1') result += (1 shl (bit))
        n -= 1
        bit += 1
    }

    return result
}

fun splitByWhiteLine(lines: List<String>): List<List<String>> {
    val subSets = mutableListOf<List<String>>()
    var startIndex = 0

    lines.forEachIndexed { index, line ->
        if (line.isEmpty()) {
            subSets.add(lines.subList(startIndex, index))
            startIndex = index + 1
        } else if (index == lines.size - 1) {
            subSets.add(lines.subList(startIndex, lines.size))
        }
    }

    return subSets
}