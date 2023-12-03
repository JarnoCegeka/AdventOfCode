package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2023Day03Part1() {
    val lines = readInputFileLines("InputYear2023Day03.txt")
    val lineInfoByIndex = mapToLineInfo(lines)
    val result = calculateValidPartResult(lineInfoByIndex)

    println("Result: $result")
}

fun adventOfCodeYear2023Day03Part2() {
    val lines = readInputFileLines("InputYear2023Day03.txt")
    val lineInfoByIndex = mapToLineInfo(lines)
    val result = calculateGearRatioResult(lineInfoByIndex)

    println("Result: $result")
}

private val digitRegex = Regex("\\d+")
private val symbolRegex = Regex("[^.0-9\\s]")

private fun mapToLineInfo(lines: List<String>): Map<Int, LineInfo> = lines.mapIndexed { index, line -> Pair(index, LineInfo(index, mapToDigitInfo(line), mapToSymbolInfo(line))) }.toMap()
private fun mapToDigitInfo(line: String): List<DigitInfo> {
    val matchResults = digitRegex.findAll(line)

    return matchResults.flatMap { it.groups.mapNotNull { group -> mapToDigitInfo(group!!) } }.toList()
}

private fun mapToDigitInfo(matchGroup: MatchGroup): DigitInfo {
    val digitRange = matchGroup.range
    return DigitInfo(matchGroup.value.toInt(), IntRange(digitRange.first - 1, digitRange.last + 1))
}

private fun mapToSymbolInfo(line: String): List<SymbolInfo> {
    val matchResults = symbolRegex.findAll(line)

    return matchResults.flatMap { it.groups.mapNotNull { group -> mapToSymbolInfo(group!!) } }.toList()
}

private fun mapToSymbolInfo(matchGroup: MatchGroup): SymbolInfo {
    return SymbolInfo(matchGroup.value, matchGroup.range.first)
}

private fun calculateValidPartResult(lineInfoByIndex: Map<Int, LineInfo>): Int {
    return lineInfoByIndex.values.filter { it.hasDigits() }
        .map { sumOfValidDigitOnSameLine(it, lineInfoByIndex) }
        .reduce { accumulator, digit -> accumulator + digit }
}

private fun sumOfValidDigitOnSameLine(lineInfo: LineInfo, lineInfoByIndex: Map<Int, LineInfo>): Int {
    val symbolsOnPrevLine = lineInfoByIndex[lineInfo.lineIndex - 1]?.symbolInfo ?: emptyList()
    val symbolsOnNextLine = lineInfoByIndex[lineInfo.lineIndex + 1]?.symbolInfo ?: emptyList()

    val validDigits = lineInfo.digitInfo.filter { isDigitValid(it, lineInfo.symbolInfo, symbolsOnPrevLine, symbolsOnNextLine) }

    if (validDigits.isEmpty()) return 0

    return validDigits.map { it.value }
        .reduce { accumulator, digit -> accumulator + digit }
}

private fun isDigitValid(digitInfo: DigitInfo, symbolsOnSameLine: List<SymbolInfo>, symbolsOnPrevLine: List<SymbolInfo>, symbolsOnNextLine: List<SymbolInfo>): Boolean {
    return digitInfo.isAnySymbolAdjacent(symbolsOnSameLine) || digitInfo.isAnySymbolAdjacent(symbolsOnPrevLine) || digitInfo.isAnySymbolAdjacent(symbolsOnNextLine)
}

private fun calculateGearRatioResult(lineInfoByIndex: Map<Int, LineInfo>): Int {
    return lineInfoByIndex.values.filter { it.hasGearSymbols() }
        .map { sumOfValidGearRatios(it, lineInfoByIndex) }
        .reduce { accumulator, digit -> accumulator + digit }
}

private fun sumOfValidGearRatios(lineInfo: LineInfo, lineInfoByIndex: Map<Int, LineInfo>): Int {
    val gears = lineInfo.symbolInfo.filter { it.isAGear() }
    val digitsOnSameLine = lineInfo.digitInfo
    val digitsOnPrevLine = lineInfoByIndex[lineInfo.lineIndex - 1]?.digitInfo ?: emptyList()
    val digitsOnNextLine = lineInfoByIndex[lineInfo.lineIndex + 1]?.digitInfo ?: emptyList()
    val allPossibleAdjacentDigits = concatenate(digitsOnSameLine, digitsOnPrevLine, digitsOnNextLine)

    val gearRatios = gears.map { gear -> allPossibleAdjacentDigits.filter { digit -> isDigitNextToAGear(digit, gear) } }
        .filter { it.size == 2 }
        .map { calculateGearRatio(it) }

    if (gearRatios.isEmpty()) return 0

    return gearRatios
        .reduce { accumulator, digit -> accumulator + digit }
}

private fun calculateGearRatio(digitInfo: List<DigitInfo>): Int {
    return digitInfo.map { digit -> digit.value }
        .reduce { accumulator, digit -> accumulator * digit }
}

private fun isDigitNextToAGear(digitInfo: DigitInfo, gear: SymbolInfo): Boolean {
    return gear.isDigitAdjacent(digitInfo)
}

fun <T> concatenate(vararg lists: List<T>): List<T> {
    return listOf(*lists).flatten()
}

private class LineInfo(val lineIndex: Int, val digitInfo: List<DigitInfo>, val symbolInfo: List<SymbolInfo>) {
    fun hasDigits(): Boolean = digitInfo.isNotEmpty()
    fun hasNoDigits(): Boolean = digitInfo.isEmpty()
    fun hasGearSymbols(): Boolean = symbolInfo.isNotEmpty() && symbolInfo.any { it.isAGear() }
}
private class DigitInfo(val value: Int, val validRange: IntRange) {
    fun isAnySymbolAdjacent(symbolInfo: List<SymbolInfo>): Boolean = symbolInfo.any { isSymbolAdjacent(it) }
    fun isSymbolAdjacent(symbolInfo: SymbolInfo): Boolean = validRange.contains(symbolInfo.symbolIndex)
}
private class SymbolInfo(val value: String, val symbolIndex: Int) {
    fun isAGear(): Boolean = value == "*"
    fun isAnyDigitAdjacent(digitInfo: List<DigitInfo>): Boolean = digitInfo.any { isDigitAdjacent(it) }
    fun isDigitAdjacent(digitInfo: DigitInfo): Boolean = digitInfo.validRange.contains(symbolIndex)
}