package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day04Part1() {
    val lines = readInputFileLines("year2021/InputYear2021Day04.txt").toMutableList()
    val drawnNumbers = lines[0].split(',').map { it.toInt() }
    val bingoBoards = createBingoBoards(lines)

    var winningBingoBoard: BingoBoard? = null
    var winningNumber = 0
    var bingoBoardHasWon = false
    var drawnNumberIndex = 0
    while (!bingoBoardHasWon) {
        val drawnNumber = drawnNumbers[drawnNumberIndex]
        var bingoBoardIndex = 0

        while (bingoBoardIndex < bingoBoards.size && !bingoBoardHasWon) {
            val bingoBoard = bingoBoards[bingoBoardIndex]
            bingoBoard.mark(drawnNumber)
            bingoBoardHasWon = bingoBoard.hasFullRowMarked()
            if (bingoBoardHasWon) {
                winningBingoBoard = bingoBoard
                winningNumber = drawnNumber
            }
            bingoBoardIndex++
        }

        drawnNumberIndex++
    }

    winningBingoBoard!!.print()
    val result = winningBingoBoard.sumOfUnmarkedNumbers() * winningNumber

    println("Result: $result")
}

fun adventOfCodeYear2021Day04Part2() {
    val lines = readInputFileLines("year2021/InputYear2021Day04.txt")
    val drawnNumbers = lines[0].split(',').map { it.toInt() }
    val bingoBoards = createBingoBoards(lines)

    var winningBingoBoard: BingoBoard? = null
    var winningNumber = 0
    var drawnNumberIndex = 0
    while (bingoBoards.isNotEmpty()) {
        val drawnNumber = drawnNumbers[drawnNumberIndex]
        var bingoBoardIndex = 0

        while (bingoBoardIndex < bingoBoards.size) {
            val bingoBoard = bingoBoards[bingoBoardIndex]
            bingoBoard.mark(drawnNumber)
            if (bingoBoard.hasFullRowMarked()) {
                winningBingoBoard = bingoBoard
                winningNumber = drawnNumber
                bingoBoards.removeAt(bingoBoardIndex)
            } else {
                bingoBoardIndex++
            }
        }

        drawnNumberIndex++
    }

    winningBingoBoard!!.print()
    val result = winningBingoBoard.sumOfUnmarkedNumbers() * winningNumber

    println("Result: $result")
}

fun createBingoBoards(lines: List<String>): MutableList<BingoBoard> {
    var lineCounter = 2
    val bingoBoards = mutableListOf<BingoBoard>()
    var bingoBoardToAdd = BingoBoard()
    while (lineCounter < lines.size) {
        val line = lines[lineCounter]
        if (line.isNotBlank() && line.isNotEmpty()) {
            val bingoNumbers = line.trim().split(Regex("\\s+")).map { it.toInt() }.map { BingoNumber(it, false) }
            bingoBoardToAdd.rows.add(Bingo(bingoNumbers))
        } else {
            bingoBoardToAdd.transposeRowsToColumns()
            bingoBoards.add(bingoBoardToAdd)
            bingoBoardToAdd = BingoBoard()
        }

        lineCounter++
    }
    bingoBoards.add(bingoBoardToAdd)

    return bingoBoards
}

data class BingoNumber(val value: Int, var marked: Boolean)
data class Bingo(val bingoNumbers: List<BingoNumber>) {
    fun mark(number: Int) {
        bingoNumbers.forEach { if (it.value == number) it.marked = true }
    }

    fun isCompletelyMarked(): Boolean {
        return bingoNumbers.all { it.marked }
    }

    fun sumOfUnmarkedNumbers(): Int {
        return bingoNumbers.filterNot { it.marked }.sumOf { it.value }
    }

    fun print(): String {
        return bingoNumbers.map { "${it.value.toString().padStart(2,  '0')} " }.joinToString("") { it }
    }
}

data class BingoBoard(
    val rows: MutableList<Bingo> = mutableListOf(),
    val columns: MutableList<Bingo> = mutableListOf()
) {
    fun mark(number: Int) {
        rows.forEach { it.mark(number) }
        columns.forEach { it.mark(number) }
    }

    fun hasFullRowMarked(): Boolean {
        return rows.any { it.isCompletelyMarked() } || columns.any { it.isCompletelyMarked() }
    }

    fun sumOfUnmarkedNumbers(): Int {
        return rows.sumOf { it.sumOfUnmarkedNumbers() }
    }

    fun transposeRowsToColumns() {
        (0 until rows.size).forEach { x ->
            val columnNumbers = mutableListOf<BingoNumber>()
            (0 until rows.size).forEach { y ->
                columnNumbers.add(rows[y].bingoNumbers[x])
            }
            columns.add(Bingo(columnNumbers))
        }
    }

    fun print() {
        rows.forEach { println(it.print()) }
    }
}