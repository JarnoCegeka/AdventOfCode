package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2020Day15Part1() {
    val number2020 = calculateNumberForPosition(2020)
    println("The 2020th number is $number2020")
}

fun adventOfCodeYear2020Day15Part2() {
    val number2020 = calculateNumberForPosition(30000000)
    println("The 30000000th number is $number2020")
}

private fun calculateNumberForPosition(num: Int): Int {
    val input = readInputFileLines("InputYear2020Day15.txt").map { it.split(",") }.flatMap { it.toList() }.map { it.toInt() }
    val lastSpokenNumberIndexes = mutableMapOf<Int, MutableList<Int>>()
    val array = IntArray(num+5)

    var turn = 1
    var lastNumberSpoken = 0
    input.forEachIndexed { index, it ->
        lastSpokenNumberIndexes[it] = mutableListOf(turn)
        lastNumberSpoken = it
        array[turn-1] = it
        turn++
    }

    while (turn <= num+1 ) {
        val indexes = lastSpokenNumberIndexes[lastNumberSpoken]!!
        if (indexes.size == 1) {
            lastNumberSpoken = 0
        } else {
            val lastTurn = indexes.last()
            val lastTurnBefore = indexes[indexes.lastIndex - 1]
            lastNumberSpoken = lastTurn - lastTurnBefore
        }

        if (lastSpokenNumberIndexes.containsKey(lastNumberSpoken)) {
            lastSpokenNumberIndexes[lastNumberSpoken]!!.add(turn)
        } else {
            lastSpokenNumberIndexes[lastNumberSpoken] = mutableListOf(turn)
        }

        array[turn-1] = lastNumberSpoken
        turn++
    }

    return array[num-1]
}