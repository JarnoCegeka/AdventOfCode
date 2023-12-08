package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2023Day08Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day08.txt")
    val instructions = lines[0]
    val mapElements = mapToMapElements(lines.subList(0, lines.size))

    println(traverseMap(mapElements, instructions))
}

fun adventOfCodeYear2023Day08Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day08.txt")
    val instructions = lines[0]
    val mapElements = mapToMapElements(lines.subList(0, lines.size))

    println(traverseMapMulti(mapElements, instructions))
}

private fun mapToMapElements(lines: List<String>): List<MapElement> {
    return lines.map { mapToMapElement(it) }
}

private fun mapToMapElement(line: String): MapElement {
    val value = line.substringBefore("=").trim()
    val left = line.substringAfter("(").substringBefore(",").trim()
    val right = line.substringAfter(",").substringBefore(")").trim()

    return MapElement(value, left, right)
}

private fun traverseMap(mapElements: List<MapElement>, instructions: String, start: String = "AAA", destination: String = "ZZZ"): Int {
    val mapElementsByValue = mapElements.associateBy { it.value }

    var stepsTaken = 0
    var current = start
    while (true) {
        instructions.forEach { instruction ->
            if (current == destination) return stepsTaken

            current = mapElementsByValue[current]!!.followInstruction(instruction)
            stepsTaken++
        }
    }
}

private fun traverseMapWithDestinationEndingOn(mapElements: List<MapElement>, instructions: String, start: String = "AAA", destinationsEndingOn: String = "Z"): Pair<String, Long> {
    val mapElementsByValue = mapElements.associateBy { it.value }

    var stepsTaken = 0L
    var current = start
    while (true) {
        instructions.forEach { instruction ->
            if (current.endsWith(destinationsEndingOn)) return Pair(current, stepsTaken)

            current = mapElementsByValue[current]!!.followInstruction(instruction)
            stepsTaken++
        }
    }
}

private fun traverseMapMulti(mapElements: List<MapElement>, instructions: String, startingPositionsEndingOn: String = "A", destinationsEndingOn: String = "Z"): Long {
    val currentPositions = mapElements.filter { it.value.endsWith(startingPositionsEndingOn) }.map { it.value }
    val positions = currentPositions.associateWith { traverseMapWithDestinationEndingOn(mapElements, instructions, it, destinationsEndingOn) }.map { it.value.second }

    return lcm(positions)
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a

    return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b));
}

fun lcm(input: List<Long>): Long {
    var result = input[0]
    (1 until input.size).forEach { result = lcm(result, input[it]) }
    return result
}

private class MapElement(val value: String, val left: String, val right: String) {
    fun followInstruction(instruction: Char): String {
        return when(instruction) {
            'L' -> left
            else -> right
        }
    }
}