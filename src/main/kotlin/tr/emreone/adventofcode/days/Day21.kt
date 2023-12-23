package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.extensions.area
import tr.emreone.kotlin_utils.extensions.formatted
import tr.emreone.kotlin_utils.math.*

class Day21 : Day(21, 2023, "Step Counter") {

    val map = inputAsGrid
    val area = map.area

    private fun getStartingPoint(): Point {
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == 'S') {
                    return Point(x, y)
                }
            }
        }

        throw IllegalStateException("No starting point found")
    }

    override fun part1(): Int {

        // find S in the map
        val startingPoint = getStartingPoint()
        var reachableGardenPlots = setOf(startingPoint)

        repeat(64) {
            reachableGardenPlots = reachableGardenPlots
                .map {
                    it.directNeighbors()
                        .filter {
                            it in area && map[it.y][it.x] != '#'
                        }
                }
                .flatten()
                .toSet()
        }

        println(map.formatted { p, c ->
            if (p in reachableGardenPlots) {
                 "O"
            } else {
                c.toString()
            }
        })

        return reachableGardenPlots.size
    }

    override fun part2(): Int {
        // find S in the map
        val startingPoint = getStartingPoint()
        var reachableGardenPlots = setOf(startingPoint)

        repeat(26_501_365) {
            reachableGardenPlots = reachableGardenPlots
                .map {
                    it.directNeighbors()
                        .filter {
                            map[it.y.mod(map.size)][it.x.mod(map[0].size)] != '#'
                        }
                }
                .flatten()
                .toSet()
        }

        return reachableGardenPlots.size
    }
}
