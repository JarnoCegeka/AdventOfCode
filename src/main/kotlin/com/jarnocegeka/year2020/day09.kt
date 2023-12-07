package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2020Day09Part1() {
    val input = readInputFileLines("year2020/InputYear2020Day09.txt").map { it.toLong() }
    val numberNotFound = findIdiotNumber(input)

    println(numberNotFound)
}

fun adventOfCodeYear2020Day09Part2() {
    val input = readInputFileLines("year2020/InputYear2020Day09.txt").map { it.toLong() }
    val numberNotFound = findIdiotNumber(input)

    val contiguousSet = findContiguousSet(numberNotFound, input)
    val min = contiguousSet.minOrNull()
    val max = contiguousSet.maxOrNull()
    val result = if (min == null) max else if (max == null) min else max + min
    println("Result: $min + $max = $result")
}

fun findContiguousSet(numberNotFound: Long, input: List<Long>): List<Long> {
    var currentIndex = 0
    val contiguousSet = mutableListOf<Long>()
    while (currentIndex <= input.lastIndex) {
        val currentNumber = input[currentIndex]
        contiguousSet.add(currentNumber)

        var sum = contiguousSet.sum()
        if (sum > numberNotFound) {
            while (sum > numberNotFound) {
                contiguousSet.removeAt(0)
                sum = contiguousSet.sum()
            }
        }

        if (sum == numberNotFound) break

        currentIndex++
    }

    return contiguousSet
}

private fun findIdiotNumber(input: List<Long>): Long {
    val preambleCount = 25
    var preamble: List<Long>
    var numberNotFound = 0L

    var currentIndex = 0
    for (it in input.subList(preambleCount, input.size)) {
        preamble = input.subList(currentIndex, (preambleCount + currentIndex))

        val isSumFound = findSum(preamble, it)
        if (!isSumFound) {
            numberNotFound = it
            break
        }
        currentIndex++
    }

    return numberNotFound
}

private fun findSum(preamble: List<Long>, numberToFind: Long) : Boolean {
    for (i in preamble) {
        for (j in preamble.subList(1, preamble.size)) {
            if ((i + j) == numberToFind) return true
        }
    }
    return false
}