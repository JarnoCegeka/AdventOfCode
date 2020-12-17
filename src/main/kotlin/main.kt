import java.io.File

const val FILE_PREFIX = "src/main/resources/"

fun main() {
    //adventOfCodeDay01Part1()
    //adventOfCodeDay01Part2()
    //adventOfCodeDay02Part1()
    //adventOfCodeDay02Part2()
    //adventOfCodeDay03Part1()
    //adventOfCodeDay03Part2()
    //adventOfCodeDay04Part1()
    //adventOfCodeDay04Part2()
    //adventOfCodeDay05Part1()
    //adventOfCodeDay05Part2()
    //adventOfCodeDay06Part1()
    //adventOfCodeDay06Part2()
    //adventOfCodeDay07Part1()
    //adventOfCodeDay07Part2()
    //adventOfCodeDay08Part1()
    //adventOfCodeDay08Part2()
    //adventOfCodeDay09Part1()
    //adventOfCodeDay09Part2()
    //adventOfCodeDay10Part1()
    //adventOfCodeDay10Part2()
    //adventOfCodeDay11Part1()
    //adventOfCodeDay11Part2()
    //adventOfCodeDay12Part1()
    //adventOfCodeDay12Part2()
    //adventOfCodeDay13Part1()
    //adventOfCodeDay13Part2()
    //adventOfCodeDay14Part1()
    //adventOfCodeDay14Part2()
    //adventOfCodeDay15Part1()
    //adventOfCodeDay15Part2()
    //adventOfCodeDay16Part1()
    //adventOfCodeDay16Part2()
    //adventOfCodeDay17Part1()
    adventOfCodeDay17Part2()
}

fun readInputFile(fileName: String) : File {
    return File("$FILE_PREFIX$fileName")
}

fun readInputFileLines(fileName: String) : List<String> {
    return readInputFile(fileName).readLines()
}