package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines

private const val OCCUPIED = '#'
private const val EMPTY = 'L'
private const val FLOOR = '.'

fun adventOfCodeYear2020Day11Part1() {
    val ferryLayout = ferryLayout()

    var newFerryLayout = applySeatRulesForPart1(ferryLayout)
    var layoutChanged = arrayContentChanged(newFerryLayout, ferryLayout)
    var iterations = 1
    while (layoutChanged) {

        val prevFerryLayout = newFerryLayout.copyOf()
        newFerryLayout = applySeatRulesForPart1(newFerryLayout)
        layoutChanged = arrayContentChanged(newFerryLayout, prevFerryLayout)

        iterations++
    }

    val occupiedCount = newFerryLayout.map { it.count { char -> char == OCCUPIED } }.reduce { acc, i -> acc + i }

    println("$occupiedCount occupied seats after $iterations iterations")
}

private fun applySeatRulesForPart1(ferryLayout: Array<CharArray>): Array<CharArray> {
    val newFerryLayout = Array(ferryLayout.size) { CharArray(ferryLayout[0].size) }
    var seatsChangedCount = 0
    for ((rowIndex, row) in ferryLayout.withIndex()) {
        for ((colIndex, seat) in row.withIndex()) {
            val adjacentSeats = adjacentSeatsForPart1(rowIndex, colIndex, ferryLayout)
            if (seat == EMPTY && !adjacentSeats.contains('#')) {
                newFerryLayout[rowIndex][colIndex] = '#'
                seatsChangedCount++
            } else if (seat == OCCUPIED && adjacentSeats.count { it == '#' } >= 4) {
                newFerryLayout[rowIndex][colIndex] = 'L'
                seatsChangedCount++
            } else {
                newFerryLayout[rowIndex][colIndex] = ferryLayout[rowIndex][colIndex]
            }
        }
    }
    println("Number of seats changed: $seatsChangedCount")

    return newFerryLayout
}

private fun adjacentSeatsForPart1(row: Int, col: Int, ferryLayout: Array<CharArray>): List<Char> {
    val prevRowIndex = row - 1
    val nextRowIndex = row + 1
    val prevColIndex = col - 1
    val nextColIndex = col + 1
    val lastSeatOfRowIndex = ferryLayout[0].lastIndex
    val adjacentSeats = mutableListOf<Char>()

    if (prevRowIndex >= 0) {
        if (prevColIndex >= 0) adjacentSeats.add(ferryLayout[prevRowIndex][prevColIndex])
        if (nextColIndex <= lastSeatOfRowIndex) adjacentSeats.add(ferryLayout[prevRowIndex][nextColIndex])
        adjacentSeats.add(ferryLayout[prevRowIndex][col])
    }

    if (nextRowIndex <= ferryLayout.lastIndex) {
        if (prevColIndex >= 0) adjacentSeats.add(ferryLayout[nextRowIndex][prevColIndex])
        if (nextColIndex <= lastSeatOfRowIndex) adjacentSeats.add(ferryLayout[nextRowIndex][nextColIndex])
        adjacentSeats.add(ferryLayout[nextRowIndex][col])
    }

    if (prevColIndex >= 0) adjacentSeats.add(ferryLayout[row][prevColIndex])
    if (nextColIndex <= lastSeatOfRowIndex) adjacentSeats.add(ferryLayout[row][nextColIndex])

    for (i in 0 until (8 - adjacentSeats.size)) {
        adjacentSeats.add('.')
    }

    return adjacentSeats
}

fun adventOfCodeYear2020Day11Part2() {
    val ferryLayout = ferryLayout()

    var newFerryLayout = applySeatRulesForPart2(ferryLayout)
    var layoutChanged = arrayContentChanged(newFerryLayout, ferryLayout)
    var iterations = 1
    while (layoutChanged) {

        val prevFerryLayout = newFerryLayout.copyOf()
        newFerryLayout = applySeatRulesForPart2(newFerryLayout)
        layoutChanged = arrayContentChanged(newFerryLayout, prevFerryLayout)

        iterations++
    }

    val occupiedCount = newFerryLayout.map { it.count { char -> char == OCCUPIED } }.reduce { acc, i -> acc + i }

    println("$occupiedCount occupied seats after $iterations iterations")
}

private fun applySeatRulesForPart2(ferryLayout: Array<CharArray>): Array<CharArray> {
    val newFerryLayout = Array(ferryLayout.size) { CharArray(ferryLayout[0].size) }
    var seatsChangedCount = 0
    for ((rowIndex, row) in ferryLayout.withIndex()) {
        for ((colIndex, seat) in row.withIndex()) {
            val adjacentSeats = adjacentSeatsForPart2(rowIndex, colIndex, ferryLayout)
            if (seat == EMPTY && !adjacentSeats.contains('#')) {
                newFerryLayout[rowIndex][colIndex] = '#'
                seatsChangedCount++
            } else if (seat == OCCUPIED && adjacentSeats.count { it == '#' } >= 5) {
                newFerryLayout[rowIndex][colIndex] = 'L'
                seatsChangedCount++
            } else {
                newFerryLayout[rowIndex][colIndex] = ferryLayout[rowIndex][colIndex]
            }
        }
    }
    println("Number of seats changed: $seatsChangedCount")

    return newFerryLayout
}

