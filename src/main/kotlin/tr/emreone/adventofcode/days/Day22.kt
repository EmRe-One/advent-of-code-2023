package tr.emreone.adventofcode.days

import tr.emreone.adventofcode.days.Day22.Brick.Companion.GROUND
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.extensions.intersects
import tr.emreone.kotlin_utils.math.Point3D
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.max
import kotlin.math.min

class Day22 : Day(22, 2023, "Sand Slabs") {

    private data class Brick(val id: Int, val x: IntRange, val y: IntRange, val z: IntRange) : Comparable<Brick> {
        val supporting = mutableSetOf<Brick>()
        val supportedBy = mutableSetOf<Brick>()

        override fun compareTo(other: Brick): Int =
            z.first - other.z.first

        fun supports(other: Brick) {
            supporting += other
            other.supportedBy += this
        }

        fun canSupport(other: Brick): Boolean =
            x intersects other.x && y intersects other.y && z.last + 1 == other.z.first

        fun onGround(): Boolean =
            z.first == GROUND

        fun fall(restingPlace: Int): Brick =
            copy(
                z = restingPlace..(restingPlace + (z.last - z.first))
            )

        companion object {

            const val GROUND = 1

            fun of(index: Int, input: String): Brick =
                input.split("~")
                    .map { side -> side.split(",").map { it.toInt() } }
                    .let { lists ->
                        val left = lists.first()
                        val right = lists.last()
                        Brick(
                            index,
                            left[0]..right[0],
                            left[1]..right[1],
                            left[2]..right[2]
                        )
                    }
        }
    }

    private fun List<Brick>.settle(): List<Brick> = buildList {
        this@settle.forEach { brick ->
            var current = brick
            do {
                var settled = false
                val supporters = filter { below -> below.canSupport(current) }
                if (supporters.isEmpty() && !current.onGround()) {
                    val restingPlace = filter { it.z.last < current.z.first - 1 }
                        .maxOfOrNull { it.z.last }?.let { it + 1 } ?: GROUND
                    current = current.fall(restingPlace)
                } else {
                    settled = true
                    supporters.forEach { below -> below.supports(current) }
                    add(current)
                }
            } while (!settled)
        }
    }

    private fun List<Brick>.structurallySignificant(): List<Brick> =
        filter { brick -> brick.supporting.any { it.supportedBy.size == 1 } }

    private fun Brick.topple(): Set<Brick> = buildSet {
        add(this@topple)
        val untoppled = (bricks - this).toMutableSet()
        do {
            val willFall = untoppled
                .filter { it.supportedBy.isNotEmpty() }
                .filter { it.supportedBy.all { brick -> brick in this } }
                .also {
                    untoppled.removeAll(it)
                    addAll(it)
                }
        } while (willFall.isNotEmpty())
    }

    private val bricks: List<Brick> = inputAsList
        .mapIndexed { index, row -> Brick.of(index, row) }
        .sorted()
        .settle()

    override fun part1(): Int {
        return bricks.size - bricks.structurallySignificant().size
    }

    override fun part2(): Int {
        return bricks.structurallySignificant().sumOf { it.topple().size - 1 }
    }

}



