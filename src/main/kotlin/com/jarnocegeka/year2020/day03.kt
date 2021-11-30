package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2020Day03Part1() {
    val lines = readInputFileLines("InputYear2020Day03.txt")
    println(traverse(lines, 3, 1))
}

fun adventOfCodeYear2020Day03Part2() {
    val lines = readInputFileLines("InputYear2020Day03.txt")

    val traversal1 = traverse(lines, 1, 1).toLong()
    val traversal2 = traverse(lines, 3, 1).toLong()
    val traversal3 = traverse(lines, 5, 1).toLong()
    val traversal4 = traverse(lines, 7, 1).toLong()
    val traversal5 = traverse(lines, 1, 2).toLong()

    val result = traversal1 * traversal2 * traversal3 * traversal4 * traversal5

    println("Result: $traversal1 * $traversal2 * $traversal3 * $traversal4 * $traversal5 = $result")
}

private fun traverse(lines: List<String>, stepsRight: Int, stepsDown: Int) : Int {
    val resetIndex = 30
    var x = 0
    var y = 0
    var treeCount = 0

    while ((y + stepsDown) < lines.size) {
        y += stepsDown

        if (x + stepsRight > resetIndex) {
            x = (x + stepsRight) - resetIndex - 1
        } else {
            x += stepsRight
        }

        val position = lines[y][x]
        if (position == '#') treeCount++
    }

    return treeCount
}