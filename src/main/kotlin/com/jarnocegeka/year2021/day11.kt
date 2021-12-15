package com.jarnocegeka.year2021

import com.jarnocegeka.model.Point
import com.jarnocegeka.utils.readInputFileLines
import java.util.stream.IntStream

fun adventOfCodeYear2021Day11Part1() {
    val octopuses = readOctopuses()
    val result = (1..100).sumOf { octopuses.step() }
    println("Result: $result")
}

fun adventOfCodeYear2021Day11Part2() {
    val octopuses = readOctopuses()
    val result =
        IntStream.iterate(0) { it + 1 }.peek { octopuses.step() }.takeWhile { !octopuses.isSynchronized() }.count() + 1
    println("Result: $result")
}

private fun readOctopuses(): OctopusMap {
    val lines = readInputFileLines("InputYear2021Day11.txt")

    return OctopusMap(lines.mapIndexed { y, line ->
        line.toList().mapIndexed { x, energyLevel -> Pair(Point(x, y), energyLevel.digitToInt()) }
    }.flatten().toMap().toMutableMap())
}

class OctopusMap(underlying: MutableMap<Point, Int>) : MutableMap<Point, Int> by underlying {
    fun step(): Int {
        val flashers = mutableSetOf<Point>()
        val octopusesToIncrease = mutableListOf<Point>()
        octopusesToIncrease.addAll(keys)

        while (octopusesToIncrease.isNotEmpty()) {
            val current = octopusesToIncrease.removeFirst()
            if (!flashers.contains(current)) {
                val value = compute(current) { _, v -> v!! + 1 }!!
                if (value > 9) {
                    flashers.add(current)
                    current.neighbours().filter { containsKey(it) && !flashers.contains(it) }
                        .forEach { octopusesToIncrease.add(it) }
                }
            }
        }
        flashers.forEach { put(it, 0) }

        return flashers.size
    }

    fun isSynchronized(): Boolean {
        return values.all { it == 0 }
    }
}