package com.jarnocegeka.year2022

import com.jarnocegeka.utils.findFirstCommonItem
import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToIntWithSum

fun adventOfCodeYear2022Day03Part1() {
    val lines = readInputFileLines("year2022/InputYear2022Day03.txt")
    val rucksacks = mapToRucksacks(lines)

    println(rucksacks.map { it.calculateValueOfCommonItem() }.reduceToIntWithSum())
}

fun adventOfCodeYear2022Day03Part2() {
    val lines = readInputFileLines("year2022/InputYear2022Day03.txt")
    val elfGroups = mapToElfGroups(lines)

    println(elfGroups.map { it.calculateValueOfCommonItem() }.reduceToIntWithSum())
}

private fun mapToRucksacks(lines: List<String>): List<Rucksack> {
    return lines.map { Rucksack(it, it.substring(0, it.length / 2), it.substring(it.length / 2, it.length)) }
}

private fun mapToRucksack(line: String): Rucksack {
    return  Rucksack(line, line.substring(0, line.length / 2), line.substring(line.length / 2, line.length))
}

private fun mapToElfGroups(lines: List<String>): List<ElfGroup> {
    return (lines.indices step 3).map {
        val rucksack1 = mapToRucksack(lines[it])
        val rucksack2 = mapToRucksack(lines[it + 1])
        val rucksack3 = mapToRucksack(lines[it + 2])
        ElfGroup(rucksack1, rucksack2, rucksack3)
    }
}

private const val LOWER_CASE_A_CODE = 'a'.code
private const val UPPER_CASE_A_CODE = 'A'.code

private fun calculateValueOfItem(item: Char): Int {
    return if (item.isUpperCase()) (item.code - UPPER_CASE_A_CODE) + 27 else (item.code - LOWER_CASE_A_CODE) + 1
}

private class ElfGroup(val rucksack1: Rucksack, val rucksack2: Rucksack, val rucksack3: Rucksack) {

    fun calculateValueOfCommonItem(): Int {
        return calculateValueOfItem(commonItem())
    }

    private fun commonItem(): Char {
        val a = rucksack1.value.toCharArray().toSortedSet().toList()
        val b = rucksack2.value.toCharArray().toSortedSet().toList()
        val c = rucksack3.value.toCharArray().toSortedSet().toList()

        return a.findFirstCommonItem(b, c)
    }
}

private class Rucksack(val value: String, val compartment1: String, val compartment2: String) {

    fun calculateValueOfCommonItem(): Int {
        return calculateValueOfItem(commonItem())
    }

    private fun commonItem(): Char {
        return compartment1.toCharArray().toSortedSet().findFirstCommonItem(compartment2.toCharArray().toSortedSet())
    }
}
