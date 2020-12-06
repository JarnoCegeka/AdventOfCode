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
}

fun readInputFile(fileName: String) : File {
    return File("$FILE_PREFIX$fileName")
}

fun readInputFileLines(fileName: String) : List<String> {
    return readInputFile(fileName).readLines()
}