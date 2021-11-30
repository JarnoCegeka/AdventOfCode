fun adventOfCodeDay19Part1() {
    checkInput()
}

fun adventOfCodeDay19Part2() {
    readInputFileLines("InputDay18.txt")
}

fun checkInput() {
    val input = readInputFileLines("InputDay19.txt")
    val blankLine = input.indexOfFirst { it.isBlank() }
    val rulesTxt = input.subList(0, blankLine)
    val messages = input.subList(blankLine+1, input.size)
    //val (rulesTxt, messages) = input.split("\n\n")
    val rules = rulesTxt.map { val parts = it.split(":");parts[0].toInt() to parts[1].trim() }.toMap().toSortedMap()

    fun matchesRules(msg: String, msgr: List<Int>): Boolean {
        if (msg.length == 0) {
            return msgr.isEmpty()
        }
        if (msgr.isEmpty()) {
            return false
        }
        val first = rules[msgr.first()]!!
        if (first[0] == '"') {
            return if (msg.startsWith(first[1])) {
                matchesRules(msg.drop(1), msgr.drop(1))
            } else {
                false
            }
        }
        return first.split("|").map { it.trim() }.firstOrNull {
            matchesRules(msg, it.split(" ").map { it.toInt() } + msgr.drop(1))
        } != null
    }

    println(messages.count { matchesRules(it, listOf(0)) })

    rules[8] = "42 | 42 8"
    rules[11] = "42 31 | 42 11 31"
    println(messages.count { matchesRules(it, listOf(0)) })
}