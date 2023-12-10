package com.jarnocegeka.model

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun destinationCoordinate(x: Int, y: Int): Coordinate {
        return when(this) {
            NORTH -> Coordinate(x, y - 1)
            EAST -> Coordinate(x + 1, y)
            SOUTH -> Coordinate(x, y + 1)
            WEST -> Coordinate(x - 1, y)
        }
    }

    fun opposite(): Direction {
        return when(this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }
    }
}