package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.splitByWhiteLine

fun adventOfCodeYear2023Day05Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day05.txt")
    val almanacInfo = splitByWhiteLine(lines)
    val seeds = getSimpleSeeds(almanacInfo[0])
    val productionSteps = mapToProductionSteps(almanacInfo)
    val lowestLocationNumber = determineLowestLocationNumber(seeds, productionSteps)

    println("Result: $lowestLocationNumber")
}

fun adventOfCodeYear2023Day05Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day05.txt")
    val almanacInfo = splitByWhiteLine(lines)
    val seeds = getSeeds(almanacInfo[0])
    val productionSteps = mapToProductionSteps(almanacInfo)
    val globalMin = findGlobalMin(seeds, productionSteps)

    println("Result: $globalMin")
}

private val whiteSpaceRegex = Regex("\\s+")

private fun getSimpleSeeds(lines: List<String>): List<Long> {
    return lines[0].substringAfter(":").trim().split(whiteSpaceRegex).map { it.toLong() }
}

private fun getSeeds(lines: List<String>): List<SeedRange> {
    val seedInfo = lines[0].substringAfter(":").trim().split(whiteSpaceRegex).map { it.toLong() }

    return (seedInfo.indices step 2).map { SeedRange(seedInfo[it], seedInfo[it + 1]) }
}

private fun mapToProductionSteps(almanacInfo: List<List<String>>): List<ProductionStep> {
    return (1 until almanacInfo.size).map { mapToProductionStep(almanacInfo[it]) }
}

private fun mapToProductionStep(stepsToMap: List<String>): ProductionStep {
    val id = stepsToMap[0].substringBefore("map:").trim()
    val productionMappings = (1 until stepsToMap.size).map { mapToProductionMapping(stepsToMap[it]) }

    return ProductionStep(id, productionMappings)
}

private fun mapToProductionMapping(line: String): ProductionMapping {
    val lineInfo = line.split(whiteSpaceRegex).map { it.toLong() }

    val offset = lineInfo[0] - lineInfo[1]
    val sourceRange = LongRange(lineInfo[1], (lineInfo[1] + lineInfo[2]) - 1L)

    return ProductionMapping(sourceRange, offset)
}

private fun determineLowestLocationNumber(seeds: List<Long>, productionSteps: List<ProductionStep>): Long {
    return seeds.minOf { seed -> determineLocation(seed, productionSteps) }
}

private fun determineLocation(seed: Long, productionSteps: List<ProductionStep>): Long {
    var location = seed
    productionSteps.forEach {
        location = it.determineLocationNumber(location)
    }

    return location
}

private fun findGlobalMin(seedRanges: List<SeedRange>, productionSteps: List<ProductionStep>): Long {
    val globalMin = Long.MAX_VALUE
    return seedRanges.minOf { minOf(globalMin, findMin(it.range.first, it.range.last - it.range.first, productionSteps)) }
}

private fun findMin(start: Long, length: Long, productionSteps: List<ProductionStep>): Long {
    if (length == 1L) return minOf(determineLocation(start, productionSteps), determineLocation(start + 1, productionSteps))

    val stepSize = length / 2
    val middle = start + stepSize

    val startLocation = determineLocation(start, productionSteps)
    val middleLocation = determineLocation(middle, productionSteps)
    val endLocation = determineLocation(start + length, productionSteps)

    var foundMin = Long.MAX_VALUE
    if (startLocation + stepSize != middleLocation) foundMin = findMin(start, stepSize, productionSteps)
    if (middleLocation + (length - stepSize) != endLocation) foundMin = minOf(foundMin, findMin(middle, (length - stepSize), productionSteps))

    return foundMin
}

private class SeedRange(startSeed: Long, seedCount: Long = 0, val range: LongRange = LongRange(startSeed, startSeed + seedCount - 1)) {
    fun contains(seed: Long): Boolean = range.contains(seed)
}

private class ProductionStep(val id: String, val productionMappings: List<ProductionMapping>) {
    fun determineLocationNumber(seed: Long): Long {
        return productionMappings.firstOrNull { it.isWithinSourceRange(seed) }?.calculateDestination(seed) ?: seed
    }
}

private class ProductionMapping(val sourceRange: LongRange, val offset: Long) {
    fun isWithinSourceRange(source: Long): Boolean = sourceRange.contains(source)
    fun calculateDestination(source: Long): Long = source + offset
}