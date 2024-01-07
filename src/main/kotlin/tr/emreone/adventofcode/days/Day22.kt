package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Point2D
import tr.emreone.kotlin_utils.math.Point3D
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Day22 : Day(22, 2023, "Sand Slabs") {

    data class Brick(val id: Int, val a: Point3D, val b: Point3D) {
        private val xRange = rangeBy { it.x.toInt() }
        private val yRange = rangeBy { it.y.toInt() }
        private val zRange = rangeBy { it.z.toInt() }

        private fun rangeBy(f: (Point3D) -> Int) = min(f(a), f(b))..max(f(a), f(b))

        val xyPoints = xRange.flatMap { xi ->
            yRange.map { yi ->
                Point2D(xi.toLong(), yi.toLong())
            }
        }

        val minZ
            get() = zRange.first
        val maxZ
            get() = zRange.last

        fun fallBy(fallAmount: Int) = copy(
            a = Point3D(x = a.x, y = a.y, z = a.z - fallAmount),
            b = Point3D(x = b.x, y = b.y, z = b.z - fallAmount)
        )

        override fun toString() = "[$id] $a~$b"

        fun overlapsWith(other: Brick): Boolean {
            return max(this.a.x, other.a.x) <= min(this.b.x, other.b.x)
                    && max(this.a.y, other.a.y) <= min(this.b.y, other.b.y)
        }
    }

    data class Snapshot(val bricks: Set<Brick>) {

        fun makeBricksFall(): Snapshot {
            val tetris = BrickTetris(this)
            tetris.makeAllBricksFall(print = true)
            return Snapshot(tetris.bricks().toSet())
        }

        // returns number of bricks that would fall if given brick were removed
        private fun countAffectedBricksAfterRemovalOf(brick: Brick): Int {
            val tetris = BrickTetris(this)
            tetris.remove(brick)
            tetris.makeAllBricksFall()
            return tetris.bricks().count { it !in bricks }
        }

        fun findBrickRemovalCounts(): Map<Brick, Int> = bricks.associateWith { brick ->
            countAffectedBricksAfterRemovalOf(brick)
        }
    }

    data class BrickFall(val oldBrick: Brick, val fallAmount: Int) {
        fun newBrick(): Brick {
            return oldBrick.fallBy(fallAmount)
        }
    }

    private val bricks = inputAsList
        .mapIndexed { index, line ->
            val (left, right) = line.split("~")

            val (x1, y1, z1) = left.split(",").map { it.toLong() }
            val (x2, y2, z2) = right.split(",").map { it.toLong() }

            Brick(index, Point3D(x1, y1, z1), Point3D(x2, y2, z2))
        }
        .toSet()

    class BrickTetris(snapshot: Snapshot) {

        private val bricksByXY: MutableMap<Point2D, SortedSet<Brick>> = snapshot.bricks
            .flatMap { brick -> brick.xyPoints.map { xy -> xy to brick } }
            .groupBy({ (xy, _) -> xy }, { (_, brick) -> brick })
            .mapValues { (_, bricks) -> bricks.toSortedSet(compareBy { it.minZ }) }
            .toMutableMap()

        private val bricksByMinZ: SortedMap<Int, MutableList<Brick>> = snapshot.bricks
            .groupBy { it.minZ }
            .mapValues { (_, bricks) -> bricks.toMutableList() }
            .toSortedMap()


        private var lastCheckedMinZ = 1

        fun bricks() = bricksByMinZ.values.asSequence().flatten()

        private fun bricksAt(xy: Point2D) = bricksByXY[xy]!!

        private fun bricksAt(minZ: Int) = bricksByMinZ[minZ]

        fun remove(brick: Brick) {
            brick.xyPoints.forEach { xy -> bricksAt(xy) -= brick }

            bricksAt(brick.minZ)?.let {
                it -= brick
            }
        }

        private fun add(brick: Brick) {
            brick.xyPoints.forEach { xy -> bricksAt(xy) += brick }

            bricksAt(brick.minZ)?.let {
                it += brick
            }
        }

        fun makeAllBricksFall(print: Boolean = false) {
            var counter = 0
            while (tryMakeNextBrickFall()) {
                counter++
            }
            if (print) println("Made $counter bricks fall")
        }

        private fun tryMakeNextBrickFall(): Boolean {
            val brickFall = findNextBrickToFall() ?: return false

            remove(brickFall.oldBrick)
            add(brickFall.newBrick())

            lastCheckedMinZ = brickFall.oldBrick.minZ
            return true
        }

        private fun findNextBrickToFall(): BrickFall? = bricksByMinZ
            .tailMap(lastCheckedMinZ) // do not check below last checked minZ
            .values
            .asSequence()
            .flatten()
            .firstNotNullOfOrNull { brick -> findFallAmount(brick)?.let { fall -> BrickFall(brick, fall) } }

        private fun findFallAmount(brick: Brick): Int? {
            val zAfterFall = brick.xyPoints.maxOf { xy -> minZAfterFallAt(brick, xy) }
            return (brick.minZ - zAfterFall).takeIf { it > 0 }
        }

        private fun minZAfterFallAt(brick: Brick, xy: Point2D): Int {
            val bricksBelow = bricksAt(xy).headSet(brick)
            if (bricksBelow.isEmpty()) {
                return 1
            }
            return bricksBelow.last().maxZ + 1
        }
    }

    override fun part1(): Int {
        // https://www.youtube.com/watch?v=imz7uexX394

        val removal = Snapshot(this.bricks)
            .makeBricksFall()
            .findBrickRemovalCounts()

        println(removal)

        return removal
            .values
            .count { it == 0 }
    }

    override fun part2(): Int {
        return Snapshot(this.bricks)
            .makeBricksFall()
            .findBrickRemovalCounts()
            .values
            .sum()
    }

}
