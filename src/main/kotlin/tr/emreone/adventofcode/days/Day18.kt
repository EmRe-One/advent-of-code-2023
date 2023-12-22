package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day

import tr.emreone.kotlin_utils.extensions.MutableMapGrid
import tr.emreone.kotlin_utils.extensions.area
import tr.emreone.kotlin_utils.extensions.formatted
import tr.emreone.kotlin_utils.math.*
import java.util.LinkedList
import kotlin.collections.forEach

class Day18 : Day(18, 2023, "Lavaduct Lagoon") {

/*
    private val p2Instructions = input.map {
        val (_, _, c) = it.split(' ')
        val steps = c.drop(2).dropLast(2).toInt(16)
        val dir = when (c.dropLast(1).last()) {
            '0'  -> RIGHT
            '1'  -> DOWN
            '2'  -> LEFT
            else -> UP
        }
        dir to steps
    }
*/

    override fun part1(): Int {
        val instructions = inputAsList.map {
            val (d, s, _) = it.split(' ')
            val steps = s.toInt()
            val dir = when (d) {
                "R"  -> Direction4.EAST
                "D"  -> Direction4.SOUTH
                "L"  -> Direction4.WEST
                else -> Direction4.NORTH
            }
            dir to steps
        }

        val map: MutableMapGrid<String> = mutableMapOf()

        var pos = origin
        instructions.forEach { (d, s) ->
            val end = pos + (d.vector * s)
            (pos..end).forEach { map[it] = "#" }
            pos = end
        }

        val area = map.area
        val bigger = area + 1

        println(map.size)
        println(bigger.size)

        val q = LinkedList<Point>()
        q.add(bigger.upperLeft)

        val seen = mutableSetOf<Point>()
        while (q.isNotEmpty()) {
            val c = q.removeFirst()
            seen += c
            q += c.directNeighbors().filter { it !in q && it in bigger && it !in map && it !in seen }
        }

        map.formatted(area = bigger) { x, c ->
            when {
                x in seen -> "~"
                else      -> "#"
            }
        }

        println(seen.size)

        return bigger.size - seen.size
    }

/*
    override fun part2(): Long {
        val boundary = p2Instructions.sumOf { it.second.toLong() }
        val corners = p2Instructions.runningFold(origin) { acc, instruction ->
            acc + (instruction.first * instruction.second)
        }
        require(corners.first() == corners.last()) { "not a closed loop" }

        // area by Shoelace algorithm https://en.wikipedia.org/wiki/Shoelace_formula
        // https://youtu.be/0KjG8Pg6LGk?si=qC_1iX1YhQlGvI1o
        val area = abs(corners.zipWithNext().sumOf { (ci, cj) ->
            ci.x.toLong() * cj.y - cj.x.toLong() * ci.y
        }) / 2

        // according to Pick's theorem: A = i + b / 2 - 1
        // https://en.wikipedia.org/wiki/Pick%27s_theorem
        val inside = area - boundary / 2 + 1
        return inside + boundary
    }
*/

}
