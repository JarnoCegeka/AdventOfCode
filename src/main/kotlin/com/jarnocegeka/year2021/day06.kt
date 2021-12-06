package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day06Part1() {
    val fishTimers = readInputFileLines("InputYear2021Day06.txt")[0].split(',').map { it.toInt() }

    var newFishTimers = fishTimers
    repeat(80) {
        newFishTimers = fishTimerUpdate(newFishTimers)
    }

    val fishCount = newFishTimers.size

    println("FishCount: $fishCount")
}

fun adventOfCodeYear2021Day06Part2() {
    val fishTimers = readInputFileLines("InputYear2021Day06.txt")[0].split(',').map { it.toLong() }
    val fishTimerCounters = (0L..8L).associateWith { 0L }.toMutableMap()
    fishTimers.forEach { fishTimerCounters[it] = fishTimerCounters[it]!! + 1L }

    repeat(256) { _ ->
        val zeroTimer = fishTimerCounters[0]!!
        (1L..8L).forEach { fishTimerCounters[it - 1L] = fishTimerCounters[it]!! }
        fishTimerCounters[6L] = fishTimerCounters[6L]!! + zeroTimer
        fishTimerCounters[8L] = zeroTimer
    }

    val fishCount = fishTimerCounters.values.sum()

    println("FishCount: $fishCount")
}

private fun fishTimerUpdate(fishTimersToUpdate: List<Int>): List<Int> {
    val newFishTimers = mutableListOf<Int>()

    var fishTimersToAddCounter = 0
    fishTimersToUpdate.forEach { fishTimer ->
        when (fishTimer) {
            0 -> { newFishTimers.add(6); fishTimersToAddCounter++; }
            else -> newFishTimers.add(fishTimer-1)
        }
    }

    repeat(fishTimersToAddCounter) {
        newFishTimers.add(8)
    }

    return newFishTimers
}
