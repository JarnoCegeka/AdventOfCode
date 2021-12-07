package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines
import java.util.Comparator.comparing

fun adventOfCodeYear2021Day07Part1() {
    val positions = readInputFileLines("InputYear2021Day07.txt")[0].split(',').map { it.toInt() }
    val minPosition = positions.minOrNull()!!
    val maxPosition = positions.maxOrNull()!!

    val positionCounters = mutableMapOf<Int, Int>()
    (minPosition..maxPosition).forEach { possiblePosition ->
        if (!positionCounters.containsKey(possiblePosition)) {
            var counter = 0
            positions.forEach {
                counter += if (possiblePosition < it) it - possiblePosition else possiblePosition - it
            }
            positionCounters[possiblePosition] = counter
        }
    }

    val bestPosition = positionCounters.minOfWith(comparing { it.value }) { it }
    println("Result: $bestPosition")
}

fun adventOfCodeYear2021Day07Part2() {
    val positions = readInputFileLines("InputYear2021Day07.txt")[0].split(',').map { it.toInt() }
    val minPosition = positions.minOrNull()!!
    val maxPosition = positions.maxOrNull()!!

    val positionCounters = mutableMapOf<Int, Int>()
    (minPosition..maxPosition).forEach { possiblePosition ->
        if (!positionCounters.containsKey(possiblePosition)) {
            var counter = 0
            positions.forEach {
                val difference = if (possiblePosition < it) it - possiblePosition else possiblePosition - it
                val differenceSum = (0..difference).sum()
                counter += differenceSum
            }
            positionCounters[possiblePosition] = counter
        }
    }

    val bestPosition = positionCounters.minOfWith(comparing { it.value }) { it }
    println("Result: $bestPosition")
}
