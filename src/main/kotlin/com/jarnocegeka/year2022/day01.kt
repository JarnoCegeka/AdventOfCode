package com.jarnocegeka.year2022

import com.jarnocegeka.utils.QuickSort.quickSort
import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.splitByWhiteLine

fun adventOfCodeYear2022Day01Part1() {
    val lines = readInputFileLines("year2022/InputYear2022Day01.txt")
    val elves = mapToElves(lines)

    println(elves.maxOf { it.totalAmountOfCaloriesCarrying })
}

fun adventOfCodeYear2022Day01Part2() {
    val lines = readInputFileLines("year2022/InputYear2022Day01.txt")
    val elves = mapToElves(lines)

    println(totalOfTopThree(elves))
}

private fun mapToElves(lines: List<String>): List<Elf> {
    return splitByWhiteLine(lines).map { inventory -> Elf(inventory.map { it.toInt() }) }
}

private fun totalOfTopThree(elves: List<Elf>): Int {
    val sortedElves = elves.quickSort { current, first -> current.totalAmountOfCaloriesCarrying > first.totalAmountOfCaloriesCarrying }

    return (0 until 3).map { sortedElves[it].totalAmountOfCaloriesCarrying }.reduce { accumulator, calories -> accumulator + calories }
}

private class Elf(val inventory: List<Int>, val totalAmountOfCaloriesCarrying: Int = inventory.reduce { accumulator, calories -> accumulator + calories })