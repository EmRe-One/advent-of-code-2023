package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.automation.solve

internal class Day03Test {

    @Test
    fun `execute_tests`() {
        solve<Day03>(false) {
            Resources.resourceAsList("day03_example.txt")
                .joinToString("\n") part1 4361L part2 467835L
        }
    }

}
