package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.minus
import tr.emreone.kotlin_utils.math.plus
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y
import kotlin.math.max

class Day23 : Day(23, 2023, "A Long Walk") {

    private val grid = inputAsGrid
    private val start = Point(grid.first().indexOfFirst { it == '.' }, 0)
    private val goal = Point(grid.last().indexOfFirst { it == '.' }, grid.lastIndex)

    private fun traverse(nextLocations: (Point) -> List<Pair<Point, Int>>): Int {
        var best = 0
        val visited = mutableSetOf<Point>()

        fun traverseWork(location: Point, steps: Int): Int {
            if (location == goal) {
                best = max(steps, best)
                return best
            }
            visited += location
            nextLocations(location)
                .filter { (place, _) -> place !in visited }
                .forEach { (place, distance) -> traverseWork(place, distance + steps) }
            visited -= location
            return best
        }

        return traverseWork(start, 0)
    }

    private fun Char.matchesDirection(direction: Point): Boolean =
        when (this) {
            '^' -> Direction4.NORTH.vector == direction
            '<' -> Direction4.WEST.vector == direction
            'v' -> Direction4.SOUTH.vector == direction
            '>' -> Direction4.EAST.vector == direction
            '.' -> true
            else -> false
        }

    private fun List<List<Char>>.findDecisionPoints() = buildSet {
        add(start)
        add(goal)
        this@findDecisionPoints.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c != '#') {
                    Point(x, y).apply {
                        val numberOfNeighbors = Direction4.entries
                            .map { dir -> this + dir.vector }
                            .count {
                                it.x in grid.first().indices && it.y in grid.indices && grid[it.y][it.x] != '#'
                            }

                        if (numberOfNeighbors > 2) {
                            add(this)
                        }
                    }
                }
            }
        }
    }

    private fun reduceGridFromPoint(from: Point, toAnyOther: Set<Point>): Map<Point, Int> {
        val queue = ArrayDeque<Pair<Point, Int>>().apply {
            add(from to 0)
        }
        val seen = mutableSetOf(from)
        val answer = mutableMapOf<Point, Int>()
        while (queue.isNotEmpty()) {
            val (location, distance) = queue.removeFirst()
            if (location != from && location in toAnyOther) {
                answer[location] = distance
            } else {
                Direction4.entries
                    .map {
                        location + it.vector
                    }
                    .filter {
                        it.x in grid.first().indices && it.y in grid.indices && grid[it.y][it.x] != '#'
                    }
                    .filter { it !in seen }
                    .forEach {
                        seen += it
                        queue.add(it to distance + 1)
                    }
            }
        }
        return answer
    }

    private fun reduceGrid(): Map<Point, Map<Point, Int>> {
        return grid.findDecisionPoints()
            .let { decisionPoints ->
                decisionPoints.associateWith { point ->
                    reduceGridFromPoint(point, decisionPoints)
                }
            }
    }


    override fun part1(): Int {
        return traverse { location ->

            Direction4.entries
                .map { location + it.vector }
                .filter {
                    it.x in grid.first().indices && it.y in grid.indices && grid[it.y][it.x] != '#'
                }
                .filter { newLocation ->
                    grid[newLocation.y][newLocation.x].matchesDirection(newLocation - location)
                }
                .map { it to 1 }
        }
    }

    override fun part2(): Int {
        val reducedGrid = reduceGrid()
        return traverse { location ->
            reducedGrid
                .getValue(location)
                .map { it.key to it.value }
        }
    }
}
