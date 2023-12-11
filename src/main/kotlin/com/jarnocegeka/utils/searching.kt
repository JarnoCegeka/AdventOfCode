package com.jarnocegeka.utils

import com.jarnocegeka.model.Coordinate
import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

fun <T> Collection<T>.findFirstCommonItem(other: Collection<T>): T {
    for (item in other) {
        if (this.contains(item)) return item
    }

    throw IllegalStateException("No common item found!")
}

fun <T> List<T>.findFirstCommonItem(other: List<T>, another: List<T>): T {
    var i = 0
    var j = 0
    var k = 0

    while (i < this.size && j < other.size && k < another.size) {
        if (this[i] == other[j] && other[j] == another[k]) {
            return this[i]
        }

        if (this[i].hashCode() < other[j].hashCode()) {
            i++
        } else if (other[j].hashCode() < another[k].hashCode()) {
            j++
        } else {
            k++
        }
    }

    throw IllegalStateException("No common item found!")
}

fun aStarSearchWithManhattanDistance(start: Coordinate, goal: Coordinate): Long {
    return abs(start.x.toLong() - goal.x.toLong()) + abs(start.y.toLong() - goal.y.toLong())
}

fun aStarSearchWithDiagonalDistance(start: Coordinate, goal: Coordinate, d: Int = 1, d2: Double = sqrt(2.0)): Double {
    val dx = abs (start.x - goal.x)
    val dy = abs(start.y - goal.y)

    return d * (dx + dy) + (d2 - 2 * d) * min(dx, dy)
}

fun aStarSearchWithEuclideanDistance(start: Coordinate, goal: Coordinate): Double {
    return sqrt((start.x - goal.x).toDouble().pow(2) + (start.y - goal.y).toDouble().pow(2))
}