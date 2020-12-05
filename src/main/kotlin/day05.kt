fun adventOfCodeDay05Part1() {
    val file = readInputFile("InputDay05.txt")

    var highestId = 0
    file.forEachLine {
        val row = findRow(it.substring(0..6))
        val column = findColumn(it.substring(7))
        val id = (row * 8) + column

        if (id > highestId) highestId = id
    }

    println("Highest ID: $highestId")
}

fun adventOfCodeDay05Part2() {
    val file = readInputFile("InputDay05.txt")

    val idList = mutableListOf<Int>()
    file.forEachLine {
        val row = findRow(it.substring(0..6))
        val column = findColumn(it.substring(7))
        val id = (row * 8) + column
        idList.add(id)
    }

    idList.sort()

    idList.forEachIndexed { index, i ->
        if (index != idList.lastIndex && idList[index+1] != i+1) println("${i+1}")
    }
}

private fun findRow(rowString: String) : Int {
    var lowerRow = 0
    var upperRow = 127
    var rowDivider = 64
    rowString.forEach { letter ->
        when (letter) {
            'F' -> upperRow -= rowDivider
            'B' -> lowerRow += rowDivider
        }
        rowDivider /= 2
    }

    return upperRow
}

private fun findColumn(columnString: String) : Int {
    var lowerColumn = 0
    var upperColumn = 7
    var columnDivider = 4
    columnString.forEach { letter ->
        when (letter) {
            'L' -> upperColumn -= columnDivider
            'R' -> lowerColumn += columnDivider
        }
        columnDivider /= 2
    }

    return upperColumn
}
