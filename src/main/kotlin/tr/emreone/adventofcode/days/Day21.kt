package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.extensions.area
import tr.emreone.kotlin_utils.extensions.formatted
import tr.emreone.kotlin_utils.math.*
import java.util.Queue

class Day21 : Day(21, 2023, "Step Counter") {

    val map = inputAsGrid
    private val area = map.area

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

    private fun fillGarden(startingPoint: Point, steps: Int): Set<Point> {
        val reachableGardenPlots = mutableSetOf<Point>()
        val seenGardenPlots = mutableSetOf(startingPoint)

        val q = ArrayDeque<Pair<Point, Int>>()
        q.add(startingPoint to 64)

        while(q.isNotEmpty()) {
            val (point, steps) = q.removeFirst()

            if (steps.mod(2) == 0) {
                reachableGardenPlots.add(point)
            }
            if (steps == 0) {
                continue
            }

            point.directNeighbors().forEach {
                if (it in area && map[it.y][it.x] != '#' && it !in seenGardenPlots) {
                    q.add(it to steps - 1)
                    seenGardenPlots.add(it)
                }
            }
        }
        return reachableGardenPlots.toSet()
    }

    override fun part1(): Int {
        // find S in the map
        val startingPoint = getStartingPoint()
        val reachableGardenPlots = this.fillGarden(startingPoint, 64)

        println(map.formatted { p, c ->
            if (p in reachableGardenPlots) {
                 "O"
            } else {
                c.toString()
            }
        })

        return reachableGardenPlots.size
    }


    override fun part2(): Long {


        return 0L
    }
}
