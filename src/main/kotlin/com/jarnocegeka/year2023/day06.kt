package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines
import kotlin.math.sqrt

fun adventOfCodeYear2023Day06Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day06.txt")
    val raceStats = mapToRaceStats(lines)

    println("Result: ${calculateNumberOfTimesToBeatTheRecord(raceStats)}")
}

fun adventOfCodeYear2023Day06Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day06.txt")
    val raceStats = mapToRaceStatsPart2(lines)

    println("Result: ${raceStats.calculateWaysToBeatTheRecordCount()}")
}

private fun mapToRaceStats(lines: List<String>): List<RaceStats> {
    val times = lines[0].substringAfter(":").trim().split(Regex("\\s+")).map { it.toLong() }
    val distances = lines[1].substringAfter(":").trim().split(Regex("\\s+")).map { it.toLong() }

    return times.indices.map { RaceStats(times[it], distances[it]) }
}

private fun mapToRaceStatsPart2(lines: List<String>): RaceStats {
    val time = lines[0].substringAfter(":").trim().replace(Regex("\\s+"), "").toLong()
    val distance = lines[1].substringAfter(":").trim().replace(Regex("\\s+"), "").toLong()

    return RaceStats(time, distance)
}

private fun calculateNumberOfTimesToBeatTheRecord(raceStats: List<RaceStats>): Long {
    return raceStats.map { it.calculateWaysToBeatTheRecordCount() }.reduce { accumulator, count -> accumulator * count }
}

private class RaceStats(val time: Long, val distance: Long) {
    fun calculateWaysToBeatTheRecordCount(): Long {
        val width = sqrt(time * time - 4.0 * (distance + 1)).toLong()
        return width + (time + width + 1) % 2
    }
}