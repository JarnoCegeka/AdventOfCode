package com.jarnocegeka.model

import kotlin.math.pow
import kotlin.math.sqrt

data class Coordinate(var x: Int, var y: Int) {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Coordinate) return false

        return isSameCoordinate(other)
    }

    fun directionCoordinates(withDiagonallyCoordinates: Boolean = false, minX: Int = 0, minY: Int = 0, maxX: Int = Int.MAX_VALUE, maxY: Int = Int.MAX_VALUE): List<Coordinate> {
        return listOf(
            Coordinate(x, y - 1),
            Coordinate(x, y + 1),
            Coordinate(x - 1, y),
            Coordinate(x + 1, y)
        ).filter { it.x in minX..maxX && it.y in minY..maxY }
    }

    fun calculateDistanceTo(other: Coordinate): Double {
        return sqrt((other.x - this.x).toDouble().pow(2) + (other.y - this.y).toDouble().pow(2))
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    private fun isSameCoordinate(other: Coordinate): Boolean {
        return x == other.x && y == other.y
    }
}