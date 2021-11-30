package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2020Day17Part1() {
    val lines = readInputFileLines("InputYear2020Day17.txt").map { it.toList() }
    val grid = mutableSetOf<Triple<Int, Int, Int>>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            when (lines[x][y]) {
                '#' -> {
                    grid.add(Triple(x, y, 0))
                }
            }
        }
    }

    val cycles = 6
    repeat(cycles) {
        part1(grid)
    }

    println(grid.size)
}

fun adventOfCodeYear2020Day17Part2() {
    val lines = readInputFileLines("InputYear2020Day17.txt").map { it.toList() }
    val grid = mutableSetOf<Quad<Int, Int, Int, Int>>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            when (lines[x][y]) {
                '#' -> {
                    grid.add(Quad(x, y, 0, 0))
                }
            }
        }
    }

    val cycles = 6
    repeat(cycles) {
        part2(grid)
    }

    println(grid.size)
}

fun part1(grid: MutableSet<Triple<Int, Int, Int>>) {
    val lastGrid = grid.toSet()

    val toEvaluate = mutableSetOf<Triple<Int, Int, Int>>()
    grid.forEach {
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    toEvaluate.add(Triple(it.first + i, it.second + j, it.third + k))
                }
            }
        }
    }
    grid.clear()

    toEvaluate.forEach {
        var active = 0
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    if (i == 0 && j == 0 && k == 0) {
                        continue
                    }
                    if (lastGrid.contains(Triple(it.first + i, it.second + j, it.third + k))) {
                        active++
                    }
                }
            }
        }

        when {
            lastGrid.contains(it) -> when (active) {
                2, 3 -> grid.add(it)
            }
            else -> when (active) {
                3 -> grid.add(it)
            }
        }
    }
}

private fun part2(grid: MutableSet<Quad<Int, Int, Int, Int>>) {
    val lastGrid = grid.toSet()

    val toEvaluate = mutableSetOf<Quad<Int, Int, Int, Int>>()
    grid.forEach {
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    for (l in -1..1) {
                        toEvaluate.add(Quad(it.x + i, it.y + j, it.z + k, it.w + l))
                    }
                }
            }
        }
    }
    grid.clear()

    toEvaluate.forEach {
        var active = 0
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    for (l in -1..1) {
                        if (i == 0 && j == 0 && k == 0 && l == 0) {
                            continue
                        }
                        if (lastGrid.contains(Quad(it.x + i, it.y + j, it.z + k, it.w + l))) {
                            active++
                        }
                    }
                }
            }
        }

        when {
            lastGrid.contains(it) -> when (active) {
                2, 3 -> grid.add(it)
            }
            else -> when (active) {
                3 -> grid.add(it)
            }
        }
    }
}

data class Quad<X, Y, Z, W>(val x: X, val y: Y, val z: Z, val w: W)