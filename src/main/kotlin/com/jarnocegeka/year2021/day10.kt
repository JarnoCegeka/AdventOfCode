package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day10Part1() {
    val lines = readInputFileLines("InputYear2021Day10.txt").map { it.toMutableList() }

    val illegalCharacters = mutableListOf<Char>()
    lines.forEach { line ->
        var illegal = false
        val queue = mutableListOf<Char>()
        queue.add(line.removeFirst())

        while (queue.isNotEmpty() && line.isNotEmpty() && !illegal) {
            val charToClose = queue.last()
            val currentChar = line.removeFirst()

            if (currentChar == chunkDelimiters[charToClose]) {
                queue.removeLast()
            } else if (chunkDelimiters.containsKey(currentChar)) {
                queue.add(currentChar)
            } else if (currentChar != chunkDelimiters[charToClose] && line.isNotEmpty()) {
                illegal = true
                illegalCharacters.add(currentChar)
            }
        }
    }

    val result = illegalCharacters.sumOf { illegalCharScore[it]!! }

    println("Result: $result")
}

fun adventOfCodeYear2021Day10Part2() {
    val lines = readInputFileLines("InputYear2021Day10.txt").map { it.toMutableList() }
    val incompleteLines = findCharactersToClose(lines)

    val scores = incompleteLines.map { line ->
        var score = 0L
        line.reversed().map { autoCompleteScore[it]!! }.forEach {
            score *= 5L
            score += it
        }
        score
    }.sorted()

    val middleIndex = scores.size / 2
    val result = scores[middleIndex]

    println("Result: $result")
}

private fun findCharactersToClose(lines: List<MutableList<Char>>): List<List<Char>> {
    val incompleteLines = mutableListOf<MutableList<Char>>()
    lines.forEach { line ->
        val lineToCheck = line.toMutableList()
        var illegal = false
        val queue = mutableListOf<Char>()
        queue.add(lineToCheck.removeFirst())

        while (lineToCheck.isNotEmpty() && !illegal) {
            val currentChar = lineToCheck.removeFirst()
            if (queue.isEmpty()) {
                queue.add(currentChar)
            } else {
                val charToClose = queue.last()

                if (lineToCheck.isNotEmpty()) {
                    when {
                        currentChar == chunkDelimiters[charToClose] -> queue.removeLast()
                        chunkDelimiters.containsKey(currentChar) -> queue.add(currentChar)
                        currentChar != chunkDelimiters[charToClose] -> illegal = true
                    }
                } else {
                    if (currentChar == chunkDelimiters[charToClose]) queue.removeLast()
                    if (chunkDelimiters.containsKey(currentChar)) queue.add(currentChar)

                    if (queue.size > 1) incompleteLines.add(queue)
                }
            }
        }
    }

    return incompleteLines
}

private val chunkDelimiters = mapOf(Pair('(', ')'), Pair('{', '}'), Pair('[', ']'), Pair('<', '>'))
private val illegalCharScore = mapOf(Pair(')', 3), Pair('}', 1197), Pair(']', 57), Pair('>', 25137))
private val autoCompleteScore = mapOf(Pair('(', 1L), Pair('{', 3L), Pair('[', 2L), Pair('<', 4L))