package com.jarnocegeka.year2023

import com.jarnocegeka.model.Coordinate
import com.jarnocegeka.model.Direction
import com.jarnocegeka.model.Direction.*
import com.jarnocegeka.utils.readInputFileLines
import com.jarnocegeka.utils.reduceToIntWithSum
import java.util.Vector

fun adventOfCodeYear2023Day10Part1() {
    val lines = readInputFileLines("year2023/InputYear2023Day10.txt")
    val startingPipePoint = determineStartingPipePoint(lines)
    val pipePoints = mapToPipePoints(lines)

    val stepsTaken = traverseLoop(startingPipePoint, pipePoints).size
    val farthestPoint = stepsTaken / 2

    println(farthestPoint)
}

fun adventOfCodeYear2023Day10Part2() {
    val lines = readInputFileLines("year2023/InputYear2023Day10.txt")
    val startingPipePoint = determineStartingPipePoint(lines)
    val pipePoints = mapToPipePoints(lines)
    val loopPoints = traverseLoop(startingPipePoint, pipePoints)
    cleanJunkPipes(pipePoints, loopPoints)

    val expandedMap = expandMapWithGroundAndStraightPipes(pipePoints)
    val filledGroundEdges = fillGroundCoordinates(expandedMap).toMutableList()
    (1 until filledGroundEdges.size step 2).reversed().forEach { filledGroundEdges.removeAt(it) }
    val unexpandedMap = filledGroundEdges.map { row ->
        val reverseUnevenIndices = (1 until row.size step 2).reversed()
        val copy = row.toMutableList()
        reverseUnevenIndices.forEach { copy.removeAt(it) }
        copy
    }

    println(unexpandedMap.map { row -> row.count { it.isGround() } }.reduceToIntWithSum())
}

private fun determineStartingPipePoint(lines: List<String>): StartingPipePoint {
    val y = lines.indexOfFirst { it.contains('S') }
    val x = lines[y].indexOfFirst { it == 'S' }

    return StartingPipePoint(x, y)
}

private fun mapToPipePoints(lines: List<String>): List<List<PipePoint>> {
    return lines.mapIndexed { y, line -> mapToPipePoints(line, y) }
}

private fun mapToPipePoints(line: String, y: Int): List<PipePoint> {
    return line.mapIndexed { x, value -> PipePoint(PipeShape(value, possibleDirectionsFor(value)), x, y) }
}

private fun possibleDirectionsFor(value: Char): List<Direction> {
    return when(value) {
        '|' -> listOf(NORTH, SOUTH)
        '-' -> listOf(EAST, WEST)
        'L' -> listOf(EAST, NORTH)
        'J' -> listOf(NORTH, WEST)
        '7' -> listOf(SOUTH, WEST)
        'F' -> listOf(EAST, SOUTH)
        else -> emptyList()
    }
}

private fun traverseLoop(startingPipePoint: StartingPipePoint, pipePoints: List<List<PipePoint>>): List<Coordinate> {
    val startingDirection = findFirstStartingDirection(startingPipePoint, pipePoints)
    val nextToStartCoordinate = startingDirection.destinationCoordinate(startingPipePoint.x, startingPipePoint.y)

    var stepsTaken = 1
    var currentPipePoint = pipePoints[nextToStartCoordinate.y][nextToStartCoordinate.x]
    var direction = currentPipePoint.nextDirection(startingDirection)
    val loopPoints = mutableListOf(currentPipePoint.coordinate())

    do {
        val nextCoordinate = currentPipePoint.nextCoordinate(direction)
        currentPipePoint = pipePoints[nextCoordinate.y][nextCoordinate.x]
        loopPoints.add(currentPipePoint.coordinate())
        if (currentPipePoint.hasSameCoordinates(startingPipePoint.x, startingPipePoint.y)) return loopPoints
        direction = currentPipePoint.nextDirection(direction)

        stepsTaken++
    } while(true)
}

private fun findFirstStartingDirection(startingPipePoint: StartingPipePoint, pipePoints: List<List<PipePoint>>): Direction {
    val north = if (startingPipePoint.y > 0) pipePoints[startingPipePoint.y - 1][startingPipePoint.x] else null
    val east = if (startingPipePoint.x > 0) pipePoints[startingPipePoint.y][startingPipePoint.x + 1] else null
    val south = if (startingPipePoint.y < pipePoints.size) pipePoints[startingPipePoint.y + 1][startingPipePoint.x] else null

    return when {
        north != null && north.shape.possibleDirections.contains(SOUTH) -> NORTH
        east != null && east.shape.possibleDirections.contains(WEST) -> EAST
        south != null && south.shape.possibleDirections.contains(NORTH) -> SOUTH
        else -> WEST
    }
}

