package com.jarnocegeka.year2023

import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToIntWithSum

fun adventOfCodeYear2023Day15Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day15.txt")
    println(lines.first().split(",").map { hash(it) }.reduceToIntWithSum())
}

fun adventOfCodeYear2023Day15Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day15.txt")
    println(lines.first().split(',').fold(mutableMapOf<Int, Map<String, Int>>()) { acc, entry ->
        when {
            '=' in entry -> {
                val (label, focalLength) = entry.split('=')
                acc.merge(hash(label), mapOf(label to focalLength.toInt()), Map<String, Int>::plus)
            }

            '-' in entry -> {
                val label = entry.substringBefore('-')
                acc.computeIfPresent(hash(label)) { _, value -> value.filterKeys { it != label } }
            }
        }
        acc
    }.toList().sumOf { (boxNumber, box) ->
        box.toList().withIndex().sumOf { (index, entry) ->
            (boxNumber + 1) * (index + 1) * entry.second
        }
    })
}

private fun hash(line: String): Int = line.fold(0) { acc, cur -> ((acc + cur.code) * 17) % 256 }