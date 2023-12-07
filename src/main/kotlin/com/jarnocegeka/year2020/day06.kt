package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2020Day06Part1() {
    val fileLines = readInputFileLines("year2020/InputYear2020Day06.txt")

    val yesForEachGroup = mutableListOf<Int>()
    var countPerGroup = 0
    val alreadyAnswered = mutableSetOf<Char>()
    fileLines.forEachIndexed { index, it ->
        if (it.isBlank()) {
            yesForEachGroup.add(countPerGroup)
            countPerGroup = 0
            alreadyAnswered.clear()
        } else {
            it.forEach { char ->
                if (!alreadyAnswered.contains(char)) {
                    alreadyAnswered.add(char)
                    countPerGroup++
                }
            }
            if (index == fileLines.lastIndex) yesForEachGroup.add(countPerGroup)
        }
    }

    val total = yesForEachGroup.reduce { i, j -> i + j }
    println("Total answered questions: $total")
}

fun adventOfCodeYear2020Day06Part2() {
    val fileLines = readInputFileLines("year2020/InputYear2020Day06.txt")

    val yesForEachGroup = mutableListOf<Int>()
    var personCount = 0
    val questionCount = mutableMapOf<Char, Int>()
    fileLines.forEachIndexed { index, it ->
        if (it.isBlank()) {
            val countPerGroup = questionCount.entries.filter { (c, count) -> count == personCount }.count()
            yesForEachGroup.add(countPerGroup)
            personCount = 0
            questionCount.clear()
        } else {
            personCount++
            it.forEach { char ->
                questionCount.putIfAbsent(char, 0)
                questionCount[char] = questionCount[char]!! + 1
            }

            if (index == fileLines.lastIndex) {
                val countPerGroup = questionCount.entries.filter { (c, count) -> count == personCount }.count()
                yesForEachGroup.add(countPerGroup)
            }
        }
    }

    val total = yesForEachGroup.reduce { i, j -> i + j }
    println("Total answered questions: $total")
}