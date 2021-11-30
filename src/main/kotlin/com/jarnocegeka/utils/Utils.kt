package com.jarnocegeka.utils

import java.io.File

const val FILE_PREFIX = "src/main/resources/"

fun readInputFile(fileName: String): File {
    return File("$FILE_PREFIX$fileName")
}

fun readInputFileLines(fileName: String): List<String> {
    return readInputFile(fileName).readLines()
}