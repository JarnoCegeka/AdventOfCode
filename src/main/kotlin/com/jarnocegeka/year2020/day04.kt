package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines
import java.lang.Exception
import java.util.*
import java.util.function.Predicate
import kotlin.text.Regex.Companion.fromLiteral

val requiredFieldsPart1 = listOf("byr", "ecl", "eyr", "hcl", "hgt", "iyr", "pid")
fun adventOfCodeYear2020Day04Part1() {
    val fileLines = readInputFileLines("year2020/InputYear2020Day04.txt")
    val passwords = generatePasswords(fileLines)

    val validPasswords = passwords.filter { hasAllRequiredFields(it) }.count()
    println(validPasswords)
}

fun adventOfCodeYear2020Day04Part2() {
    val fileLines = readInputFileLines("year2020/InputYear2020Day04.txt")
    val passwords = generatePasswords(fileLines)

    val validPasswords = passwords.filter { hasAllRequiredFields(it) }.filter { allPairsValid(it) }.count()
    println(validPasswords)
}

private fun generatePasswords(lines: List<String>): MutableList<SortedMap<String, String>> {
    val passwords = mutableListOf<SortedMap<String, String>>()
    var passwordMap = sortedMapOf<String, String>()
    lines.forEachIndexed { index, it ->
        if (it.isBlank()) {
            passwords.add(passwordMap)
            passwordMap = sortedMapOf()
        } else {
            val fields = it.split(fromLiteral(" "))

            fields.forEach { field ->
                val keyValue = field.split(":")
                passwordMap[keyValue[0]] = keyValue[1]
            }

            if (index == lines.lastIndex) passwords.add(passwordMap)
        }
    }

    return passwords
}

private fun hasAllRequiredFields(pairs: Map<String, String>): Boolean {
    return pairs.keys.containsAll(requiredFieldsPart1)
}

private fun allPairsValid(pairs: Map<String, String>): Boolean {
    return pairs.entries.all {
        try {
            validators.getOrDefault(it.key, Predicate { true }).test(it.value)
        } catch (e: Exception) {
            false
        }
    }
}

private val validators = mapOf<String, Predicate<String>>(
    Pair("byr", Predicate { v ->
        val r = v.toInt()
        r in 1920..2002
    }),
    Pair("ecl", Predicate { v ->
        listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(v)
    }),
    Pair("eyr", Predicate { v ->
        val r = v.toInt()
        r in 2020..2030
    }),
    Pair("hcl", Predicate { v ->
        v.matches(Regex("#[0-9a-f]{6}"))
    }),
    Pair("hgt", Predicate { v ->
        val r = v.substring(0, v.length - 2).toInt()
        when (v.substring(v.length - 2)) {
            "cm" -> r in 150..193
            "in" -> r in 59..76
            else -> false
        }
    }),
    Pair("iyr", Predicate { v ->
        val r = v.toInt()
        r in 2010..2020
    }),
    Pair("pid", Predicate { v ->
        v.length == 9 && v.matches(Regex("[0-9]+"))
    })
)
