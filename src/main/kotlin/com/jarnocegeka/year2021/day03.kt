package com.jarnocegeka.year2021

import com.jarnocegeka.utils.binaryToDecimal
import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day03Part1() {
    val lines = readInputFileLines("year2021/InputYear2021Day03.txt")

    val binarySize = lines[0].length
    val ones = MutableList(binarySize) { 0 }
    val zeros = MutableList(binarySize) { 0 }
    var gammaRate = ""
    var epsilonRate = ""
    lines.forEach { binaryNumber ->
        binaryNumber.toCharArray()
            .forEachIndexed { index, binary ->
                when (binary) {
                    '0' -> zeros[index] += 1
                    '1' -> ones[index] += 1
                }
                println("zeros: $zeros, ones: $ones")
            }
        println("---------------------------------------------------------")
    }

    (0 until binarySize).forEach {
        val zeroCount = zeros[it]
        val oneCount = ones[it]
        val mostCommonBit = if (zeroCount > oneCount) '0' else '1'
        val leastCommonBit = if (zeroCount < oneCount) '0' else '1'

        gammaRate += mostCommonBit
        epsilonRate += leastCommonBit
    }

    val gammaRateDecimal = binaryToDecimal(gammaRate)
    val epsilonRateDecimal = binaryToDecimal(epsilonRate)

    println(gammaRateDecimal)
    println(epsilonRateDecimal)
    val result = gammaRateDecimal * epsilonRateDecimal
    println(result)
}

fun adventOfCodeYear2021Day03Part2() {
    val lines = readInputFileLines("year2021/InputYear2021Day03.txt")

    val binarySize = lines[0].length
    var oneCount1 = 0
    var oneCount2 = 0
    var zeroCount1 = 0
    var zeroCount2 = 0
    var binaryPosition = 0
    val mutableListForOxygenRating= lines.toMutableList()
    val mutableListForCo2ScrubberRating= lines.toMutableList()

    while (binaryPosition != binarySize) {
        if (mutableListForOxygenRating.size > 1) {
            mutableListForOxygenRating.forEach { binaryString -> if (binaryString[binaryPosition] == '0') zeroCount1++ else oneCount1++ }
            if (oneCount1 > zeroCount1 || zeroCount1 == oneCount1) mutableListForOxygenRating.removeIf { it[binaryPosition] == '0' } else mutableListForOxygenRating.removeIf { it[binaryPosition] == '1' }
        }

        if (mutableListForCo2ScrubberRating.size > 1) {
            mutableListForCo2ScrubberRating.forEach { binaryString -> if (binaryString[binaryPosition] == '0') zeroCount2++ else oneCount2++ }
            if (oneCount2 > zeroCount2 || zeroCount2 == oneCount2) mutableListForCo2ScrubberRating.removeIf { it[binaryPosition] == '1' } else mutableListForCo2ScrubberRating.removeIf { it[binaryPosition] == '0' }
        }

        oneCount1 = 0
        oneCount2 = 0
        zeroCount1 = 0
        zeroCount2 = 0
        binaryPosition++
    }

    val oxygenRatingBinary = mutableListForOxygenRating[0]
    val co2ScrubberRatingBinary = mutableListForCo2ScrubberRating[0]
    val oxygenRating = binaryToDecimal(oxygenRatingBinary)
    val co2ScrubberRating = binaryToDecimal(co2ScrubberRatingBinary)
    val result = oxygenRating * co2ScrubberRating

    println(oxygenRatingBinary)
    println(co2ScrubberRatingBinary)
    println(oxygenRating)
    println(co2ScrubberRating)
    println(result)
}