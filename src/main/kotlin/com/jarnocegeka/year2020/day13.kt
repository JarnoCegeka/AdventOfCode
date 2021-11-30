package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFile

private val input = readInputFile("InputYear2020Day13.txt").readLines()
fun adventOfCodeYear2020Day13Part1() {
    calculateEarliestBus()
}

private fun calculateEarliestBus() {
    val earliestTime = input[0].toInt()
    val busses = input[1].split(',').filter { it[0].toLowerCase() != 'x' }.map { it.toInt() }.sorted()

    var earliestBus = Pair(0, 0)
    val earliestBusFound = false
    for (bus in busses) {
        for (time in earliestTime..(earliestTime + busses[0])) {
            if (time % bus == 0) {
                earliestBus = Pair(bus, time)
                break
            }
        }
        if (earliestBusFound) break
    }

    println("Bus ${earliestBus.first} leaves at ${earliestBus.second}")
    val minutesToWait = earliestBus.second - earliestTime
    val result = earliestBus.first * minutesToWait
    println("Result: $result")
}

fun adventOfCodeYear2020Day13Part2() {
    calculateSubsequentBusses()
}

private fun calculateSubsequentBusses() {
    val busLines = mutableListOf<Pair<Int, Long>>()
    input[1].split(",").forEachIndexed { index, it -> if (it != "x") busLines.add(Pair(index, it.toLong())) }
    val productOfAllLineNumbers = busLines.fold(1L, { acc, int -> acc * int.second })

    var timeStamp = busLines.first().second
    for (i in 1 until busLines.size) {
        //  The fold function on the next line of code essentially takes the product of the numbers we already found the timestamp for,
        //  so to find the timestamp where t % 17 == 0, (t+7) % 41 == 0, and (t+17) % 523 == 0, we can iterate from the timestamp of t % 17 == 0, (t+7) % 41 == 0 with steps of (7*41)
        timeStamp = (timeStamp..productOfAllLineNumbers step (busLines.take(i).fold(1L, { acc, int -> acc * int.second })))
            .first { (it + busLines[i].first) % busLines[i].second == 0L }
    }
    println(timeStamp)
}

