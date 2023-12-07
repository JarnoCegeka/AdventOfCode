package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2023Day04Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day04.txt")
    val scratchCards = mapToScratchCards(lines)
    val result = validateScratchCards(scratchCards)

    println("Result: $result")
}

fun adventOfCodeYear2023Day04Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day04.txt")
    val scratchCards = mapToScratchCards(lines)
    val scratchCardsByCardId = scratchCards.associate { Pair(it.cardId, mutableListOf(it)) }
    val result = calculateTotalAmountOfScratchCards(scratchCardsByCardId)

    println("Result: $result")
}

private fun mapToScratchCards(lines: List<String>): List<ScratchCard> = lines.map { mapToScratchCard(it) }
private fun mapToScratchCard(line: String): ScratchCard {
    val cardId = line.substringAfter("Card").substringBefore(":").trim().toInt()

    val winningNumbers = getWinningNumbers(line)
    val chosenNumbers = getChosenNumbers(line)

    return ScratchCard(cardId, winningNumbers, chosenNumbers)
}

private fun getWinningNumbers(line: String): List<Int> {
    return line.substringAfter(":").substringBefore("|").trim().split(Regex("\\s+")).map { it.toInt() }
}

private fun getChosenNumbers(line: String): List<Int> {
    return line.substringAfter("|").trim().split(Regex("\\s+")).map { it.toInt() }
}

private fun validateScratchCards(scratchCards: List<ScratchCard>): Int {
    return scratchCards.map { it.calculateCardPoints() }.reduce { accumulator, points -> accumulator + points }
}

private fun calculateTotalAmountOfScratchCards(scratchCardsByCardId: Map<Int, MutableList<ScratchCard>>): Int {
    scratchCardsByCardId.forEach { (cardId, scratchCards) ->
        scratchCards.forEach { scratchCard ->
            val winningChosenNumbersCount = scratchCard.winningChosenNumbersCount()
            for (copiedCardId in cardId + 1..cardId + winningChosenNumbersCount) {
                val scratchCardToCopy = scratchCardsByCardId[copiedCardId]!!.first()
                scratchCardsByCardId[copiedCardId]!!.add(scratchCardToCopy.copy())
            }
        }
    }

    return scratchCardsByCardId.values.map { it.size }.reduce { accumulator, points -> accumulator + points }
}

private class ScratchCard(val cardId: Int, val winningNumbers: List<Int>, val chosenNumbers: List<Int>) {
    fun winningChosenNumbers(): List<Int> = chosenNumbers.filter { winningNumbers.contains(it) }
    fun winningChosenNumbersCount(): Int = chosenNumbers.count { winningNumbers.contains(it) }
    fun copy(): ScratchCard = ScratchCard(cardId, winningNumbers, chosenNumbers)
    fun calculateCardPoints(): Int {
        val winningChosenNumbers = winningChosenNumbers()
        if (winningChosenNumbers.isEmpty()) return 0

        return winningChosenNumbers.drop(1).fold(1) { sum, _ -> sum * 2 }
    }
}

