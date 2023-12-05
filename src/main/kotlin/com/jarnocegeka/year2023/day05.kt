package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines


fun adventOfCodeYear2023Day05Part1() {
    val lines = readInputFileLines("InputYear2023Day05.txt")
    val almanacInfo = splitToAlmanacInfo(lines)
    val seeds = getSimpleSeeds(almanacInfo[0])
    val productionSteps = mapToProductionSteps(almanacInfo)
    val lowestLocationNumber = determineLowestLocationNumber(seeds, productionSteps)

    println("Result: $lowestLocationNumber")
}

fun adventOfCodeYear2023Day05Part2() {
    val lines = readInputFileLines("InputYear2023Day05.txt")
    val almanacInfo = splitToAlmanacInfo(lines)
    val seeds = getSeeds(almanacInfo[0])
    val productionSteps = mapToProductionSteps(almanacInfo)

    var lowestDestination = productionSteps.reversed().first().sourceOfTheLowestDestination()
    var lowestLocation = productionSteps.reversed().first().determineLocationNumber(lowestDestination)
    var lowestSeedFound = false
    while (!lowestSeedFound) {
        val startingPointForLowestLocationInMappings = startingPointOfLowest(productionSteps.reversed(), lowestDestination)
        lowestLocation = determineLocation(startingPointForLowestLocationInMappings, productionSteps)
        lowestSeedFound = seeds.any { it.contains(startingPointForLowestLocationInMappings) }
        lowestDestination + 1
    }

//    println()
//    val pathToLowestSeed = determineLocation(lowestSeed, productionSteps)

    println("Result: $lowestLocation")
}

private fun startingPointOfLowest(productionSteps: List<ProductionStep>, lowestDestination: Long): Long {
    if (productionSteps.size == 1) {
        return productionSteps.first().determineSource(lowestDestination)
    }

    val productionStepsSubList = productionSteps.subList(1, productionSteps.size)
    val newLowestDestination = productionSteps.first().determineSource(lowestDestination)

    return startingPointOfLowest(productionStepsSubList, newLowestDestination)
}

private val whiteSpaceRegex = Regex("\\s+")

private fun splitToAlmanacInfo(lines: List<String>): List<List<String>> {
    val almanac = mutableListOf<List<String>>()
    var startIndex = 0

    lines.forEachIndexed { index, line ->
        if (line.isEmpty()) {
            almanac.add(lines.subList(startIndex, index))
            startIndex = index + 1
        } else if (index == lines.size - 1) {
            almanac.add(lines.subList(startIndex, lines.size))
        }
    }

    return almanac
}

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

private class SeedRange(val startSeed: Long, val seedCount: Long = 0) {
    fun contains(seed: Long): Boolean = seed >= startSeed && seed <= (startSeed + seedCount) - 1
}

private class ProductionStep(val id: String, val productionMappings: List<ProductionMapping>) {
    fun determineLocationNumber(seed: Long): Long {
        return productionMappings.firstOrNull { it.isWithinSourceRange(seed) }?.calculateDestination(seed) ?: seed
    }

    fun determineSource(destination: Long): Long {
        return productionMappings.firstOrNull { it.isDestinationSourceWithinSourceRange(destination) }?.calculateSource(destination) ?: destination
    }

    fun sourceOfTheLowestDestination(): Long {
        val lowest = productionMappings.minByOrNull { it.minDestination }?.sourceRange?.first ?: -69
        return lowest
    }
}

private class ProductionMapping(val sourceRange: LongRange, val offset: Long, val minDestination: Long = sourceRange.first + offset) {
    fun isWithinSourceRange(source: Long): Boolean = sourceRange.contains(source)
    fun isDestinationSourceWithinSourceRange(destination: Long): Boolean = sourceRange.contains(destination - offset)
    fun calculateDestination(source: Long): Long {
        return source + offset
    }
    fun calculateSource(destination: Long): Long {
        return destination - offset
    }
}