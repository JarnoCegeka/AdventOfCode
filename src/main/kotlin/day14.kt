import kotlin.math.pow

fun adventOfCodeDay14Part1() {
    val maskInput = maskInput()

    val memoryValues = mutableMapOf<Long, Long>()
    maskInput.forEach { (mask, inputValues) ->
        inputValues.forEach { (memoryPos, value) ->
            val binary = binary(value)
            val result = maskBinaryPart1(mask, binary)
            memoryValues[memoryPos] = result
        }
    }

    val sum = memoryValues.values.sum()
    println("Total sum of memory: $sum")
}

fun adventOfCodeDay14Part2() {
    val maskInput = maskInput()

    val memoryValues = mutableMapOf<Long, Long>()
    maskInput.forEach { (mask, inputValues) ->
        inputValues.forEach { (memoryPos, value) ->
            val binary = binary(memoryPos)
            val floatingBinary = maskBinaryPart2(mask, binary)
            val memoryAddresses = memoryPositions(floatingBinary)
            memoryAddresses.forEach { memoryValues[it] = value }
        }
    }

    val sum = memoryValues.values.sum()
    println("Total sum of memory: $sum")
}

private fun memoryPositions(floatingBinary: String): Set<Long> {
    val xCount = floatingBinary.count { it.toLowerCase() == 'x' }
    val memoryAddresses = mutableSetOf<Long>()
    val possible = 2.0.pow(xCount).toInt()
    val possiblePerNum = possible / 2

    findNextCombo(floatingBinary, possiblePerNum, '0', '1').map { decimal(it) }.forEach { memoryAddresses.add(it) }
    findNextCombo(floatingBinary, possiblePerNum, '1', '0').map { decimal(it) }.forEach { memoryAddresses.add(it) }

    return memoryAddresses
}

private fun findNextCombo(floatingBinary: String, firstCount: Int, comboStarter: Char, comboToChange: Char): Set<String> {
    val binary = floatingBinary.replaceFirst('X', comboStarter)
    val combos = mutableSetOf<String>()
    if (!binary.contains('X')) {
        return setOf(binary)
    } else {
        combos.addAll(findNextCombo(binary, firstCount / 2, comboStarter, comboToChange))
        combos.addAll(findNextCombo(binary, firstCount / 2, comboToChange, comboStarter))
        val binaryToAdd = binary.replace('X', comboToChange)
        combos.add(binaryToAdd)
    }

    return combos
}

/*private fun memoryPositions(floatingBinary: String): List<Long> {
    val xCount = floatingBinary.count { it.toLowerCase() == 'x' }
    val memoryAddresses = mutableListOf<Long>()
    val possible = 2.0.pow(xCount-1).toInt() - 1

    val lowestBin = floatingBinary.replace('X', '0')
    val highestBin = floatingBinary.replace('X', '1')

    val lowestNum = decimal(lowestBin)
    val highestNum = decimal(highestBin)

    (lowestNum..(lowestNum+possible)).forEach { memoryAddresses.add(it) }
    ((highestNum-possible)..highestNum).forEach { memoryAddresses.add(it) }

    return memoryAddresses
}*/

private fun maskInput(): Map<String, List<Pair<Long, Long>>> {
    val maskMap = mutableMapOf<String, MutableList<Pair<Long, Long>>>()
    var currentMask = ""
    readInputFileLines("InputDay14.txt").forEach {
        if (it.startsWith("mask")) {
            currentMask = it.substringAfter(" = ")
            maskMap[currentMask] = mutableListOf()
        } else {
            val memoryPosition = it.substringAfter("[").substringBefore("]").toLong()
            val decimalValue = it.substringAfter(" = ").toLong()
            maskMap[currentMask]!!.add(Pair(memoryPosition, decimalValue))
        }
    }

    return maskMap
}

private fun maskBinaryPart1(mask: String, binary: String): Long {
    val binaryStr = StringBuilder()

    mask.forEachIndexed { index, c ->
        val binaryC = binary[index]
        if (c.toLowerCase() == 'x') binaryStr.append(binaryC.toString())

        when {
            binaryC == '0' && c == '0' -> binaryStr.append("0")
            binaryC == '1' && c == '0' -> binaryStr.append("0")
            binaryC == '0' && c == '1' -> binaryStr.append("1")
            binaryC == '1' && c == '1' -> binaryStr.append("1")
        }
    }

    val binaryResult = binaryStr.toString()
    return decimal(binaryResult)
}

private fun maskBinaryPart2(mask: String, binary: String): String {
    val binaryStr = StringBuilder()

    mask.forEachIndexed { index, c ->
        val binaryC = binary[index]
        if (c.toLowerCase() == 'x') binaryStr.append("X")

        when {
            binaryC == '0' && c == '0' -> binaryStr.append("0")
            binaryC == '1' && c == '0' -> binaryStr.append("1")
            binaryC == '0' && c == '1' -> binaryStr.append("1")
            binaryC == '1' && c == '1' -> binaryStr.append("1")
        }
    }

    return binaryStr.toString()
}

private fun binary(num: Long): String {
    var decimalNumber = num
    val binaryStr = StringBuilder()

    while (decimalNumber > 0) {
        val r = decimalNumber % 2
        decimalNumber /= 2
        binaryStr.append(r)
    }

    (binaryStr.length until 36).forEach { _ ->
        binaryStr.append(0)
    }

    return binaryStr.reverse().toString()
}

fun decimal(b: String): Long {
    var power = b.lastIndex

    var sum = 0L
    b.forEach {
        val r = it.toString().toInt()
        val value = r * (2.0.pow(power.toDouble())).toLong()
        sum += value
        power--
    }

    return sum
}
