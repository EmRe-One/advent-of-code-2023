package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day01Test {

    @Test
    fun part1() {
        val input = Resources.resourceAsList("day01_1_example.txt")
        assertEquals(142, Day01.part1(input), "Day01, Part1 should be 142.")
    }

    @Test
    fun part2() {
        val input = Resources.resourceAsList("day01_2_example.txt")
        assertEquals(281, Day01.part2(input), "Day01, Part2 should be 281.")
    }

}
