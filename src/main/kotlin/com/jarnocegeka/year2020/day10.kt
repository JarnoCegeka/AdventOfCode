package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2020Day10Part1() {
    val inputV1 = readInputFileLines("year2020/InputYear2020Day10.txt").map { it.toInt() }.toMutableList()
    inputV1.add(inputV1.maxOrNull()!! + 3)
    val input = inputV1.sorted()
    val joltDifferences = joltDifferences(input)

    val result = joltDifferences[1]!! * joltDifferences[3]!!

    println("1-jolt differences: ${joltDifferences[1]}")
    println("3-jolt differences: ${joltDifferences[3]}")
    println("Result: $result")
}

fun adventOfCodeYear2020Day10Part2() {
    val input = readInputFileLines("year2020/InputYear2020Day10.txt").map { it.toInt() }.toMutableList()
    input.add(input.maxOrNull()!! + 3)
    input.sort()

    val mustHaves = determineMustHavesJolts(input)
    mustHaves.add(0,0)
    println("Must haves: $mustHaves")

    var totalPos = 1L
    for (iteration in 0 until mustHaves.size - 1 ) {
        val current = mustHaves[iteration]
        val target = mustHaves[iteration+1]

        val relevantJolts = input.filter { it in (current + 1) .. target }

        val numPossRange = countPossibleArrangements(target, current, relevantJolts)
        totalPos *= numPossRange
        println("$current - $target - $numPossRange")
    }

    println(totalPos)
}

fun determineMustHavesJolts(jolts: List<Int>): MutableList<Int> {
    val gapBelow = jolts
        .filter { !jolts.contains(it - 1) }
        .filter { !jolts.contains(it - 2) }
        .filter { jolts.contains(it - 3) }

    val gapAbove = jolts
        .filter { !jolts.contains(it + 1) }
        .filter { !jolts.contains(it + 2) }
        .filter { jolts.contains(it + 3) }

    return gapBelow.union(gapAbove).toSortedSet().toMutableList()
}


fun countPossibleArrangements(device: Int, current: Int, voltages: List<Int>): Long {
    var total = 0L
    if (current == device) {
        total++
    }

    val allowed = voltages.filter { it <= current + 3 && it > current }.distinct()
    for (next in allowed) {
        val newList = voltages.toMutableList()
        newList.remove(next)

        total += countPossibleArrangements(device, next, newList)
    }

    return total
}

private fun joltDifferences(input: List<Int>): MutableMap<Int, Int> {
    val joltDifferences = mutableMapOf(Pair(1, 0), Pair(2, 0), Pair(3, 0))

    var currentAdapter = input[0]
    joltDifferences[currentAdapter] = joltDifferences[currentAdapter]?.plus(1)!!

    for (i in input.subList(1, input.size)) {
        val joltDifference = i - currentAdapter

        if (joltDifference == 1) {
            joltDifferences[1] = joltDifferences[1]!! + 1
            currentAdapter = i
        }

        if (joltDifference == 2) {
            joltDifferences[2] = joltDifferences[2]!! + 1
            currentAdapter = i
        }

        if (joltDifference == 3) {
            joltDifferences[3] = joltDifferences[3]!! + 1
            currentAdapter = i
        }
    }

    return joltDifferences
}