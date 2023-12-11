package com.jarnocegeka.year2023

import com.jarnocegeka.model.Coordinate
import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2023Day11Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day11.txt")
    val universe = mapToUniverse(lines)
    universe.expandUniverse()

    universe.print()

    //Find Rows and Columns fully made of points
    //Expand universe

    //Formula to calculate pairs of n numbers => n*(n-1)/2
}

fun adventOfCodeYear2023Day11Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day11.txt")
}

private fun mapToUniverse(lines: List<String>): Universe {
    return Universe(lines.mapIndexed { y, spaces -> mapToSpaces(spaces, y) })
}

private fun mapToSpaces(line: String, y: Int): List<Space> {
    return line.mapIndexed { x, value -> Space(value, Coordinate(x, y)) }
}

private class Universe(var spaces: List<List<Space>>) {
    fun flatSpaces() = spaces.flatten().toMutableList()
    fun expandUniverse() {
        expandSpaceRows()
        expandSpaceColumns()
    }

    private fun expandSpaceRows() {
        val flatSpaces = flatSpaces()
        val spaceColumnSize = spaces.first().size
        val rowsToExpand = (0 until spaceColumnSize)
            .filter { flatSpaces.count { space -> space.coordinate.y == it && !space.isGalaxy() } == spaceColumnSize }

        rowsToExpand.forEach { y -> flatSpaces.filter { it.coordinate.y > y }.forEach { it.coordinate.y += 1 } }

        val rowsToAdd = (0 until spaceColumnSize).flatMap { x -> rowsToExpand.map { y -> Space('.', Coordinate(x, y + 1)) } }
        flatSpaces.addAll(rowsToAdd)
        spaces = flatSpaces.sortedBy { it.coordinate.y }.groupBy { it.coordinate.y }.values.map { it.sortedBy { column -> column.coordinate.x } }.toList()
    }

    private fun expandSpaceColumns() {
        val flatSpaces = flatSpaces()
        val spaceRowSize = spaces.size
        val columnsToExpand = (0 until spaceRowSize)
            .filter { flatSpaces.count { space -> space.coordinate.x == it && !space.isGalaxy() } == spaceRowSize }

        columnsToExpand.forEach { x -> flatSpaces.filter { it.coordinate.x > x }.forEach { it.coordinate.x += 1 } }

        val columnsToAdd = (0 until spaceRowSize).flatMap { y -> columnsToExpand.map { x -> Space('.', Coordinate(x + 1, y)) } }
        flatSpaces.addAll(columnsToAdd)
        spaces = flatSpaces.sortedBy { it.coordinate.y }.groupBy { it.coordinate.y }.values.map { it.sortedBy { column -> column.coordinate.x } }.toList()
    }

    fun print() {
        spaces.forEach { row ->
            row.forEach { print("${it.value} ") }
            println()
        }
    }
}

private class Space(val value: Char, var coordinate: Coordinate) {
    fun isGalaxy():Boolean {
        return value == '#'
    }
}