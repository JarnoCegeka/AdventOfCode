package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day02Part1() {
    val lines = readInputFileLines("InputYear2021Day02.txt")

    var position = 0
    var depth = 0
    lines.map { Pair(it.substringBefore(' '), it.substringAfter(' ').toInt()) }.forEach {
        when (it.first) {
            "forward" -> position += it.second
            "up" -> depth -= it.second
            "down" -> depth += it.second
        }
    }

    println("Position: $position")
    println("Depth: $depth")
    println("Result: ${position * depth}")
}

fun adventOfCodeYear2021Day02Part2() {
    val lines = readInputFileLines("InputYear2021Day02.txt")

    var position = 0
    var depth = 0
    var aim = 0
    lines.map { Pair(it.substringBefore(' '), it.substringAfter(' ').toInt()) }.forEach {
        when (it.first) {
            "forward" -> {
                position += it.second
                depth += (aim * it.second)
            }
            "up" -> aim -= it.second
            "down" -> aim += it.second
        }
    }

    println("Position: $position")
    println("Depth: $depth")
    println("Result: ${position * depth}")
}