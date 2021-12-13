package com.jarnocegeka.year2021

import com.jarnocegeka.model.Point
import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day13Part1() {
    val lines = readInputFileLines("InputYear2021Day13.txt")
    val blankLine = lines.indexOfFirst { it.isBlank() }
    val points = lines.subList(0, blankLine).map { line -> line.split(',')
        .map { it.toInt() } }
        .map { Point(it[0], it[1]) }.toSet()
    val fold = lines[blankLine + 1].substringAfter("fold along ").split('=')

    var result = points.size
    when (fold[0]) {
        "x" -> result = foldX(fold[1].toInt(), points).size
        "y" -> result = foldY(fold[1].toInt(), points).size
    }

    println("Result: $result")
}

fun adventOfCodeYear2021Day13Part2() {
    val lines = readInputFileLines("InputYear2021Day13.txt")
    val blankLine = lines.indexOfFirst { it.isBlank() }
    val points = lines.subList(0, blankLine).map { line -> line.split(',').map { it.toInt() } }.map { Point(it[0], it[1]) }
    val folds = lines.subList(blankLine+1, lines.size)
        .map { line -> line.substringAfter("fold along ").split('=') }

    var remainingPoints = points.toSet()
    folds.forEach { fold ->
        remainingPoints = fold(remainingPoints, fold)
    }

    val maxX = remainingPoints.maxOf { it.x }
    val maxY = remainingPoints.maxOf { it.y }

    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            if (remainingPoints.contains(Point(x, y))) print("#") else print(".")
        }
        println()
    }

    println("Result: ")
}

private fun fold(points: Set<Point>, fold: List<String>): Set<Point> {
    return when (fold[0]) {
        "x" -> foldX(fold[1].toInt(), points)
        "y" -> foldY(fold[1].toInt(), points)
        else -> emptySet()
    }
}

private fun foldX(xToFoldOver: Int, points: Set<Point>): Set<Point> {
    val firstHalf = points.filter { it.x < xToFoldOver }.toMutableSet()
    val secondHalf = points.filter { it.x > xToFoldOver }

    secondHalf.forEach { point ->
        val dx = xToFoldOver - (point.x - xToFoldOver)
        firstHalf.add(Point(dx, point.y))
    }

    return firstHalf
}

private fun foldY(yToFoldOver: Int, points: Set<Point>): Set<Point> {
    val firstHalf = points.filter { it.y < yToFoldOver }.toMutableSet()
    val secondHalf = points.filter { it.y > yToFoldOver }

    secondHalf.forEach { point ->
        val dy = yToFoldOver - (point.y - yToFoldOver)
        firstHalf.add(Point(point.x, dy))
    }

    return firstHalf
}