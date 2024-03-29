package com.jarnocegeka.year2021

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day16Part1() {
    val binaryString =
        readInputFileLines("year2021/InputYear2021Day16.txt")[0].map { it.digitToInt(16).toString(2).padStart(4, '0') }
            .joinToString("")
    println(parse(binaryString).sumOfVersions())
}

fun adventOfCodeYear2021Day16Part2() {
    val binaryString =
        readInputFileLines("year2021/InputYear2021Day16.txt")[0].map { it.digitToInt(16).toString(2).padStart(4, '0') }
            .joinToString("")
    println(parse(binaryString).value())
}

@OptIn(ExperimentalStdlibApi::class)
private fun parse(packet: String): Packet {
    val type = packet.drop(3).take(3).toInt(2)
    val rest = packet.drop(6)

    return when (type) {
        4 -> {
            rest.chunked(5).let { it.takeWhile { g -> g.first() == '1' } + it.first { g -> g.first() == '0' } }
                .let { Literal("${packet.take(6)}${it.joinToString("")}") }
        }
        else -> {
            when (rest.first()) {
                '0' -> {
                    val totalLength = rest.drop(1).take(15).toInt(2)
                    val subPackets = buildList<Packet> {
                        while (totalLength - sumOf { it.bits.length } > 0) {
                            parse(rest.drop(16 + sumOf { it.bits.length })).also { add(it) }
                        }
                    }
                    Operator("${packet.take(22)}${subPackets.joinToString("") { it.bits }}", subPackets)
                }
                else -> {
                    val subPacketsNumber = rest.drop(1).take(11).toInt(2)
                    val subPackets = buildList<Packet> {
                        repeat(subPacketsNumber) {
                            parse(rest.drop(12 + sumOf { it.bits.length })).also { add(it) }
                        }
                    }
                    Operator("${packet.take(18)}${subPackets.joinToString("") { it.bits }}", subPackets)
                }
            }
        }
    }
}

sealed class Packet(val bits: String) {
    abstract fun sumOfVersions(): Int
    abstract fun value(): Long

    val type = bits.drop(3).take(3).toInt(2)
}

class Literal(bits: String) : Packet(bits) {
    override fun sumOfVersions() = bits.take(3).toInt(2)

    override fun value() = bits.drop(6).chunked(5).joinToString("") { it.drop(1) }.toLong(2)
}

class Operator(bits: String, private val subs: List<Packet>) : Packet(bits) {
    override fun sumOfVersions() = bits.take(3).toInt(2) + subs.sumOf { it.sumOfVersions() }

    override fun value(): Long = when (type) {
        0 -> subs.sumOf { it.value() }
        1 -> subs.fold(1) { acc, packet -> acc * packet.value() }
        2 -> subs.minOf { it.value() }
        3 -> subs.maxOf { it.value() }
        5 -> (subs[0].value() > subs[1].value()).toLong()
        6 -> (subs[0].value() < subs[1].value()).toLong()
        7 -> (subs[0].value() == subs[1].value()).toLong()
        else -> error("Unsupported type $type")
    }

    private fun Boolean.toLong() = if (this) 1L else 0L
}

fun convertToBinaryString(hexadecimalString: String): String {
    return hexadecimalString.mapNotNull { hexadecimalToBinary[it] }.joinToString("")
}

private val hexadecimalToBinary = mapOf(
    Pair('0', "0000"), Pair('1', "0001"), Pair('2', "0010"), Pair('3', "0011"), Pair('4', "0100"), Pair('5', "0101"), Pair('6', "0110"), Pair('7', "0111"), Pair('8', "1000"), Pair('9', "1001"), Pair('A', "1010"), Pair('B', "1011"), Pair('C', "1100"), Pair('D', "1101"), Pair('E', "1110"), Pair('F', "1111")
)