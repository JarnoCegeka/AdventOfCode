package com.jarnocegeka.year2020

import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2020Day01Part1() {
    val lines = readInputFileLines("InputYear2020Day01.txt")

    for (i in lines) l@{
        for (j in lines.subList(1, lines.size)) {
            if ((i.toInt() + j.toInt()) == 2020) {
                println("$i + $j = 2020")
                println("$i * $j = ${i.toInt() * j.toInt()}")
                println("-------------------------------------------------------------------------")
            }
        }
    }
}

fun adventOfCodeYear2020Day01Part2() {
    val lines = readInputFileLines("InputYear2020Day01.txt")

    for (i in lines) {
        for (j in lines.subList(1, lines.size)) {
            for (k in lines.subList(2, lines.size)) {
                if ((i.toInt() + j.toInt() + k.toInt()) == 2020) {
                    println("$i + $j + $k = 2020")
                    println("$i * $j * $k = ${i.toInt() * j.toInt() * k.toInt()}")
                    println("-------------------------------------------------------------------------")
                }
            }
        }
    }
}