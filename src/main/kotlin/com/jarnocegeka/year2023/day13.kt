package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToIntWithSum
import com.jarnocegeka.utils.splitByWhiteLine

fun adventOfCodeYear2023Day13Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day13.txt")
    val mirrorValleys = mapToMirrorValleys(lines)
    val mirrorValleySummaries = mirrorValleys.map { it.calculateMirrorPlacement() }

    println(mirrorValleySummaries.reduceToIntWithSum())
}

fun adventOfCodeYear2023Day13Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day13.txt")
    val mirrorValleys = mapToMirrorValleys(lines)
    val mirrorValleySummaries = mirrorValleys.map { it.calculateMirrorPlacement(1) }

    println(mirrorValleySummaries.reduceToIntWithSum())
}

private fun mapToMirrorValleys(lines: List<String>): List<MirrorValley> {
    return splitByWhiteLine(lines).map { mapToMirrorValley(it) }
}

private fun mapToMirrorValley(lines: List<String>): MirrorValley {
    val columnCount = lines.first().length
    val columns = (0 until columnCount).mapIndexed { index, column ->
        Pair(index, lines.map { it[column] }.joinToString("") { it.toString() })
    }.toMap()
    val rows = lines.mapIndexed { index, row -> Pair(index, row) }.toMap()

    return MirrorValley(rows, columns)
}

private class Mirror(val range: IntRange) {
    fun calculateColumnsLeftOfMirror(): Int = calculateValueBeforeMirror()
    fun calculateRowsAboveOfMirror(): Int = calculateValueBeforeMirror()
    private fun calculateValueBeforeMirror(): Int = range.first + 1
}

private class MirrorValley(val rows: Map<Int, String>, val columns: Map<Int, String>) {
    fun calculateMirrorPlacement(maxAmountOfDifferences: Int = 0): Int {
        val columnsLeftOfMirror = findMirrorBetweenColumns(maxAmountOfDifferences)?.calculateColumnsLeftOfMirror() ?: 0
        val rowsAboveMirror = findMirrorBetweenRows(maxAmountOfDifferences)?.calculateRowsAboveOfMirror() ?: 0

        if (columnsLeftOfMirror != 0  && rowsAboveMirror != 0) {
            print()
            println()
        }

        return columnsLeftOfMirror + (rowsAboveMirror * 100)
    }

    fun findMirrorBetweenRows(maxAmountOfDifferences: Int): Mirror? {
        return findMirrorBetween(rows, maxAmountOfDifferences)
    }

    fun findMirrorBetweenColumns(maxAmountOfDifferences: Int): Mirror? {
        return findMirrorBetween(columns, maxAmountOfDifferences)
    }

    private fun findMirrorBetween(value: Map<Int, String>, maxAmountOfDifferences: Int): Mirror? {
        val indexesNextToAMirror = (0 until value.size step 1).filter { equals(value[it], value[it + 1], maxAmountOfDifferences) }

        return indexesNextToAMirror.firstNotNullOfOrNull { findMirrorBetweenFor(value, it, maxAmountOfDifferences) }
    }

    private fun findMirrorBetweenFor(value: Map<Int, String>, indexNextToMirror: Int, maxAmountOfDifferences: Int): Mirror? {
        val rangeAroundMirror = IntRange(indexNextToMirror, indexNextToMirror + 1)

        return if (doesReflectionGoToTheEnd(value, rangeAroundMirror, maxAmountOfDifferences)) Mirror(rangeAroundMirror) else null
    }

    private fun doesReflectionGoToTheEnd(value: Map<Int, String>, rangeAroundMirror: IntRange, maxAmountOfDifferences: Int): Boolean {
        if (rangeAroundMirror.first == 0 || rangeAroundMirror.last == value.size - 1) return true

        var differenceCounter = differencesBetween(value[rangeAroundMirror.first], value[rangeAroundMirror.last])
        var rangeToCheck = IntRange(rangeAroundMirror.first - 1, rangeAroundMirror.last + 1)
        while (rangeToCheck.first >= 0 && rangeToCheck.last <= value.size - 1) {
            if (differenceCounter > maxAmountOfDifferences) return false

            differenceCounter += differencesBetween(value[rangeToCheck.first], value[rangeToCheck.last])
            rangeToCheck = IntRange(rangeToCheck.first - 1, rangeToCheck.last + 1)
        }

        return differenceCounter == maxAmountOfDifferences
    }

    private fun equals(a: String?, b: String?, maxAmountOfDifferences: Int): Boolean {
        if (a == null || b == null) return false
        if (maxAmountOfDifferences == 0) return a == b
        if (a.length != b.length) return false

        return differencesBetween(a, b) <= maxAmountOfDifferences
    }

    private fun differencesBetween(a: String?, b: String?): Int {
         return when {
             a == null && b == null -> 0
             a == null -> b!!.length
             b == null -> a.length
             else -> a.indices.count { a[it] != b[it] }
         }
    }

    fun print() {
        rows.forEach { println(it.value) }
    }
}