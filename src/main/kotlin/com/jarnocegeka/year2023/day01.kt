package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2023Day01Part1() {
    val lines = readInputFileLines("InputYear2023Day01.txt")

    println(decodeSimpleCalibrationDocument(lines))
}

fun adventOfCodeYear2023Day01Part2() {
    val lines = readInputFileLines("InputYear2023Day01.txt")

    println(decodeCalibrationDocument(lines))
}

private fun decodeSimpleCalibrationDocument(lines: List<String>): Int {
    return lines.map { line -> "${getSimpleFirstDigit(line)}${getSimpleLastDigit(line)}".toInt() }
        .reduce { accumulator, digit -> accumulator + digit }
}

private fun getSimpleFirstDigit(line: String) = line.first { character -> character.isDigit() }

private fun getSimpleLastDigit(line: String) = line.last { character -> character.isDigit() }

private fun decodeCalibrationDocument(lines: List<String>): Int {
    return lines.map { line -> "${getFirstDigit(line)}${getLastDigit(line)}".toInt() }
        .reduce { accumulator, digit -> accumulator + digit }
}

private fun getFirstDigit(line: String): Int {
    val indexOfFirstDigit = line.indexOfFirst { character -> character.isDigit() }
    val firstDigit = line.first { character -> character.isDigit() }.digitToInt()
    if (indexOfFirstDigit < 3) {
        return firstDigit
    }

    if (hasWrittenDigit(line)) {
        val writtenDigit = getWrittenDigit(line)

        return if (writtenDigit.index > indexOfFirstDigit) firstDigit else writtenDigit.digitValue
    }

    return firstDigit
}

private fun getLastDigit(line: String): Int {
    val indexOfLastDigit = line.indexOfLast { character -> character.isDigit() }
    val lastDigit = line.last { character -> character.isDigit() }.digitToInt()
    if (indexOfLastDigit > line.length - 3) {
        return lastDigit
    }

    if (hasWrittenDigit(line)) {
        val writtenDigit = getWrittenDigit(line, false)

        return if (writtenDigit.index > indexOfLastDigit) writtenDigit.digitValue else lastDigit
    }

    return lastDigit
}

private fun hasWrittenDigit(line: String): Boolean = validWrittenDigits.any { validWrittenDigit -> line.contains(validWrittenDigit) }
private fun getWrittenDigit(line: String, findFirst: Boolean = true): WrittenDigit {
    val digitGroups = validWrittenDigits
        .filter { validWrittenDigit -> line.contains(validWrittenDigit) }


    val writtenDigit = if (findFirst) digitGroups.minByOrNull { line.indexOf(it) }!! else digitGroups.maxByOrNull { line.lastIndexOf(it) }!!
    val index = if (findFirst) line.indexOf(writtenDigit) else line.lastIndexOf(writtenDigit)

    return WrittenDigit(writtenDigit, index)
}

class WrittenDigit(val value: String, val index: Int, val digitValue: Int = validWrittenDigits.indexOf(value) + 1)

private val validWrittenDigits = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

