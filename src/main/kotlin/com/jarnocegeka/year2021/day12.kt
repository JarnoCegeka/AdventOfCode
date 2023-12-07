package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day12Part1() {
    val caveNeighbours = caveNeighbours()
    val queue = mutableListOf(mutableListOf("start"))
    var paths = 0

    while (queue.isNotEmpty()) {
        val path = queue.removeFirst()

        caveNeighbours[path.last()]?.forEach { nextCave ->
            if (nextCave == "end")
                paths += 1
            else if (nextCave.none { it.isLowerCase() } || !path.contains(nextCave)) {
                val nextPath = path.toMutableList()
                nextPath.add(nextCave)
                queue.add(nextPath)
            }
        }
    }

    println("Result: $paths")
}

fun adventOfCodeYear2021Day12Part2() {
    val caveNeighbours = caveNeighbours()
    val queue = mutableListOf(mutableListOf("start"))
    var paths = 0

    while (queue.isNotEmpty()) {
        val path = queue.removeFirst()

        caveNeighbours[path.last()]?.forEach { nextCave ->
            val isRepeat = nextCave.all { it.isLowerCase() } && path.contains(nextCave)

            if (nextCave == "end")
                paths += 1
            else if (nextCave != "start" && !(path[0] == "*" && isRepeat)) {
                val nextPath = if (isRepeat) mutableListOf("*") else mutableListOf()
                nextPath.addAll(path)
                nextPath.add(nextCave)
                queue.add(nextPath)
            }
        }
    }

    println("Result: $paths")
}

fun caveNeighbours(): Map<String, Set<String>> {
    val caveNeighbours = mutableMapOf<String, MutableSet<String>>()
    readInputFileLines("year2021/InputYear2021Day12.txt").forEach { line ->
            val caves = line.split('-')
            val a = caves[0]
            val b = caves[1]
            caveNeighbours.computeIfAbsent(a) { mutableSetOf() }.add(b)
            caveNeighbours.computeIfAbsent(b) { mutableSetOf() }.add(a)
        }

    return caveNeighbours
}