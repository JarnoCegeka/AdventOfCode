package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFile
import kotlin.math.abs

private val input = readInputFile("InputYear2020Day12.txt").readLines().map { Pair(it.take(1), it.drop(1).toInt()) }
fun adventOfCodeYear2020Day12Part1() {
    println(part1(input))
}

fun adventOfCodeYear2020Day12Part2() {
    println(part2(input))
}

fun part1(commands: List<Pair<String, Int>>): Int {
    var x = 0
    var y = 0
    var dir = 0

    for ((command, units) in commands) {
        when (command) {
            "N" -> y += units
            "S" -> y -= units
            "E" -> x += units
            "W" -> x -= units
            "L" -> {
                dir += units
                if (dir >= 360) dir -= 360
            }
            "R" -> {
                dir -= units
                if (dir < 0) dir += 360
            }
            "F" -> when (dir) {
                0 -> x += units
                90 -> y += units
                180 -> x -= units
                270 -> y -= units
            }
        }
    }
    return abs(x) + abs(y)
}

fun part2(commands: List<Pair<String, Int>>): Int {
    var wx = 10
    var wy = 1
    var x = 0
    var y = 0

    for ((command, units) in commands) {
        when (command) {
            "N" -> wy += units
            "S" -> wy -= units
            "E" -> wx += units
            "W" -> wx -= units
            "L" -> {
                repeat(units / 90) {
                    wy *= -1
                    val tmp = wx
                    wx = wy
                    wy = tmp
                }
            }
            "R" -> {
                repeat(units / 90) {
                    wx *= -1
                    val tmp = wx
                    wx = wy
                    wy = tmp
                }
            }
            "F" -> {
                x += wx * units
                y += wy * units
            }
        }
    }

    return abs(x) + abs(y)
}