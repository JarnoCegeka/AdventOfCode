package com.jarnocegeka.year2021

import com.jarnocegeka.model.Point
import com.jarnocegeka.utils.readInputFileLines

fun adventOfCodeYear2021Day05Part1() {
    val lineStrings = readInputFileLines("InputYear2021Day05.txt")
    val lines = lineStrings.map { toLine(it) }.filterNot { it.isDiagonal() }

    val result = retrieveOverlappingCount(lines)
    println("Result: $result")
}

fun adventOfCodeYear2021Day05Part2() {
    val lineStrings = readInputFileLines("InputYear2021Day05.txt")
    val lines = lineStrings.map { toLine(it) }

    val result = retrieveOverlappingCount(lines)
    println("Result: $result")
}

fun retrieveOverlappingCount(lines: List<Line>): Int {
    val pointCounter = mutableMapOf<Point, Int>()
    lines.forEach { line ->
        val linePoints = line.linePoints()
        linePoints.forEach { point ->
            if (pointCounter.containsKey(point)) pointCounter[point] = pointCounter[point]!! + 1 else pointCounter[point] = 1
        }
    }

    return pointCounter.count { it.value > 1 }
}

fun toLine(lineString: String): Line {
    val points = lineString.split(" -> ").map { toPoint(it) }
    return Line(points[0], points[1])
}

fun toPoint(pointString: String): Point {
    val pointValues = pointString.split(",").map { it.toInt() }
    return Point(pointValues[0], pointValues[1])
}

data class Line(val startPoint: Point, val endPoint: Point) {
    private fun isHorizontal(): Boolean {
        return startPoint.y == endPoint.y
    }

    private fun isVertical(): Boolean {
        return startPoint.x == endPoint.x
    }

    fun isDiagonal(): Boolean {
        return !isHorizontal() && !isVertical()
    }

    fun linePoints(): List<Point> {
        if (isHorizontal()) {
            val x1 = startPoint.x
            val x2 = endPoint.x
            val y = startPoint.y
            return if (x1 <= x2) (x1..x2).map { Point(it, y) } else (x2..x1).map { Point(it, y) }
        } else if (isVertical()) {
            val y1 = startPoint.y
            val y2 = endPoint.y
            val x = startPoint.x
            return if (y1 <= y2) (y1..y2).map { Point(x, it) } else (y2..y1).map { Point(x, it) }
        } else {
            val x1 = startPoint.x
            val x2 = endPoint.x
            val y1 = startPoint.y
            val y2 = endPoint.y

            val dx = if (x1 <= x2) (x1..x2).map { it } else (x2..x1).map { it }.reversed()
            val dy = if (y1 <= y2) (y1..y2).map { it } else (y2..y1).map { it }.reversed()

            return (dx.indices).map { Point(dx[it], dy[it]) }
        }
    }
}