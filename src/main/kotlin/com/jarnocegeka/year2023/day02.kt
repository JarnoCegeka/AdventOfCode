package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2023Day02Part1() {
    val lines = readInputFileLines("InputYear2023Day02.txt")
    val cubeGames = mapToCubeGames(lines)

//    cubeGames.forEach { it.print() }
    println("Result: ${sumOfValidCubesGames(cubeGames)}")
}

fun adventOfCodeYear2023Day02Part2() {
    val lines = readInputFileLines("InputYear2023Day02.txt")
    val cubeGames = mapToCubeGames(lines)

    println("Result: ${sumOfAllPowers(cubeGames)}")
}

private fun mapToCubeGames(lines: List<String>): List<CubeGame> = lines.map { mapToCubeGame(it) }
private fun mapToCubeGame(line: String): CubeGame {
    val id = line.substringAfter("Game ").substringBefore(":").toInt()
    val gameSets = line.substringAfter(":").split(";")
    val cubesGameSets = mapToCubeGameSets(gameSets)

    return CubeGame(id, calculateCubeGameTotal(cubesGameSets), calculateCubeGameMinimumTotal(cubesGameSets), cubesGameSets.all { it.isValid }, cubesGameSets)
}
private fun mapToCubeGameSets(gameSets: List<String>): List<CubeGameSet> = gameSets.map { mapToCubeGameSet(it) }
private fun mapToCubeGameSet(gameSet: String): CubeGameSet {
    var redCubes = 0
    var greenCubes = 0
    var blueCubes = 0
    gameSet.split(",").forEach {
        redCubes += getCubesCount(it, "red")
        greenCubes += getCubesCount(it, "green")
        blueCubes += getCubesCount(it, "blue")
    }

    return CubeGameSet(redCubes, greenCubes, blueCubes, validateCubeGameSet(redCubes, greenCubes, blueCubes))
}

private fun getCubesCount(cubes: String, color: String): Int {
    if (cubes.contains(color)) {
        return cubes.trim().substringBefore(color).trim().toInt()
    }

    return 0
}

private fun calculateCubeGameTotal(cubeGameSets: List<CubeGameSet>): CubeGameTotal {
    var totalRedCubes = 0
    var totalGreenCubes = 0
    var totalBlueCubes = 0
    cubeGameSets.forEach {
        totalRedCubes += it.redCubes
        totalGreenCubes += it.greenCubes
        totalBlueCubes += it.blueCubes
    }

    return CubeGameTotal(totalRedCubes, totalGreenCubes, totalBlueCubes)
}

private fun calculateCubeGameMinimumTotal(cubeGameSets: List<CubeGameSet>): CubeGameMinimumTotal {
    var totalRedCubes = 0
    var totalGreenCubes = 0
    var totalBlueCubes = 0
    cubeGameSets.forEach {
        if (it.redCubes > totalRedCubes) totalRedCubes = it.redCubes
        if (it.greenCubes > totalGreenCubes) totalGreenCubes = it.greenCubes
        if (it.blueCubes > totalBlueCubes) totalBlueCubes = it.blueCubes
    }

    return CubeGameMinimumTotal(totalRedCubes, totalGreenCubes, totalBlueCubes)
}

private fun validateCubeGameSet(redCubes: Int, greenCubes: Int, blueCubes: Int): Boolean {
    return redCubes <= 12 && greenCubes <= 13 && blueCubes <= 14
}

private fun sumOfValidCubesGames(cubeGames: List<CubeGame>): Int = cubeGames.filter { it.isValid }.map { it.id }.reduce { accumulator, id -> accumulator + id }
private fun sumOfAllPowers(cubeGames: List<CubeGame>): Int = cubeGames.map { it.power() }.reduce { accumulator, power -> accumulator + power }

private class CubeGameSet(val redCubes: Int = 0, val greenCubes: Int = 0, val blueCubes: Int = 0, var isValid: Boolean = false)
private class CubeGameTotal(val redCubes: Int = 0, val greenCubes: Int = 0, val blueCubes: Int = 0)
private class CubeGameMinimumTotal(val redCubes: Int = 0, val greenCubes: Int = 0, val blueCubes: Int = 0) {
    fun power(): Int = redCubes * greenCubes * blueCubes
}
private class CubeGame(val id: Int, val cubeGameTotal: CubeGameTotal = CubeGameTotal(), val cubeGameMinimumTotal: CubeGameMinimumTotal = CubeGameMinimumTotal(), var isValid: Boolean = false, val cubeGameSets: List<CubeGameSet> = emptyList()) {
    fun print() = println("Game $id: ${cubeGameTotal.redCubes} red, ${cubeGameTotal.greenCubes} green, ${cubeGameTotal.blueCubes} blue -> valid: $isValid")
    fun power() = cubeGameMinimumTotal.power()
}
