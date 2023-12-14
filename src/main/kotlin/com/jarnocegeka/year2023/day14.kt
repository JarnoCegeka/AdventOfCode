package com.jarnocegeka.year2023

import com.jarnocegeka.utils.areEqual
import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToIntWithSum
import com.jarnocegeka.utils.transpose
import kotlin.Int.Companion.MAX_VALUE

fun adventOfCodeYear2023Day14Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day14.txt")
    val platform = mapToPlatform(lines)

    println(platform.calculateLoadByRolling())
}

fun adventOfCodeYear2023Day14Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day14.txt")
    val platform = mapToPlatform(lines)

    println(platform.calculateLoadByCycling(1000000000))
}

private fun mapToPlatform(lines: List<String>): Platform {
    return Platform(lines)
}

private class Platform(val rockFormations: List<String>) {
    fun calculateLoadByRolling(): Int {
        return transpose(rollRockFormation()).reversed()
            .mapIndexed { index, value -> value.count { it == 'O' } * (index + 1) }
            .reduceToIntWithSum()
    }

    fun calculateLoadByCycling(cycleTimes: Int = 1, withPrinting: Boolean = false): Int {
        val cycleFormations: MutableMap<Int, List<String>> = mutableMapOf()
        var rockFormation: List<String> = rockFormations
        cycleFormations[0] = rockFormation
        repeat(cycleTimes) {
            val cycledRockFormation = cycleRockFormation(rockFormation)
            if (cycleFormations.any { formation -> areEqual(cycledRockFormation, formation.value) }) {
                val formation = cycleFormations.entries.first { formation -> areEqual(cycledRockFormation, formation.value) }
                val difference = it - formation.key

                val i = (cycleTimes - it) % difference
                val shouldBeFormationIndex = (i + formation.key) - 1
                val shouldBeFormation = cycleFormations[shouldBeFormationIndex]!!

                return calculateLoad(shouldBeFormation)
            } else {
                cycleFormations[it] = cycledRockFormation
            }

            rockFormation = cycledRockFormation
        }

        if (rockFormation.isEmpty()) return 0
        if (withPrinting) printRockFormation(rockFormation)

        return calculateLoad(rockFormation)
    }

    fun calculateLoad(rockFormation: List<String>): Int {
        return rockFormation.reversed()
                .mapIndexed { index, value -> value.count { it == 'O' } * (index + 1) }
                .reduceToIntWithSum()
    }

    private fun cycleRockFormation(rockFormation: List<String>): List<String> {
        val north = rollRockFormation(transpose(rockFormation))
        val west = rollRockFormation(transpose(north))
        val south = rollRockFormation(transpose(west).map { it.reversed() })
        val east = rollRockFormation(transpose(south.map { it.reversed() }).map { it.reversed() })

        return east.map { it.reversed() }
    }

    private fun rollRockFormation(rockFormationToRoll: List<String> = transpose(rockFormations)) = rockFormationToRoll.map { rollRockFormation(it) }
    private fun rollRockFormation(rockFormation: String): String {
        var rolledRockFormation = rockFormation

        rockFormation.indices.forEach { currentIndex ->
            val current = rolledRockFormation[currentIndex]
            val roundRockIndexes = rolledRockFormation.mapIndexedNotNull { index, rock -> if (rock == 'O') index else null }.sorted().toMutableList()
            val cubeRockIndexes = rolledRockFormation.mapIndexedNotNull { index, rock -> if (rock == '#') index else null }.sorted().toMutableList()

            if (current == '.') {
                val roundRockMinIndex = roundRockIndexes.filter { it > currentIndex }.minByOrNull { it } ?: MAX_VALUE
                val cubeRockMinIndex = cubeRockIndexes.filter { it > currentIndex }.minByOrNull { it } ?: MAX_VALUE

                if (roundRockMinIndex != MAX_VALUE || cubeRockMinIndex != MAX_VALUE) {
                    val nextSymbolIndex = minOf(roundRockMinIndex, cubeRockMinIndex)
                    val nextSymbol = rolledRockFormation[nextSymbolIndex]
                    if (nextSymbol == 'O') {
                        rolledRockFormation = rolledRockFormation.replaceRange(currentIndex, currentIndex + 1, "O")
                        rolledRockFormation = rolledRockFormation.replaceRange(nextSymbolIndex, nextSymbolIndex + 1, ".")
                    }
                }
            }
        }

        return rolledRockFormation
    }

    private fun printRockFormation(rockFormation: List<String>) {
        rockFormation.forEach { println(it) }
        println()
    }
}