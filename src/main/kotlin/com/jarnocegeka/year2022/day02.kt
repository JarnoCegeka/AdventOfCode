package com.jarnocegeka.year2022

import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToIntWithSum
import com.jarnocegeka.utils.whiteSpaceRegex
import com.jarnocegeka.year2022.RockPaperScissors.*

fun adventOfCodeYear2022Day02Part1() {
    val lines = readInputFileLines("year2022/InputYear2022Day02.txt")
    val rockPaperScissorsGames = mapToRockPaperScissorsGames(lines)
    val totalPlayerScore = rockPaperScissorsGames.map { it.playerScore() }.reduceToIntWithSum()

    println(totalPlayerScore)
}

fun adventOfCodeYear2022Day02Part2() {
    val lines = readInputFileLines("year2022/InputYear2022Day02.txt")
    val rockPaperScissorsGames = mapToRockPaperScissorsGamesForPart2(lines)
    val totalPlayerScore = rockPaperScissorsGames.map { it.playerScore() }.reduceToIntWithSum()

    println(totalPlayerScore)
}

private fun mapToRockPaperScissorsGames(lines: List<String>): List<RockPaperScissorsGame> {
    val opponentMapping = mapOf(Pair("A", ROCK), Pair("B", PAPER), Pair("C", SCISSORS))
    val playerMapping = mapOf(Pair("X", ROCK), Pair("Y", PAPER), Pair("Z", SCISSORS))

    return lines.map { it.split(whiteSpaceRegex) }
            .map { RockPaperScissorsGame(playerMapping[it[1]] ?: ROCK, opponentMapping[it[0]] ?: ROCK) }
}

private fun mapToRockPaperScissorsGamesForPart2(lines: List<String>): List<RockPaperScissorsGame> {
    return lines.map { it.split(whiteSpaceRegex) }
            .map { mapToRockPaperScissorsGame(it[0], it[1]) }
}

private fun mapToRockPaperScissorsGame(opponentHand: String, gameResult: String): RockPaperScissorsGame {
    val opponentMapping = mapOf(Pair("A", ROCK), Pair("B", PAPER), Pair("C", SCISSORS))
    val playedOpponentHand = opponentMapping[opponentHand]!!

    return RockPaperScissorsGame(mapPlayerHand(gameResult, playedOpponentHand), playedOpponentHand)
}

private fun mapPlayerHand(gameResult: String, opponentHand: RockPaperScissors): RockPaperScissors {
    return when(gameResult) {
        "X" -> opponentHand.beats()
        "Y" -> opponentHand
        else -> opponentHand.losesTo()
    }
}

private class RockPaperScissorsGame(val player: RockPaperScissors,
                                    val opponent: RockPaperScissors,
                                    val playerWin: Boolean = player.doesItBeat(opponent)) {
    fun playerScore(): Int {
        val playerShapeScore = player.score
        val opponentShapeScore = opponent.score

        return when {
            playerWin -> playerShapeScore + 6
            playerShapeScore == opponentShapeScore -> playerShapeScore + 3
            else -> playerShapeScore
        }
    }
}

private enum class RockPaperScissors(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun doesItBeat(other: RockPaperScissors): Boolean {
        return when (this) {
            ROCK -> other == SCISSORS
            PAPER -> other == ROCK
            SCISSORS -> other == PAPER
        }
    }

    fun beats(): RockPaperScissors {
        return when (this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }
    }

    fun losesTo(): RockPaperScissors {
        return when (this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }
    }
}