private fun adjacentSeatsForPart2(row: Int, col: Int, ferryLayout: Array<CharArray>): List<Char> {
    var prevRowIndex = row - 1
    var nextRowIndex = row + 1
    var prevColIndex = col - 1
    var nextColIndex = col + 1
    val lastSeatOfRowIndex = ferryLayout[0].lastIndex
    val adjacentSeats = Array(3) { CharArray(3) { '.' } }

    var shouldContinueSearching = true
    while (shouldContinueSearching) {
        val prevSS = copyArray(adjacentSeats)
        if (prevRowIndex >= 0) {
            if (prevColIndex >= 0 && adjacentSeats[0][0] == '.') adjacentSeats[0][0] = ferryLayout[prevRowIndex][prevColIndex]
            if (adjacentSeats[0][1] == '.') adjacentSeats[0][1] = ferryLayout[prevRowIndex][col]
            if (nextColIndex <= lastSeatOfRowIndex && adjacentSeats[0][2] == '.') adjacentSeats[0][2] = ferryLayout[prevRowIndex][nextColIndex]
        }

        if (nextRowIndex <= ferryLayout.lastIndex) {
            if (prevColIndex >= 0 && adjacentSeats[2][0] == '.') adjacentSeats[2][0] = ferryLayout[nextRowIndex][prevColIndex]
            if (adjacentSeats[2][1] == '.') adjacentSeats[2][1] = ferryLayout[nextRowIndex][col]
            if (nextColIndex <= lastSeatOfRowIndex && adjacentSeats[2][2] == '.') adjacentSeats[2][2] = ferryLayout[nextRowIndex][nextColIndex]
        }

        if (prevColIndex >= 0 && adjacentSeats[1][0] == '.') adjacentSeats[1][0] = ferryLayout[row][prevColIndex]
        if (nextColIndex <= lastSeatOfRowIndex && adjacentSeats[1][2] == '.') adjacentSeats[1][2] = ferryLayout[row][nextColIndex]

        prevRowIndex--
        nextRowIndex++
        prevColIndex--
        nextColIndex++
        val outOfBounds = outOfBounds(prevRowIndex, prevColIndex, nextRowIndex, nextColIndex, ferryLayout)
        shouldContinueSearching = (arrayContentChanged(adjacentSeats, prevSS) && arrayContains(adjacentSeats, '.')) || !outOfBounds
    }

    return adjacentSeats.flatMap { it.toList() }
}

private fun outOfBounds(prevRowIndex: Int, prevColIndex: Int, nextRowIndex: Int, nextColIndex: Int, array: Array<CharArray>): Boolean {
    val lastRowIndex = array[0].lastIndex
    return prevRowIndex < 0 && prevColIndex < 0 && nextRowIndex > lastRowIndex && nextColIndex > lastRowIndex
}

private fun copyArray(array: Array<CharArray>): Array<CharArray> {
    val newArray = Array(array.size) { CharArray(array[0].size) }

    array.forEachIndexed { rowIndex, chars ->
        chars.forEachIndexed { colIndex, c ->
            newArray[rowIndex][colIndex] = c
        }
    }

    return newArray
}

private fun arrayContains(array: Array<CharArray>, char: Char): Boolean {
    return array.any { row -> row.any { col -> col == char } }
}

private fun arrayContentChanged(array: Array<CharArray>, prevArray: Array<CharArray>, withPrintChanges: Boolean = false): Boolean {
    if (withPrintChanges) printChanges(array, prevArray)
    return array.withIndex().any { (rowIndex, row) -> row.withIndex().any { (colIndex, col) -> col != prevArray[rowIndex][colIndex] } }
}

private fun printChanges(ferryLayout: Array<CharArray>, prevFerryLayout: Array<CharArray>) {
    println("************************************************************")
    for ((rowIndex, row) in ferryLayout.withIndex()) {
        val rowString = row.joinToString("")
        val prevRowString = prevFerryLayout[rowIndex].joinToString("")
        println("$prevRowString -- $rowString")
    }
}

private fun ferryLayout(): Array<CharArray> {
    val input = readInputFileLines("year2020/InputYear2020Day11.txt")
    val ferryLayout = mutableListOf<CharArray>()

    input.forEach {
        ferryLayout.add(it.toCharArray())
    }

    return ferryLayout.toTypedArray()
}