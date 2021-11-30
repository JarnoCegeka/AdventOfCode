package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFile

fun adventOfCodeYear2020Day02Part1() {
    val file = readInputFile("InputYear2020Day02.txt")

    var validPwCount = 0
    file.forEachLine {
        val pwSubstring = it.substringAfterLast(" ")
        val letterIndex = it.substringBefore(":").lastIndex
        val letterSubstring = it.substring(letterIndex, letterIndex + 1)[0]
        val numbersSubstring = it.substringBefore(" ")
        val atLeastNumber = numbersSubstring.substringBefore("-").toInt()
        val atMostNumber = numbersSubstring.substringAfter("-").toInt()

        val count = pwSubstring.count { letter -> letter == letterSubstring }
        if (count in atLeastNumber..atMostNumber) validPwCount++
    }

    println(validPwCount)
}

fun adventOfCodeYear2020Day02Part2() {
    val file = readInputFile("InputYear2020Day02.txt")

    var validPwCount = 0
    file.forEachLine {
        val pwSubstring = it.substringAfter(": ")
        val letterIndex = it.substringBefore(":").lastIndex
        val letterToFind = it.substring(letterIndex, letterIndex + 1)[0]
        val numbersSubstring = it.substringBefore(" ")
        val firstIndex = numbersSubstring.substringBefore("-").toInt() - 1
        val secondIndex = numbersSubstring.substringAfter("-").toInt() - 1

        val firstIndexTest = pwSubstring[firstIndex] == letterToFind
        val secondIndexTest = secondIndex < pwSubstring.length && pwSubstring[secondIndex] == letterToFind

        if ((firstIndexTest && !secondIndexTest) || (!firstIndexTest && secondIndexTest)) {
            validPwCount++
        }
    }

    println(validPwCount)
}