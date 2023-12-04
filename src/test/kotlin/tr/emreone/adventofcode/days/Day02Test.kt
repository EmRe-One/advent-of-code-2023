package tr.emreone.adventofcode.days

import tr.emreone.utils.Resources
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day02Test {

    @Test
    fun part1() {
        val input = Resources.resourceAsList("day02_example.txt")
        assertEquals(8, Day02.part1(input, 12, 13, 14), "Day02, Part1 should be 8.")
    }

    @Test
    fun part2() {
        val input = Resources.resourceAsList("day02_example.txt")
        assertEquals(2286, Day02.part2(input), "Day02, Part2 should be 2286.")
    }

}