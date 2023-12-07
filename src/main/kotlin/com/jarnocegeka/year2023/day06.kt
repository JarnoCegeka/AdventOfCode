package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.year2023.HandType.*
import java.lang.IllegalStateException
import java.util.Comparator

fun adventOfCodeYear2023Day06Part1() {
    val lines = readInputFileLines("InputYear2023Day06.txt")
    val camelCardsGame = mapToCamelCardsGame(lines)
    val ranking = determineRanking(camelCardsGame)

    println("Result: ${calculateTotalWinnings(ranking)}")
}

fun adventOfCodeYear2023Day06Part2() {
    val lines = readInputFileLines("InputYear2023Day06.txt")
    val camelCardsGame = mapToCamelCardsGame(lines, true)
    val ranking = determineRanking(camelCardsGame)

    println("Result: ${calculateTotalWinnings(ranking)}")
}

private fun mapToCamelCardsGame(lines: List<String>, withJokers: Boolean = false): CamelCardsGame {
    return CamelCardsGame(lines.map { mapToHand(it, withJokers) })
}

private fun mapToHand(line: String, withJokers: Boolean = false): Hand {
    val value = line.substringBefore(" ").trim()
    val bid = line.substringAfter(" ").trim().toInt()

    return Hand(value, bid, withJokers = withJokers)
}

private fun determineRanking(cardsGame: CamelCardsGame): List<Hand> {
    return cardsGame.hands.sortedWith(Comparator.comparing(Hand::strength).thenComparator { a, b -> determineHighCard(a, b) })
}

private fun determineHighCard(a: Hand, b: Hand): Int {
    val faceValuesOfAByIndex = a.faceValuesByIndex
    val faceValuesOfBByIndex = b.faceValuesByIndex

    var index = 0
    while (index < faceValuesOfAByIndex.size) {
        val faceValueOfA = faceValuesOfAByIndex[index]!!
        val faceValueOfB = faceValuesOfBByIndex[index]!!

        if (faceValueOfA > faceValueOfB) {
            return 1
        } else if (faceValueOfA < faceValueOfB) {
            return -1
        }

        index++
    }

    return 0
}

private fun calculateTotalWinnings(hands: List<Hand>): Int {
    return hands.mapIndexed { index, hand -> (index + 1) * hand.bid }
        .reduce { accumulator, winning -> accumulator + winning }
}

private fun determineHandType(cardFacesCount: Map<Char, Int>, withJokers: Boolean = false): HandType {
    val mutableCardFacesCount = cardFacesCount.toMutableMap()
    if (withJokers && mutableCardFacesCount.contains('J') && mutableCardFacesCount.size > 1) {
        val jokerCount = mutableCardFacesCount['J']!!
        mutableCardFacesCount.remove('J')
        val highestKey = mutableCardFacesCount.maxByOrNull { it.value }?.key ?: throw IllegalStateException("No other value found!")
        mutableCardFacesCount[highestKey] = mutableCardFacesCount[highestKey]!! + jokerCount
    }
    val countValues = mutableCardFacesCount.values

    return when {
        countValues.size == 1 && countValues.contains(5) -> FIVE_OF_A_KIND
        countValues.size == 2 && countValues.contains(4) -> FOUR_OF_A_KIND
        countValues.size == 2 && countValues.contains(3) && countValues.contains(2) -> FULL_HOUSE
        countValues.size == 3 && countValues.contains(3) -> THREE_OF_A_KIND
        countValues.size == 3 && countValues.contains(2) -> TWO_PAIR
        countValues.size == 4 && countValues.contains(2) -> ONE_PAIR
        else -> HIGH_CARD
    }
}

private fun determineCardFaceValue(face: Char, withJokers: Boolean = false): Int {
    return when(face) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> if (withJokers) 1 else 11
        'T' -> 10
        '9' -> 9
        '8' -> 8
        '7' -> 7
        '6' -> 6
        '5' -> 5
        '4' -> 4
        '3' -> 3
        else -> 2
    }
}

private class CamelCardsGame(val hands: List<Hand>)
private class Hand(val value: String,
                   val bid: Int,
                   val cardFacesCount: Map<Char, Int> = value.toCharArray().toSet().associateWith { cardFace -> value.count { it == cardFace } },
                   val withJokers: Boolean = false,
                   val type: HandType = determineHandType(cardFacesCount, withJokers),
                   val strength: Int = type.strength,
                   val faceValuesByIndex: Map<Int, Int> = value.toCharArray().mapIndexed { index, face ->  Pair(index, determineCardFaceValue(face, withJokers)) }.toMap()
)

private enum class HandType(val strength: Int) {
    HIGH_CARD(1), ONE_PAIR(2), TWO_PAIR(3), THREE_OF_A_KIND(4), FULL_HOUSE(5), FOUR_OF_A_KIND(6), FIVE_OF_A_KIND(7);
}