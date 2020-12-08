const val ACC = "acc"
const val JMP = "jmp"
const val NOP = "nop"
val fileLines = readInputFileLines("InputDay08.txt").toMutableList()

fun adventOfCodeDay08Part1() {
    val bug = runProgram()
    println("Accumulator: ${bug.accumulator}")
}

private fun runProgram(listToCheck: List<String> = fileLines): Bug {
    var accumulator = 0
    var currentIndex = 0
    val visitedIndexes = mutableSetOf<Int>()
    while (currentIndex <= listToCheck.lastIndex) {
        if (visitedIndexes.contains(currentIndex)) break else visitedIndexes.add(currentIndex)

        val instruction = listToCheck[currentIndex]
        val instructionCode = instruction.substring(0..2)
        val instructionValue = instruction.substringAfterLast(" ")

        val instructionValueSign = instructionValue.substring(0, 1)
        val instructionValueNumber = instructionValue.substring(1).toInt()
        if (instructionCode == ACC) {
            if (instructionValueSign == "+") accumulator += instructionValueNumber else accumulator -= instructionValueNumber
            currentIndex++
        } else if (instructionCode == JMP) {
            if (instructionValueSign == "+") currentIndex += instructionValueNumber else currentIndex -= instructionValueNumber
        } else {
            currentIndex++
        }
    }

    return Bug("", currentIndex, accumulator)
}

fun adventOfCodeDay08Part2() {
    var indexToCheck = 0
    var changedInstructionList: List<String>
    var bug = Bug()
    while(indexToCheck <= fileLines.lastIndex) {
        changedInstructionList = changeInstruction(indexToCheck, fileLines)
        bug = runProgram(changedInstructionList)

        if (bug.currentIndex > fileLines.lastIndex) break
        indexToCheck++
    }

    println("End: ${bug.accumulator}")
}

fun changeInstruction(index: Int, list: List<String>): List<String> {
    val newList = list.toMutableList()
    val instruction = newList[index]

    newList[index] = when (instruction.substring(0..2)) {
        JMP -> newList[index].replaceRange(0..2, NOP)
        NOP -> newList[index].replaceRange(0..2, JMP)
        else -> newList[index]
    }

    return newList
}

private fun bug(currentIndex: Int, accumulator: Int): Bug {
    var newIndex = currentIndex
    var newAccumulatorValue = accumulator
    val currentInstruction = fileLines[newIndex]
    val instructionCode = currentInstruction.substring(0..2)
    val instructionValue = currentInstruction.substringAfterLast(" ")

    val instructionValueSign = instructionValue.substring(0, 1)
    val instructionValueNumber = instructionValue.substring(1).toInt()
    if (instructionCode == ACC) {
        if (instructionValueSign == "+") newAccumulatorValue += instructionValueNumber else newAccumulatorValue -= instructionValueNumber
        newIndex++
    } else if (instructionCode == JMP) {
        if (instructionValueSign == "+") newIndex += instructionValueNumber else newIndex -= instructionValueNumber
    } else {
        newIndex++
    }

    return Bug(currentInstruction, newIndex, newAccumulatorValue)
}

private data class Bug(val currentInstruction: String = "", val currentIndex: Int = 0, val accumulator: Int = 0)