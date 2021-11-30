package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFile
import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2020Day16Part1() {
    val ticketInput = ticketInput()

    val allRanges = ticketInput.ticketFields.values.flatMap { it.toList() }
    val invalidNumbers = mutableListOf<Int>()

    ticketInput.nearbyTicketNumbers.forEach {
        it.forEach { num ->
            if(allRanges.none { range -> isInRange(num, range) }) {
                invalidNumbers.add(num)
            }
        }
    }

    val result = invalidNumbers.sum()
    println("Sum of invalid numbers is $result")
}

private fun isInRange(num: Int, range: Pair<Int, Int>): Boolean {
    return num >= range.first && num <= range.second
}

private fun ticketInput(): TicketInput {
    val input = readInputFileLines("InputYear2020Day16.txt")

    val indexOfYourTicket = input.indexOf("your ticket:")
    val indexOfNearbyTickets = input.indexOf("nearby tickets:")

    val fieldLines = input.subList(0, indexOfYourTicket - 1)
    val yourTicketNumbersString = input.subList(indexOfYourTicket+1, indexOfYourTicket+2)[0]
    val nearbyTicketsNumbersString = input.subList(indexOfNearbyTickets+1, input.size)

    val ticketFields = fieldLines.map {
        val field = it.substringBefore(":")
        val range1 = it.substringAfter(": ").substringBefore(" or").split("-").map { txt -> txt.toInt() }
        val range1Pair = Pair(range1[0], range1[1])
        val range2 = it.substringAfter(" or ").split("-").map { txt -> txt.toInt() }
        val range2Pair = Pair(range2[0], range2[1])
        Pair(field, listOf(range1Pair, range2Pair))
    }.toMap()

    val yourTicketNumbers = yourTicketNumbersString.split(",").map { it.toInt() }.toList()

    val nearbyTicketNumbers = nearbyTicketsNumbersString.map {
        it.split(",").map { txt -> txt.toInt() }.toList()
    }.toList()

    return TicketInput(ticketFields, yourTicketNumbers, nearbyTicketNumbers)
}

private data class TicketInput(val ticketFields: Map<String, List<Pair<Int, Int>>>, val yourTicketNumbers: List<Int>, val nearbyTicketNumbers: List<List<Int>>)

fun adventOfCodeYear2020Day16Part2() {
    println(executePart2())
}

private fun executePart2(): Long {
    val input = readInputFile("InputYear2020Day16.txt")
    var state = 0
    val rules = mutableSetOf<Rule>()
    val lines = input.readLines()
    var myTicket: List<Int> = emptyList()
    val validTickets = mutableListOf<List<Int>>()
    lines.forEach { line ->
        when {
            line == "your ticket:" -> state = 1
            line == "nearby tickets:" -> state = 2
            line.isNotEmpty() -> {
                when (state) {
                    0 -> rules += Rule.parse(line)
                    1 -> myTicket = line.split(",").map(String::toInt)
                    2 -> {
                        val theirs = line.split(",").map(String::toInt)
                        if (!rules.check(theirs)) return@forEach
                        validTickets.add(theirs)
                    }
                }
            }
        }
    }

    // Start with all rules as possibilities for all fields.
    val rulePossibilities = ArrayList<MutableSet<Rule>>(myTicket.size)
    repeat(myTicket.size) { rulePossibilities.add(rules.toMutableSet()) }

    // Keep going until we've found unique rules for each field.
    val doneFields = mutableSetOf<Int>()
    while (doneFields.size != myTicket.size) {
        // Iterate over the fields.
        myTicket.indices.forEach { field ->
            if (field in doneFields) return@forEach

            // For each candidate rule, if all tickets current field work for that rule, we can keep
            // it. Otherwise, we need to drop it.
            val candidates = rulePossibilities[field]
            val toKeep = mutableSetOf<Rule>()
            candidates.forEach { rule ->
                val allOkay = validTickets.all { ticket -> rule.check(ticket[field]) }
                if (allOkay) toKeep.add(rule)
            }
            rulePossibilities[field] = toKeep

            // If we found our rule for this field, we can remove it as an option from all of the
            // other fields and mark it as done so we can skip it next time around the while loop.
            if (toKeep.size == 1) {
                val ruleAtField = toKeep.first()
                rulePossibilities.forEachIndexed { index, remaining ->
                    if (index == field) return@forEachIndexed
                    remaining.remove(ruleAtField)
                }
                doneFields.add(field)
            }
        }
    }

    return rulePossibilities.map(MutableSet<Rule>::first).withIndex()
        .filter { (_, rule) -> rule.name.startsWith("departure") }
        .product { (i, _) -> myTicket[i].toLong() }
}

data class Rule(val name: String, val rangeOne: IntRange, val rangeTwo: IntRange) {
    fun check(value: Int): Boolean = value in rangeOne || value in rangeTwo

    companion object {
        private val RULE_PATTERN = Regex("([A-Za-z ]+): (([0-9]+)-([0-9]+)) or (([0-9]+)-([0-9]+))")

        fun parse(line: String): Rule {
            val match = requireNotNull(RULE_PATTERN.matchEntire(line)) { line }.groupValues

            return Rule(
                name = match[1],
                rangeOne = match[3].toInt()..match[4].toInt(),
                rangeTwo = match[6].toInt()..match[7].toInt()
            )
        }
    }
}

fun Collection<Rule>.check(ticket: List<Int>): Boolean =
    ticket.none { field -> all { rule -> !rule.check(field) } }

fun Iterable<Long>.product(): Long = fold(1L) { acc, value -> value * acc }
fun <T> Iterable<T>.product(block: (T) -> Long): Long =
    fold(1L) { acc, value -> block(value) * acc }