package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day01Test {

    @Test
    fun `execute_tests`() {
        solve<Day01>(false) {
            Resources.resourceAsList("day01_1_example.txt")
                .joinToString("\n") part1 142

            Resources.resourceAsList("day01_2_example.txt")
                .joinToString("\n") part2 281
        }
    }

}
