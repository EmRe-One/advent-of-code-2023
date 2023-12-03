package tr.emreone.adventofcode

import tr.emreone.utils.math.Point2D
import kotlin.math.abs

fun String.readTextGroups(delimitter: String = "\n\n"): List<String> {
    return this.split(delimitter)
}

fun Point2D.manhattanDistanceTo(other: Point2D): Long {
    return abs(x - other.x) + abs(y - other.y)
}

fun Point2D.move(direction: String, distance: Int): Point2D {
    return when (direction.lowercase()) {
        in arrayOf("u", "up", "n", "north", "norden")   -> Point2D(x, y + distance)
        in arrayOf("d", "down", "s", "south", "sueden") -> Point2D(x, y - distance)
        in arrayOf("r", "right", "e", "east", "osten")  -> Point2D(x + distance, y)
        in arrayOf("l", "left", "w", "west", "westen")  -> Point2D(x - distance, y)
        else                                            -> throw IllegalArgumentException("Unknown direction: $direction")
    }
}

operator fun IntRange.contains(other: IntRange): Boolean {
    return this.contains(other.first) && this.contains(other.last)
}

infix fun IntRange.overlaps(other: IntRange): Boolean {
    return this.first <= other.last && other.first <= this.last
}

fun List<Long>.product(): Long = this.reduce { acc, i -> acc * i }

infix fun Long.isDivisibleBy(divisor: Int): Boolean = this % divisor == 0L

// greatest common divisor
infix fun Long.gcd(other: Long): Long {
    var a = this
    var b = other
    while (b != 0L) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

// least common multiple
infix fun Long.lcm(other: Long): Long = (this * other) / (this gcd other)

fun List<Long>.gcd(): Long = this.reduce(Long::gcd)
fun List<Long>.lcm(): Long = this.reduce(Long::lcm)

