package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToIntWithSum
import com.jarnocegeka.utils.reduceToLongWithSum
import com.jarnocegeka.utils.transpose
import kotlin.Int.Companion.MAX_VALUE

fun adventOfCodeYear2023Day14Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day14.txt")
    val platform = mapToPlatform(lines)

//    println(platform.calculateLoad())
    println(platform.calculateLoadByRolling())
}

fun adventOfCodeYear2023Day14Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day14.txt")
}

private fun mapToPlatform(lines: List<String>): Platform {
    return Platform(transpose(lines))
}

private class Platform(val rockFormations: List<String>) {
    fun calculateLoad(): Long {
        return rockFormations.map { calculateLoadFor(it) }.reduceToLongWithSum()
    }

    fun calculateLoadByRolling(): Int {
        return transpose(rollRockFormation()).reversed()
            .mapIndexed { index, value -> value.count { it == 'O' } * (index + 1) }
            .reduceToIntWithSum()
    }

    private fun calculateLoadFor(rockFormation: String): Long {
        if (hasCubeRocks(rockFormation)) {
            val lastRoundRockIndex = rockFormation.lastIndexOf("O")
            val firstCubeRockIndex = rockFormation.indexOf("#")

            if (lastRoundRockIndex < firstCubeRockIndex) {
                return calculateLoadForWithoutCubeRocks(rockFormation)
            }

            val roundRockIndexes = rockFormation.mapIndexedNotNull { index, rock -> if (rock == 'O') index else null }.sorted().toMutableList()
            val cubeRockIndexes = rockFormation.mapIndexedNotNull { index, rock -> if (rock == '#') index else null }.sorted().toMutableList()

            var load = 0L
            var startingRockLoad = 10L
            do {
                var cubeRockIndex = rockFormation.length
                if (cubeRockIndexes.isNotEmpty()) {
                    cubeRockIndex = cubeRockIndexes.first()
                    cubeRockIndexes.removeFirst()
                }

                val roundRocksBeforeCubeRock = roundRockIndexes.count { it < cubeRockIndex }.toLong()

                if (roundRocksBeforeCubeRock > 0) {
                    roundRockIndexes.removeIf { it < cubeRockIndex }
                    load += calculateLoadFor(roundRocksBeforeCubeRock, startingRockLoad)
                }

                startingRockLoad = rockFormation.length.toLong() - (cubeRockIndex + 1)
            } while (cubeRockIndexes.isNotEmpty() || roundRockIndexes.isNotEmpty())

            return load
        }

        return calculateLoadForWithoutCubeRocks(rockFormation)
    }

    private fun calculateLoadForWithoutCubeRocks(rockFormation: String): Long {
        val amountOfRocks = rockFormation.count { it == 'O' }
        if (amountOfRocks == 0) return 0

        return calculateLoadFor(amountOfRocks.toLong(), rockFormation.length.toLong())
    }

    private fun calculateLoadFor(amountOfRocks: Long, startingRockLoad: Long): Long {
        var rockLoad = startingRockLoad
        (1 until amountOfRocks).forEach {
            rockLoad += startingRockLoad - it
        }

        return rockLoad
    }

    private fun hasCubeRocks(rockFormation: String) = rockFormation.contains('#')

    fun printRolledRockFormation() {
        transpose(rollRockFormation()).forEach { println(it) }
    }

    private fun rollRockFormation() = rockFormations.map { rollRockFormation(it) }
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
}