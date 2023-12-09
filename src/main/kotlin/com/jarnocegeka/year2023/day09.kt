package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToLongWithSum
import com.jarnocegeka.utils.whiteSpaceRegex

fun adventOfCodeYear2023Day09Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day09.txt")
    println(mapToNumberSeries(lines).map { extrapolate(it) }.reduceToLongWithSum())
}

fun adventOfCodeYear2023Day09Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day09.txt")
    println(mapToNumberSeries(lines, true).map { extrapolate(it) }.reduceToLongWithSum())
}

private fun mapToNumberSeries(lines: List<String>, reversed: Boolean = false): List<List<Long>> {
    return lines.map { mapToNumberSeries(it, reversed) }
}

private fun mapToNumberSeries(line: String, reversed: Boolean): List<Long> {
    val result = line.split(whiteSpaceRegex).map { it.trim().toLong() }

    return if (reversed) result.reversed() else result
}

private fun extrapolate(numberSeries: List<Long>): Long {
    val next = (0 until numberSeries.size - 1).map { numberSeries[it + 1] - numberSeries[it] }
    if (next.toSet().size == 1) return next.first() + numberSeries.last()

    val extrapolate = extrapolate(next)
    return extrapolate + numberSeries.last()
}