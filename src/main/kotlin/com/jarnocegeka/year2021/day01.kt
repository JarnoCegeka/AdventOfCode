package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day01Part1() {
    val lines = readInputFileLines("InputYear2021Day01.txt").map { it.toInt() }

    println(retrieveSumCounter(lines))
}

fun adventOfCodeYear2021Day01Part2() {
    val lines = readInputFileLines("InputYear2021Day01.txt").map { it.toInt() }

    val newList = mutableListOf<Int>()
    (0 until (lines.size - 2)).forEach {
        val a = lines[it]
        val b = lines[it + 1]
        val c = lines[it + 2]

        val result = a + b + c
        newList.add(result)
    }

    println(retrieveSumCounter(newList))
}

private fun retrieveSumCounter(lines: List<Int>): Int {
    var counter = 0
    var current = lines[0]
    (1 until lines.size).forEach {
        val next = lines[it]

        if (next > current) counter++

        current = next
    }
    return counter
}