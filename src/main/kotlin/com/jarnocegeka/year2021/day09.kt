package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day09Part1() {
    val lines = readInputFileLines("InputYear2021Day09.txt")
    val locations = lines.map { line -> line.toCharArray().map { it.digitToInt() }.toIntArray() }.toTypedArray()

    val lowPoints = findLowPoints(locations)

    val result = lowPoints.sumOf { it.height } + lowPoints.size
    println("Result: $result")
}

fun adventOfCodeYear2021Day09Part2() {
    val lines = readInputFileLines("InputYear2021Day09.txt")
    val locations = lines.map { line -> line.toCharArray().map { it.digitToInt() }.toIntArray() }.toTypedArray()

    val lowPoints = findLowPoints(locations)
    val maxY = locations.size
    val maxX = locations[0].size

    val basins = mutableListOf<List<Coordinate>>()
    lowPoints.forEach {
        basins.add(findBasin(it, locations, maxX, maxY))
    }

    basins.sortByDescending { it.size }
    val result = basins[0].size * basins[1].size * basins[2].size

    println("Result: $result")
}

private fun findBasin(lowPoint: Coordinate, locations: Array<IntArray>, maxX: Int, maxY: Int): List<Coordinate> {
    val basinCoordinates = mutableListOf<Coordinate>()
    val queue = mutableListOf<Coordinate>()
    queue.add(lowPoint)
    basinCoordinates.add(lowPoint)

    while (queue.size > 0) {
        val current = queue.removeFirst()

        val x = current.x
        val y = current.y

        val validCoordinates = mutableListOf<Coordinate>()
        if (isValid(x, y - 1, maxX, maxY, locations, basinCoordinates)) validCoordinates.add(Coordinate(x, y - 1, locations[y - 1][x]))
        if (isValid(x - 1, y, maxX, maxY, locations, basinCoordinates)) validCoordinates.add(Coordinate(x - 1, y, locations[y][x - 1]))
        if (isValid(x + 1, y, maxX, maxY, locations, basinCoordinates)) validCoordinates.add(Coordinate(x + 1, y, locations[y][x + 1]))
        if (isValid(x, y + 1, maxX, maxY, locations, basinCoordinates)) validCoordinates.add(Coordinate(x, y + 1, locations[y + 1][x]))

        basinCoordinates.addAll(validCoordinates)
        queue.addAll(validCoordinates)
    }

    return basinCoordinates
}

private fun isValid(x: Int, y: Int, maxX: Int, maxY: Int, locations: Array<IntArray>, basinCoordinates: MutableList<Coordinate>): Boolean {
    if (x < 0 || x >= maxX || y < 0 || y >= maxY || locations[y][x] == 9 || basinCoordinates.any { it.x == x && it.y == y }) {
        return false
    }

    return true
}

private fun findLowPoints(locations: Array<IntArray>): List<Coordinate> {
    val lowPoints = mutableListOf<Coordinate>()
    for (y in locations.indices) {
        for (x in 0 until locations[y].size) {
            val location = locations[y][x]
            val adjacentLocationHeights = findAdjacentLocationHeights(locations, x, y)
            if (adjacentLocationHeights.all { it > location }) lowPoints.add(Coordinate(x, y, location))
        }
    }

    return lowPoints
}

private fun findAdjacentLocationHeights(locations: Array<IntArray>, x: Int, y: Int): List<Int> {
    val adjacentLocationHeights = mutableListOf<Int>()
    when (y) {
        0 -> adjacentLocationHeights.add(locations[y + 1][x])
        locations.size - 1 -> adjacentLocationHeights.add(locations[y - 1][x])
        else -> {
            adjacentLocationHeights.add(locations[y + 1][x])
            adjacentLocationHeights.add(locations[y - 1][x])
        }
    }

    when (x) {
        0 -> adjacentLocationHeights.add(locations[y][x + 1])
        locations[y].size - 1 -> adjacentLocationHeights.add(locations[y][x - 1])
        else -> {
            adjacentLocationHeights.add(locations[y][x + 1])
            adjacentLocationHeights.add(locations[y][x - 1])
        }
    }

    return adjacentLocationHeights
}

data class Coordinate(val x: Int, val y: Int, val height: Int)