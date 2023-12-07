package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day14Part1() {
    val lines = readInputFileLines("year2021/InputYear2021Day14.txt")
    var polymerTemplate = lines[0]
    val pairInsertions = lines.subList(2, lines.size)
            .map { it.split(" -> ") }
            .map { Pair(it[0], "${it[0][0]}${it[1]}") }
            .toMap()

    repeat(10) {
        var newPolymerTemplate = ""
        val polymerTemplateChars = polymerTemplate.toCharArray()
        (0 until polymerTemplateChars.size - 1).forEach {
            val pair = "${polymerTemplateChars[it]}${polymerTemplateChars[it + 1]}"
            val pairInsertion = pairInsertions[pair] ?: pair
            newPolymerTemplate += pairInsertion
        }
        polymerTemplate = "$newPolymerTemplate${polymerTemplateChars.last()}"
    }

    val charCounts = polymerTemplate.toSet()
            .associateWith { char -> polymerTemplate.count { it == char } }
    val mostCommonCharCount = charCounts.maxOf { it.value }
    val leastCommonCharCount = charCounts.minOf { it.value }
    val result = mostCommonCharCount - leastCommonCharCount

    println("Polymer template: $polymerTemplate")
    println("Result: $result")
}

fun adventOfCodeYear2021Day14Part2() {
    val lines = readInputFileLines("year2021/InputYear2021Day14.txt")
    val polymerTemplate = lines[0]
    val pairInsertions = lines.subList(2, lines.size)
            .map { it.split(" -> ") }
            .associate { Pair(it[0], it[1]) }

    val pairs = (0 until polymerTemplate.length - 1)
            .map { "${polymerTemplate[it]}${polymerTemplate[it + 1]}" }
    var pairCounter = pairs.associateWith { key -> pairs.count { it == key }.toLong() }

    repeat(40) {
       val newPairCounter = mutableMapOf<String, Long>()
        pairCounter.keys.forEach { pair ->
            val left = pair[0]
            val right = pair[1]
            val mid = pairInsertions[pair]
            val leftPair = "$left$mid"
            val rightPair = "$mid$right"
            newPairCounter[leftPair] = (newPairCounter[leftPair] ?: 0L) + (pairCounter[pair] ?: 0L)
            newPairCounter[rightPair] = (newPairCounter[rightPair] ?: 0L) + (pairCounter[pair] ?: 0L)
        }

        pairCounter = newPairCounter
    }

    val charCounter = mutableMapOf<Char, Long>()
    pairCounter.forEach { key, value ->
        val left = key[0]
        charCounter[left] = (charCounter[left] ?: 0L) + value
    }
    val lastChar = polymerTemplate.last()
    charCounter[lastChar] = (charCounter[lastChar] ?: 0L) + 1L

    val mostCommonCharCount = charCounter.maxOf { it.value }
    val leastCommonCharCount = charCounter.minOf { it.value }
    val result = mostCommonCharCount - leastCommonCharCount

    println("Result: $result")
}