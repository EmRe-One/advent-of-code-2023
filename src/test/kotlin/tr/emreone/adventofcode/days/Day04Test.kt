package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day04Test {

    @Test
    fun `execute_tests`() {
        solve<Day04>(false) {
            Resources.resourceAsList("day04_example.txt")
                .joinToString("\n") part1 13 part2 30
        }
    }

}
