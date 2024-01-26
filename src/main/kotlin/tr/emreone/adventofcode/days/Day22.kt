package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Point3D
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.max
import kotlin.math.min

class Day22 : Day(22, 2023, "Sand Slabs") {

    data class Brick(val id: Int, var a: Point3D, var b: Point3D) {
        override fun toString() = "[$id] $a~$b"

        fun overlapsWith(other: Brick): Boolean {
            return max(this.a.x, other.a.x) <= min(this.b.x, other.b.x)
                    && max(this.a.y, other.a.y) <= min(this.b.y, other.b.y)
        }
    }

    private val bricks = inputAsList
        .mapIndexed { index, line ->
            val (left, right) = line.split("~")

            val (x1, y1, z1) = left.split(",").map { it.toLong() }
            val (x2, y2, z2) = right.split(",").map { it.toLong() }

            Brick(index, Point3D(x1, y1, z1), Point3D(x2, y2, z2))
        }
        .sortedBy { it.a.z }
        .also {
            it.forEachIndexed { index, brick ->
                var maxZ = 1L
                for (i in 0 until index) {
                    val other = it.elementAt(i)
                    if (brick.overlapsWith(other)) {
                        maxZ = max(maxZ, other.b.z + 1)
                    }
                }
                brick.b = Point3D(brick.b.x, brick.b.y, brick.b.z - brick.a.z + maxZ)
                brick.a = Point3D(brick.a.x, brick.a.y, maxZ)
            }
        }
        .sortedBy { it.a.z }


    override fun part1(): Int {
        // https://www.youtube.com/watch?v=imz7uexX394

        val supports = this.bricks.associate { it.id to mutableSetOf<Int>() }
        val supportedBy = this.bricks.associate { it.id to mutableSetOf<Int>() }

        this.bricks.forEachIndexed { j, upperBrick ->
            this.bricks.subList(0, j).forEachIndexed { i, lowerBrick ->
                if (upperBrick.overlapsWith(lowerBrick) && upperBrick.a.z == lowerBrick.b.z + 1) {
                    supports[i]!! += j
                    supportedBy[j]!! += i
                }
            }
        }

        return supports.values.count { set ->
            set.all { supportedBy[it]!!.size > 1 }
        }
    }

    override fun part2(): Int {
        val supports = this.bricks.associate { it.id to mutableSetOf<Int>() }
        val supportedBy = this.bricks.associate { it.id to mutableSetOf<Int>() }

        this.bricks.forEachIndexed { j, upperBrick ->
            this.bricks.subList(0, j).forEachIndexed { i, lowerBrick ->
                if (upperBrick.overlapsWith(lowerBrick) && upperBrick.a.z == lowerBrick.b.z + 1) {
                    supports[i]!! += j
                    supportedBy[j]!! += i
                }
            }
        }

        var total = 0

        this.bricks.forEachIndexed { i, brick ->
            val q: Deque<Int> = LinkedList(supports[brick.id]!!.filter { supportedBy[it]!!.size == 1 })

            val falling = q.toMutableSet()
            falling.add(i)

            while (q.isNotEmpty()) {
                val j = q.pop()
                for (k in (supports[j]!! - falling)) {
                    if (supportedBy[k]!!.all { falling.contains(it) }) {
                        q.add(k)
                        falling.add(k)
                    }
                }
            }

            total += falling.size - 1
        }

        return total
    }

}