private fun floodFill(pipePoints: List<List<PipePoint>>, m: Int, n: Int, x: Int, y: Int, oldChar: Char, newChar: Char) {
    val queue = Vector<Coordinate>()

    queue.add(Coordinate(x, y))
    pipePoints[y][x].shape.value = newChar

    while (queue.size > 0) {
        val current = queue[queue.size - 1]
        queue.removeAt(queue.size - 1)

        val posX = current.x
        val posY = current.y

        if (isValid(pipePoints, m, n, posX + 1, posY, oldChar, newChar)) {
            pipePoints[posY][posX + 1].shape.value = newChar
            queue.add(Coordinate(posX + 1, posY))
        }

        if(isValid(pipePoints, m, n, posX - 1, posY, oldChar, newChar)) {
            pipePoints[posY][posX - 1].shape.value = newChar
            queue.add(Coordinate(posX - 1, posY))
        }

        if(isValid(pipePoints, m, n, posX, posY + 1, oldChar, newChar)) {
            pipePoints[posY + 1][posX].shape.value = newChar
            queue.add(Coordinate(posX, posY + 1))
        }

        if(isValid(pipePoints, m, n, posX, posY - 1, oldChar, newChar)) {
            pipePoints[posY - 1][posX].shape.value = newChar
            queue.add(Coordinate(posX, posY - 1))
        }
    }
}

private fun isValid(pipePoints: List<List<PipePoint>>, m: Int, n: Int, x: Int, y: Int, oldChar: Char, newChar: Char): Boolean {
    return !(x < 0 || x >= m || y < 0 || y >= n || pipePoints[y][x].shape.value != oldChar || pipePoints[y][x].shape.value == newChar)
}

private fun expandMapWithGroundAndStraightPipes(pipePoints: List<List<PipePoint>>): List<List<PipePoint>> {
    val newRowSize = pipePoints.size * 2
    val newColumnSize = pipePoints.first().size * 2
    val newPipePoints = mutableListOf<MutableList<PipePoint>>()

    (0 until newRowSize).forEach { y ->
        newPipePoints.add(mutableListOf())
        (0 until newColumnSize).forEach { x ->
            if (y % 2 == 0 && x % 2 == 0) {
                newPipePoints[y].add(PipePoint(pipePoints[y / 2][x / 2].shape, x, y))
            } else {
                val directionCoordinates = Coordinate(x, y).directionCoordinates(maxX = newColumnSize - 1, maxY = newRowSize - 1)
                val verticalCoordinates = directionCoordinates.filter { it.y == y }
                val horizontalCoordinates = directionCoordinates.filter { it.x == x }

                if (verticalCoordinates.all { it.x % 2 == 0 } && y % 2 == 0 && canTraverseHorizontallyToEachOther(verticalCoordinates.map { pipePoints[it.y / 2][it.x / 2] })) {
                    newPipePoints[y].add(PipePoint(PipeShape('-', emptyList()), x, y))
                } else if (horizontalCoordinates.all { it.y % 2 == 0 } && x % 2 == 0 && canTraverseVerticallyToEachOther(horizontalCoordinates.map { pipePoints[it.y / 2][it.x / 2] })) {
                    newPipePoints[y].add(PipePoint(PipeShape('|', emptyList()), x, y))
                } else {
                    newPipePoints[y].add(PipePoint(PipeShape('.', emptyList()), x, y))
                }
            }
        }
    }

    return newPipePoints
}

private fun canTraverseHorizontallyToEachOther(pipePoints: List<PipePoint>): Boolean {
    val leftPoint = pipePoints.minByOrNull { it.x } ?: pipePoints.first()
    val rightPoint = pipePoints.maxByOrNull { it.x } ?: pipePoints.last()

    val leftPipe = leftPoint.shape.value
    val rightPipe = rightPoint.shape.value

    return when {
        leftPipe == '-' && rightPipe == '-' -> true
        leftPipe == '-' && rightPipe == '7' -> true
        leftPipe == '-' && rightPipe == 'J' -> true
        leftPipe == 'L' && rightPipe == '-' -> true
        leftPipe == 'F' && rightPipe == '-' -> true
        leftPipe == 'F' && rightPipe == 'J' -> true
        leftPipe == 'L' && rightPipe == 'J' -> true
        leftPipe == 'F' && rightPipe == '7' -> true
        leftPipe == 'L' && rightPipe == '7' -> true
        leftPipe == 'S' && rightPipe == '-' -> true
        leftPipe == 'S' && rightPipe == 'J' -> true
        leftPipe == 'S' && rightPipe == '7' -> true
        leftPipe == '-' && rightPipe == 'S' -> true
        leftPipe == 'L' && rightPipe == 'S' -> true
        leftPipe == 'F' && rightPipe == 'S' -> true
        else -> false
    }
}

