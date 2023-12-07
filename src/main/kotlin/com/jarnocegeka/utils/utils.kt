package com.jarnocegeka.utils

import java.io.File

const val FILE_PREFIX = "src/main/resources/"

fun readInputFile(fileName: String): File {
    return File("$FILE_PREFIX$fileName")
}

fun readInputFileLines(fileName: String): List<String> {
    return readInputFile(fileName).readLines()
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