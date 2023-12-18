package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day15Test {

    @Test
    fun `execute_tests`() {
        solve<Day15>(false) {
            Resources.resourceAsList("day15_example.txt")
                .joinToString("\n") part1 1320 part2 145
        }
    }

}
