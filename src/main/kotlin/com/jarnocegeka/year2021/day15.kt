package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

typealias Point = Pair<Int, Int>

fun adventOfCodeYear2021Day15Part1() {
    val lines = readInputFileLines("InputYear2021Day15.txt")
    val input = lines.map { line -> line.map { it.digitToInt() } }
    println(solveRecursive(input, mapOf((0 to 0) to 0)).toLong())
}

fun adventOfCodeYear2021Day15Part2() {
    val lines = readInputFileLines("InputYear2021Day15.txt")
    val input = lines.map { line -> line.map { it.digitToInt() } }
    println(solveRecursive(enlargeMap(input), mapOf((0 to 0) to 0)).toLong())
}

private fun enlargeMap(ys: List<List<Int>>): List<List<Int>> {
    val something = ys.map { xs -> (0..4).fold(emptyList<Int>()) { acc, i -> acc + enlargeRowTimes(xs, i) } }

    return (0..4).fold(emptyList()) { acc, i -> acc + something.map { enlargeRowTimes(it, i) } }
}

private tailrec fun enlargeRowTimes(xs: List<Int>, times: Int): List<Int> = when (times) {
    0 -> xs
    1 -> enlargeRow(xs)
    else -> enlargeRowTimes(enlargeRow(xs), times - 1)
}

private fun enlargeRow(xs: List<Int>) = xs.map { new ->
    when (new + 1) {
        10 -> 1
        else -> new + 1
    }
}

private tailrec fun solveRecursive(input: List<List<Int>>, grid: Map<Point, Int>): Int {
    val w = input.first().size
    val h = input.size

    val newGrid = grid.flatMap { (point, cost) ->
        getNeighbours(
            x = point.first, y = point.second, input.first().size, input.size
        ).map { (x, y) -> (x to y) to input[y][x] + cost } + listOf(point to cost)
    }.groupBy { it.first }.mapValues { (_, value) -> value.minOf { pp -> pp.second } }.toMap()

    if (newGrid == grid) {
        return grid[(h - 1) to (w - 1)]!!
    }

    return solveRecursive(input, newGrid)
}

private fun getNeighbours(x: Int, y: Int, w: Int, h: Int) = listOf(
    x - 1 to y, x to y - 1, x + 1 to y, x to y + 1
).filter { (x, y) -> x >= 0 && y >= 0 && x < w && y < h }