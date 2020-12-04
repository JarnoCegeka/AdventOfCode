fun adventOfCodeDay01Part1() {
    val lines = readInputFileLines("InputDay01.txt")

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

fun adventOfCodeDay01Part2() {
    val lines = readInputFileLines("InputDay01.txt")

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