package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.extensions.area
import tr.emreone.kotlin_utils.extensions.formatted
import tr.emreone.kotlin_utils.extensions.height
import tr.emreone.kotlin_utils.extensions.width
import tr.emreone.kotlin_utils.math.*
import java.util.Queue

class Day21 : Day(21, 2023, "Step Counter") {

    val map = inputAsGrid
    private val area = map.area
    private var mapSize: Int


    init {
        require(map.height == map.width)
        mapSize = map.height
    }


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

    private fun print(reachableGardenPlots: Set<Point>) {
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (Point(x, y) in reachableGardenPlots) {
                    print('O')
                } else {
                    print(map[y][x])
                }
            }
            println()
        }
    }

    private fun fillGarden(startingPoint: Point, steps: Int): Set<Point> {
        val reachableGardenPlots = mutableSetOf<Point>()
        val seenGardenPlots = mutableSetOf(startingPoint)

        val q = ArrayDeque<Pair<Point, Int>>()
        q.add(startingPoint to steps)

        while(q.isNotEmpty()) {
            val (point, remainingSteps) = q.removeFirst()

            if (remainingSteps.mod(2) == steps.mod(2)) {
                reachableGardenPlots.add(point)
            }
            if (remainingSteps == 0) {
                continue
            }

            point.directNeighbors().forEach {
                if (it in area && map[it.y][it.x] != '#' && it !in seenGardenPlots) {
                    q.add(it to remainingSteps - 1)
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

        // this.print(reachableGardenPlots)

        return reachableGardenPlots.size
    }

    /**
     * https://www.youtube.com/watch?v=9UOMZSL0JTg
     * https://github.com/hyper-neutrino/advent-of-code/blob/main/2023/day21p2.py
     * ┌ ┐ └ ┘ ┼ ─ │ ├ ┤
     *
     * One Square is the given input.
     * S is the starting point.
     *
     * We try to sketch all the reachable points in the map.
     * Some maps are completely fillable, but the maps on the corner are not.
     * There are different types of maps:
     * - e are maps with even number of steps away
     * - o are maps with odd number of steps away
     * - t, r, b, l are the maps on the direction of top, right, bottom, left
     * - btr and str are the top right corner maps, if you draw the diagonal line from top to right.
     *      - btr is the map with the bigger part within the reachable points
     *      - str is the map with the smaller part within the reachable points
     * - the same applies for other corners
     *      - tl, btl, stl -> top left
     *      - br, bbr, sbr -> bottom right
     *      - bl, bbl, sbl -> bottom left
     *
     *                          ^
     *                        / t  \
     *                      ─┼─────┼─ \
     *                       │     │ btr \   str
     *                ─┼─────┼─────┼─────┼─\
     *                 │     │  e  │     │ btr \   str
     *          ─┼─────┼─────┼─────┼─────┼─────┼─\
     *           │     │  e  │  o  │  e  │     │ btr \ str
     *    ─┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─ \
     * ... │     │  e  │  o  │  S  │  o  │  e  │     │  r >
     *    ─┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─ /
     *           │     │  e  │  o  │  e  │     │
     *          ─┼─────┼─────┼─────┼─────┼─────┼─
     *                 │     │  e  │     │
     *                ─┼─────┼─────┼─────┼─
     *                       │     │
     *                      ─┼─────┼─
     *
     */
    override fun part2(): Long {
        val startingPoint = getStartingPoint()

        val totalSteps = 26_501_365

        // Starting point is always in the middle
        require(startingPoint.x == startingPoint.y)
        require(startingPoint.x == mapSize / 2)

        val gridWidth = totalSteps / mapSize - 1
        val oddMaps = ((gridWidth / 2) * 2 + 1).pow(2)
        val evenMaps = ((gridWidth + 1) / 2 * 2).pow(2)

        val oddPoints = this.fillGarden(startingPoint, mapSize * 2 + 1).size
        val evenPoints = this.fillGarden(startingPoint, mapSize * 2).size

        val cornerTop = this.fillGarden(Point(startingPoint.x, mapSize - 1), mapSize - 1).size
        val cornerRight = this.fillGarden(Point(0, startingPoint.y), mapSize - 1).size
        val cornerBottom = this.fillGarden(Point(startingPoint.x, 0), mapSize - 1).size
        val cornerLeft = this.fillGarden(Point(mapSize - 1, startingPoint.y), mapSize - 1).size

        val smallTopRight = this.fillGarden(Point(0, mapSize - 1), mapSize / 2 - 1).size
        val bigTopRight = this.fillGarden(Point(0, mapSize - 1), mapSize * 3 / 2 - 1).size

        val smallBottomRight = this.fillGarden(Point(0, 0), mapSize / 2 - 1).size
        val bigBottomRight = this.fillGarden(Point(0, 0), mapSize * 3 / 2 - 1).size

        val smallBottomLeft = this.fillGarden(Point(mapSize - 1, 0), mapSize / 2 - 1).size
        val bigBottomLeft = this.fillGarden(Point(mapSize - 1, 0), mapSize * 3 / 2 - 1).size

        val smallTopLeft = this.fillGarden(Point(mapSize - 1, mapSize - 1), mapSize / 2 - 1).size
        val bigTopLeft = this.fillGarden(Point(mapSize - 1, mapSize - 1), mapSize * 3 / 2 - 1).size

        val totalPoints = listOf<Long>(
            oddMaps * oddPoints.toLong(),
            evenMaps * evenPoints.toLong(),
            cornerTop + cornerRight + cornerBottom + cornerLeft.toLong(),
            (gridWidth.toLong() + 1) * (smallTopRight + smallBottomRight + smallBottomLeft + smallTopLeft),
            gridWidth.toLong() * (bigTopRight + bigBottomRight + bigBottomLeft + bigTopLeft)
        )

        println("Total steps: $totalPoints")
        return totalPoints.sum()
    }
}