private fun canTraverseVerticallyToEachOther(pipePoints: List<PipePoint>): Boolean {
    val topPoint = pipePoints.minByOrNull { it.y } ?: pipePoints.first()
    val bottomPoint = pipePoints.maxByOrNull { it.y } ?: pipePoints.last()

    val topPipe = topPoint.shape.value
    val bottomPipe = bottomPoint.shape.value

    return when {
        topPipe == '|' && bottomPipe == '|' -> true
        topPipe == '|' && bottomPipe == 'L' -> true
        topPipe == '|' && bottomPipe == 'J' -> true
        topPipe == '7' && bottomPipe == '|' -> true
        topPipe == 'F' && bottomPipe == '|' -> true
        topPipe == 'F' && bottomPipe == 'L' -> true
        topPipe == '7' && bottomPipe == 'L' -> true
        topPipe == 'F' && bottomPipe == 'J' -> true
        topPipe == '7' && bottomPipe == 'J' -> true
        topPipe == 'S' && bottomPipe == '|' -> true
        topPipe == 'S' && bottomPipe == 'J' -> true
        topPipe == 'S' && bottomPipe == 'L' -> true
        topPipe == '|' && bottomPipe == 'S' -> true
        topPipe == '7' && bottomPipe == 'S' -> true
        topPipe == 'F' && bottomPipe == 'S' -> true
        else -> false
    }
}

private fun cleanJunkPipes(pipePoints: List<List<PipePoint>>, loopPoints: List<Coordinate>) {
    pipePoints.forEach { row ->
        row.filter { !loopPoints.contains(it.coordinate()) }.forEach {
            it.shape.value = '.'
            it.shape.possibleDirections = emptyList()
        }
    }
}

private fun groundCoordinatesOnEdge(pipePoints: List<List<PipePoint>>): List<Coordinate> {
    val groundPointsOnTopRow = pipePoints[0].filter { it.isGround() }.map { it.coordinate() }
    val groundPointsOnBottomRow = pipePoints[pipePoints.size - 1].filter { it.isGround() }.map { it.coordinate() }
    val groundPointsOnFirstColumn = mutableListOf<Coordinate>()
    val groundPointsOnLastColumn = mutableListOf<Coordinate>()
    (1 until pipePoints.size - 2).forEach { y ->
        val row = pipePoints[y]
        groundPointsOnFirstColumn.add(row[0].coordinate())
        groundPointsOnLastColumn.add(row[row.size - 1].coordinate())
    }

    return concatenate(groundPointsOnTopRow, groundPointsOnBottomRow, groundPointsOnFirstColumn, groundPointsOnLastColumn)
}

private fun fillGroundCoordinates(pipePoints: List<List<PipePoint>>): List<List<PipePoint>> {
    val groundCoordinatesOnEdge = groundCoordinatesOnEdge(pipePoints).toMutableList()

    while (groundCoordinatesOnEdge.isNotEmpty()) {
        val first = groundCoordinatesOnEdge.first()
        if (pipePoints[first.y][first.x].isGround()) {
            floodFill(pipePoints, pipePoints.first().size, pipePoints.size, first.x, first.y, '.', '0')
        }

        groundCoordinatesOnEdge.removeAt(0)
    }

    return pipePoints
}

private class PipePoint(val shape: PipeShape, val x: Int, val y: Int) {
    fun nextCoordinate(nextDirection: Direction): Coordinate {
        return nextDirection.destinationCoordinate(x, y)
    }

    fun nextDirection(direction: Direction): Direction {
        return shape.nextDirection(direction)
    }

    fun hasSameCoordinates(x: Int, y: Int): Boolean {
        return this.x == x && this.y == y
    }

    fun isGround(): Boolean {
        return shape.value == '.'
    }

    fun coordinate(): Coordinate {
        return Coordinate(x, y)
    }
}

private class StartingPipePoint(val x: Int, val y: Int)
private class PipeShape(var value: Char, var possibleDirections: List<Direction>) {
    fun nextDirection(direction: Direction): Direction {
        return possibleDirections.first { it != direction.opposite() }
    }
}