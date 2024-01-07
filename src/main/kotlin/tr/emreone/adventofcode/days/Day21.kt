package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.solve
import tr.emreone.kotlin_utils.extensions.area
import tr.emreone.kotlin_utils.extensions.height
import tr.emreone.kotlin_utils.extensions.width
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.contains
import tr.emreone.kotlin_utils.math.directNeighbors
import tr.emreone.kotlin_utils.math.pow
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y

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

    private fun fillGarden(startingPoint: Point, steps: Int, withOverflow: Boolean = false): Set<Point> {
        val reachableGardenPlots = mutableSetOf<Point>()
        val seenGardenPlots = mutableSetOf(startingPoint)

        val q = ArrayDeque<Pair<Point, Int>>()
        q.add(startingPoint to steps)

        while (q.isNotEmpty()) {
            val (point, remainingSteps) = q.removeFirst()

            if (remainingSteps.mod(2) == 0) {
                reachableGardenPlots.add(point)
            }
            if (remainingSteps == 0) {
                continue
            }

            point.directNeighbors().forEach {
                if (withOverflow ) {
                    val adjustedPoint = Point(it.x.mod(this.mapSize), it.y.mod(this.mapSize))
                    if (map[adjustedPoint.y][adjustedPoint.x] != '#' && it !in seenGardenPlots) {
                        q.add(it to remainingSteps - 1)
                        seenGardenPlots.add(it)
                    }
                }
                else {
                    if (it in area && map[it.y][it.x] != '#' && it !in seenGardenPlots) {
                        q.add(it to remainingSteps - 1)
                        seenGardenPlots.add(it)
                    }
                }
            }
        }
        return reachableGardenPlots.toSet()
    }

    override fun part1(): Int {
        // find S in the map
        val startingPoint = getStartingPoint()
        val numberOfSteps = if (testInput) 6 else 64
        val reachableGardenPlots = this.fillGarden(startingPoint, numberOfSteps)

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

        val totalSteps = if (testInput) 499 else 26_501_365

        // Starting point is always in the middle
        require(startingPoint.x == startingPoint.y)
        require(startingPoint.x == mapSize / 2)

        val magicNumber = totalSteps / mapSize - 1
        val oddMaps = (magicNumber / 2 * 2 + 1L).pow(2).toLong()
        val evenMaps = ((magicNumber + 1) / 2 * 2L).pow(2).toLong()

        val pointsInOddMaps = this.fillGarden(startingPoint, mapSize * 2 + 1, false).size
        val pointsInEvenMaps = this.fillGarden(startingPoint, mapSize * 2, false).size

        val restStepsCorner = (totalSteps - (mapSize / 2 + 1)).mod(mapSize)
        val cornerTop = this.fillGarden(Point(startingPoint.x, mapSize - 1), restStepsCorner).size
        val cornerRight = this.fillGarden(Point(0, startingPoint.y), restStepsCorner).size
        val cornerBottom = this.fillGarden(Point(startingPoint.x, 0), restStepsCorner).size
        val cornerLeft = this.fillGarden(Point(mapSize - 1, startingPoint.y), restStepsCorner).size
        val cornerSum = cornerTop.toLong() + cornerRight + cornerBottom + cornerLeft

        val restStepsSmallEdges = (totalSteps - (mapSize + 1)).mod(mapSize) //
        val smallTopRight = this.fillGarden(Point(0, mapSize - 1), restStepsSmallEdges).size
        val smallBottomRight = this.fillGarden(Point(0, 0), restStepsSmallEdges).size
        val smallBottomLeft = this.fillGarden(Point(mapSize - 1, 0), restStepsSmallEdges).size
        val smallTopLeft = this.fillGarden(Point(mapSize - 1, mapSize - 1), restStepsSmallEdges).size
        val smallEdgesSum = smallTopRight.toLong() + smallBottomRight + smallBottomLeft + smallTopLeft

        val restStepsBigEdges = (totalSteps - (mapSize + 1)).mod(mapSize) + mapSize
        val bigTopRight = this.fillGarden(Point(0, mapSize - 1), restStepsBigEdges).size
        val bigBottomRight = this.fillGarden(Point(0, 0), restStepsBigEdges).size
        val bigBottomLeft = this.fillGarden(Point(mapSize - 1, 0), restStepsBigEdges).size
        val bigTopLeft = this.fillGarden(Point(mapSize - 1, mapSize - 1), restStepsBigEdges).size
        val bigEdgesSum = bigTopRight.toLong() + bigBottomRight + bigBottomLeft + bigTopLeft

        val totalPoints = listOf(
            oddMaps * pointsInOddMaps.toLong(),
            evenMaps * pointsInEvenMaps.toLong(),
            cornerSum,
            (magicNumber + 1) * smallEdgesSum,
            magicNumber * bigEdgesSum
        )

        return totalPoints.sum()
    }

}
