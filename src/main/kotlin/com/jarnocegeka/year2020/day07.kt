package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2020Day07Part1() {
    val fileLines = readInputFileLines("InputYear2020Day07.txt")
    val bagColors = mapBagColors(fileLines)

    val shinyGold = "shiny gold"
    val colorsThatContains = bagColors.filter { it.value.keys.contains(shinyGold) }.map { it.key }.toMutableList()
    val colorsSearched = mutableSetOf<String>()
    colorsSearched.addAll(colorsThatContains)
    val keysThatContainShinyGold = mutableSetOf<String>()
    keysThatContainShinyGold.addAll(colorsThatContains)

    while (colorsThatContains.isNotEmpty()) {
        val first = colorsThatContains.first()
        colorsThatContains.removeAt(0)

        val colorsToSearch = bagColors.filter { it.value.keys.contains(first) && !colorsSearched.contains(it.key) }.map { it.key }.toMutableList()
        keysThatContainShinyGold.addAll(colorsToSearch)
        colorsThatContains.addAll(colorsToSearch)
    }

    println(keysThatContainShinyGold.size)
}

fun adventOfCodeYear2020Day07Part2() {
    val fileLines = readInputFileLines("InputYear2020Day07.txt")
    val bagColors = mapBagColors(fileLines)
    val shinyGold = "shiny gold"

    val start = bagColors[shinyGold]!!

    bagsMethod(start, bagColors)

    var fullTotal = 0
    bagCounts.forEach { (c, i) ->
        i.forEach { fullTotal += it }
    }

    println(fullTotal)
}

var bagCounts = mutableMapOf<String, MutableList<Int>>()
private fun bagsMethod(mapToSearch: MutableMap<String, Int>, bagColors: MutableMap<String, MutableMap<String, Int>>, count: Int = 1): Int {
    if (mapToSearch.isEmpty()) return count

    var fullColorCount = 0
    mapToSearch.forEach { (c, colorCount) ->
        val map = bagColors[c]!!

        fullColorCount = count * colorCount

        if (map.isNotEmpty()) {
            val recursive = bagsMethod(map, bagColors, fullColorCount)
            bagCounts.computeIfAbsent(c) { mutableListOf() }.add(fullColorCount)
            fullColorCount += recursive
        } else {
            bagCounts.computeIfAbsent(c) { mutableListOf() }.add(fullColorCount)
        }
    }

    return fullColorCount
}

private fun mapBagColors(lines: List<String>): MutableMap<String, MutableMap<String, Int>> {
    val bagsMap = mutableMapOf<String, MutableMap<String, Int>>()
    lines.forEach {
        val keyColorIndex = it.indexOf(" bags")
        val keyColor = it.substring(0, keyColorIndex)
        val restOfString = it.substringAfter(" bags contain")
        val bagDetails = restOfString.split(",")

        val bagMap = mutableMapOf<String, Int>()
        bagDetails.forEach { bag ->
            val trimmedBag = bag.trim()

            if (trimmedBag != "no other bags.") {
                val bagCount = trimmedBag.substringBefore(" ").toInt()
                val bagColor = trimmedBag.substringAfter(" ").substringBefore(" bag")

                bagMap[bagColor] = bagCount
            }
        }
        bagsMap[keyColor] = bagMap
    }

    return bagsMap
}