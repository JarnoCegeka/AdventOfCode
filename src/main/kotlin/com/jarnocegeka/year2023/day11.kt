package com.jarnocegeka.year2023

import com.jarnocegeka.model.Coordinate
import com.jarnocegeka.utils.*

fun adventOfCodeYear2023Day11Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day11.txt")
    runUniversePathSimulator(lines, 1)
}

fun adventOfCodeYear2023Day11Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day11.txt")
    runUniversePathSimulator(lines, 999999)
}

private fun runUniversePathSimulator(lines: List<String>, expansion: Int) {
    val universe = mapToUniverse(lines)
    universe.expandUniverse(expansion)

    val galaxyPairs = universe.galaxyPairs()
    val aStarSearchWithManhattanDistance = galaxyPairs.associateWith { aStarSearchWithManhattanDistance(it.first, it.second) }.values.toList().reduceToLongWithSum()
    println("Result with expansion of $expansion: $aStarSearchWithManhattanDistance")
}

private fun mapToUniverse(lines: List<String>): Universe {
    return Universe(lines.mapIndexed { y, spaces -> mapToSpaces(spaces, y) })
}

private fun mapToSpaces(line: String, y: Int): List<Space> {
    return line.mapIndexed { x, value -> Space(value, Coordinate(x, y)) }
}

private class Universe(var spaces: List<List<Space>>) {
    fun flatSpaces() = spaces.flatten().toMutableList()
    fun expandUniverse(expansion: Int = 1) {
        expandSpaceRows(expansion)
        expandSpaceColumns(expansion)
    }

    private fun expandSpaceRows(expansion: Int) {
        val flatSpaces = flatSpaces()
        val spaceColumnSize = spaces.first().size
        val rowsToExpand = (0 until spaceColumnSize)
                .filter { flatSpaces.count { space -> space.coordinate.y == it && !space.isGalaxy() } == spaceColumnSize }

        rowsToExpand.forEachIndexed { index, y -> flatSpaces.filter { it.coordinate.y > y + (index * expansion) }.forEach { it.coordinate.y += expansion } }
    }

    private fun expandSpaceColumns(expansion: Int) {
        val flatSpaces = flatSpaces()
        val spaceRowSize = spaces.size
        val columnsToExpand = (0 until spaceRowSize)
            .filter { flatSpaces.count { space -> space.coordinate.x == it && !space.isGalaxy() } == spaceRowSize }

        columnsToExpand.forEachIndexed { index, x -> flatSpaces.filter { it.coordinate.x > x + (index * expansion) }.forEach { it.coordinate.x += expansion } }
    }

    fun galaxyPairs(): List<Pair<Coordinate, Coordinate>> {
        val galaxies = flatSpaces().filter { it.isGalaxy() }
        val galaxiesCopy = galaxies.toMutableList()

        return galaxies.flatMap { galaxy ->
            galaxiesCopy.removeIf { it.coordinate == galaxy.coordinate }
            galaxiesCopy.map { Pair(galaxy.coordinate, it.coordinate) }
        }
    }
}

private class Space(val value: Char, var coordinate: Coordinate) {
    fun isGalaxy():Boolean {
        return value == '#'
    }
}